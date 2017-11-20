package cpme281.hackathon.lrserver.listener;

import java.util.List;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

import cmpe281.hackathon.lrserver.constant.AWSEndPointConstants;
import redis.clients.jedis.Jedis;

public class ShortenURLQueueListener extends Thread {

	Jedis jedisInstance;
	AmazonSQS queue;

	public ShortenURLQueueListener(AmazonSQS queue, Jedis jedis) {
		this.queue = queue;
		this.jedisInstance = jedis;
	}

	public void run() {

		System.out.println("URL Message Listener Thread Started...");

		while (true) {
			
			 ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
						AWSEndPointConstants.URL_MESSAGE_QUEUE_ENDPOINT);
		        			
			try {
				
				receiveMessageRequest.setWaitTimeSeconds(2);
				
				List<Message> messages = queue.receiveMessage(receiveMessageRequest).getMessages();

				for (Message message : messages) {

					String messageBody = message.getBody();
					String[] messageValues = messageBody.split(",");

					String cacheKey = messageValues[0];
					String cacheValue = messageValues[1];

					jedisInstance.set(cacheKey, cacheValue);

					System.out.println("Message..." + messageBody);
					
					// Deleting the message after consuming it from the queue
					queue.deleteMessage(AWSEndPointConstants.URL_MESSAGE_QUEUE_ENDPOINT, 
							message.getReceiptHandle());
				}
			} finally {
				
			}
		}
	}
}
