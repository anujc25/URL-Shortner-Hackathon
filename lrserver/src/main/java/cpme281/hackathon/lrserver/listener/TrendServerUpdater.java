package cpme281.hackathon.lrserver.listener;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import cmpe281.hackathon.lrserver.constant.AWSEndPointConstants;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

public class TrendServerUpdater extends Thread {

	String callerIPAddress;
	String userAgent;
	String shortenURL;
	String redirectURL;

	public TrendServerUpdater(String shortenURL, String redirectURL, String ipAddress, String userAgent) {
		this.shortenURL = shortenURL;
		this.redirectURL = redirectURL;
		this.callerIPAddress = ipAddress;
		this.userAgent = userAgent;
	}

	public void run() {

		try {
			File database = new File(AWSEndPointConstants.LOCATION_DB_PATH);
			DatabaseReader dbReader = new DatabaseReader.Builder(database).build();

			InetAddress inetAddress = InetAddress.getByName(callerIPAddress);
			CityResponse cityRes = null;

			String country = "";
			
			try {
				cityRes = dbReader.city(inetAddress);
				country = cityRes.getCountry().getName();
			} catch (GeoIp2Exception e) {
				e.printStackTrace();
			}

			if (cityRes == null) {
				System.out.println("Location for " + callerIPAddress + " Not available...");
			}

			UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
			ReadableUserAgent agent = parser.parse(userAgent);

			String browserName = agent.getName();
			String osName = agent.getOperatingSystem().getName();
			
			StringBuffer data = new StringBuffer();
			data.append(shortenURL);
			data.append(",");
			data.append(redirectURL);
			data.append(",");
			data.append(callerIPAddress);
			data.append(",");
			data.append(country);
			data.append(",");
			data.append(osName);
			data.append(",");
			data.append(browserName);
			
			System.out.println("Data ==>" + data.toString());
			
			putInfoOnTrendQueue(data.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void putInfoOnTrendQueue(String data) {

		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		
		try {
			credentialsProvider.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.", e);
		}

		AmazonSQS sqs = AmazonSQSClientBuilder.standard().withCredentials(credentialsProvider)
				.withRegion(Regions.US_WEST_1).build();

		sqs.sendMessage(new SendMessageRequest(AWSEndPointConstants.TREND_DATA_QUEUE_ENDPOINT, data));
	}

}
