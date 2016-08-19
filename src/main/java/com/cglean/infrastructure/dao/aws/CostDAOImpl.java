package com.cglean.infrastructure.dao.aws;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.cglean.infrastructure.dao.CostDAO;
import com.cglean.infrastructure.domain.DailyCost;
import com.cglean.infrastructure.domain.TimeInterval;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

@Component
public class CostDAOImpl implements CostDAO {
	private Log log = LogFactory.getLog(CostDAOImpl.class);

	@Value("${aws.billing.bucket.name:cglean-billing}")
	private String bucketName;

	@Value("${aws.billing.bucket.prefix:daily/summary}")
	private String bucketPrefix;

	@Value("${aws.access.key:SUPPLY-RUNTIME}")
	private String accessKey;

	@Value("${aws.secret.key:SUPPLY-RUNTIME}")
	private String secretKey;

	// AmazonS3Client is thread safe. Create only once.
	private AmazonS3Client client;

	@PostConstruct
	public void init() {
		client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
		log.info("AWS Client created");
	}

	@Override
	public Collection<DailyCost> findCost(TimeInterval interval) {
		// Filter out the keys that we need. Filter because we could pick up
		// really old files from the bucket.
		List<CostSourceKey> costSourceKeys = filterKeys(interval);

		// Setup bean mapping from CSV
		CsvToBean<CostItem> csvToBean = new CsvToBean<CostItem>();
		HeaderColumnNameMappingStrategy<CostItem> strategy = new HeaderColumnNameMappingStrategy<CostItem>();
		strategy.setType(CostItem.class);

		// Collect by date
		Map<LocalDate, DailyCost> costByDate = new HashMap<LocalDate, DailyCost>();

		// Cost items get duplicated across files. Avoid picking up duplicates
		Set<String> processed = new HashSet<String>();

		for (CostSourceKey costSourceKey : costSourceKeys) {
			S3Object gzipCSVFile = client.getObject(new GetObjectRequest(bucketName, costSourceKey.getKey()));
			log.info("Attempting to process file with key " + costSourceKey.getKey());
			// Process the objectData stream.
			try {
				InputStream objectData = gzipCSVFile.getObjectContent();
				List<CostItem> costLineItems = csvToBean.parse(strategy,
						new CSVReader(new InputStreamReader(new GZIPInputStream(objectData))));
				objectData.close();
				costLineItems = costLineItems.stream().filter(item -> interval.isWithin(item.getInterval().getStart()))
						.collect(Collectors.toList());
				for (CostItem myBean : costLineItems) {
					// Do not pick up duplicates.  Use identity and date to create a synthetic key
					String identityLineItemId = myBean.getIdentityLineItemId() + myBean.getInterval();					
					if (!processed.contains(identityLineItemId)) {
						processed.add(identityLineItemId);
						LocalDate forDate = myBean.getInterval().getStart();
						if (!costByDate.containsKey(forDate)) {
							DailyCost dc = new DailyCost();
							dc.setCostDate(forDate);
							dc.setCost(BigDecimal.ZERO);
							costByDate.put(forDate, dc);
						}
						costByDate.get(forDate).accumulate(myBean.getBlendedCost());
					}
				}

			} catch (IOException e) {
				log.error("IO Exception with processing " + costSourceKey.getKey(), e);
			}
		}
		return costByDate.values();
	}

	private List<CostSourceKey> filterKeys(final TimeInterval interval) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		ObjectListing objectListing = client
				.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(bucketPrefix));
		return objectListing.getObjectSummaries().stream().filter(summary -> summary.getKey().endsWith(".csv.gz"))
				.map(summary -> {
					String key = summary.getKey();
					// daily/summary/20160801-20160901/b9eda3d3-b8bd-4e03-9c71-431b3857110b/summary-1.csv.gz
					String[] bustedKey = key.split("/");
					String dates[] = bustedKey[2].split("-");
					CostSourceKey sourceKey = new CostSourceKey();
					sourceKey.setKey(key);
					sourceKey.setGuid(bustedKey[3]);
					sourceKey.setInterval(new TimeInterval(LocalDate.parse(dates[0], formatter), LocalDate.parse(dates[1], formatter)));
					return sourceKey;

				}).filter(translatedKey -> interval.overlapsWith(translatedKey.getInterval()))
				.collect(Collectors.toList());
	}

}
