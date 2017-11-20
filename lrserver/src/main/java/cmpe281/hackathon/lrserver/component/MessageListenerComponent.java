package cmpe281.hackathon.lrserver.component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import cmpe281.hackathon.lrserver.constant.AWSEndPointConstants;
import cpme281.hackathon.lrserver.listener.ShortenURLQueueListener;
import redis.clients.jedis.Jedis;

public class MessageListenerComponent {
	
	Thread shortenURLQueueListener;
	Jedis jedis;
	
	@PostConstruct
	public void init(){
				
		/* Setting up the AWS configurations for the Queue listener*/
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        
		try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                               .withCredentials(credentialsProvider)
                               .withRegion(Regions.US_WEST_1)
                               .build();
        
        jedis = new Jedis(AWSEndPointConstants.CACHE_REDIS_INSTANCE_ENDPOINT, 6379);
	     
        shortenURLQueueListener = new ShortenURLQueueListener(sqs, jedis);
        shortenURLQueueListener.setDaemon(true);
        shortenURLQueueListener.start();
	}
	
	
	@PreDestroy
	public void destroy(){
		
		if(shortenURLQueueListener.isAlive()){
			shortenURLQueueListener.stop();
		}
		
		jedis.close();
	}

}
