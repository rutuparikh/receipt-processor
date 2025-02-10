package com.rewards.receiptprocessor.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rewards.receiptprocessor.model.FailureResponse;
import com.rewards.receiptprocessor.model.GetPointsResponse;
import com.rewards.receiptprocessor.model.Item;
import com.rewards.receiptprocessor.model.ProcessReceiptRequest;
import com.rewards.receiptprocessor.model.ProcessReceiptResponse;
import com.rewards.receiptprocessor.utils.Validator;

@Service
public class ReceiptProcessorService {
	
	private static final Logger log = LogManager.getLogger(ReceiptProcessorService.class);
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	private HashOperations<String, String, Object> hashOperations;
	
	@Autowired
    public void setHashOperations() {
        this.hashOperations = redisTemplate.opsForHash();
    }
	
	@Autowired
	ProcessReceiptResponse processReceiptResponse;
	
	@Autowired
	GetPointsResponse getPointsResponse;
	
	@Autowired
	FailureResponse failureResponse;
	
	@Autowired
	Validator validator;
	
	public ResponseEntity<Object> processReceipt(ProcessReceiptRequest receipt) {
		
		log.info("Process Receipt Request received: "+ receipt.toString());
		
		try {
			
			processReceiptResponse.setId("");
//			null/empty checks
			if(receipt.getRetailer()==null || receipt.getRetailer().toString().length()==0 || 
					receipt.getPurchaseDate()==null || receipt.getPurchaseDate().toString().length()==0 ||
					receipt.getPurchaseTime()==null || receipt.getPurchaseTime().toString().length()==0 ||
					receipt.getItems()==null || receipt.getItems().length < 1 ||
					receipt.getTotal()==null || receipt.getTotal().length()==0) {

				failureResponse.setDescription("The receipt is invalid.");
				
				log.info("ProcessReceiptResponse: "+ failureResponse.toString());
				return new ResponseEntity<>(failureResponse, HttpStatus.BAD_REQUEST);

			}
			
			log.info("null/empty check success");
			
//			format checks
			if(!validator.isValidTime(receipt.getPurchaseTime()) || !validator.isValidDate(receipt.getPurchaseDate())){
				
				log.info("Invalid date or time.");
				
				failureResponse.setDescription("The receipt is invalid.");
				
				log.info("ProcessReceiptResponse: "+ failureResponse.toString());
				return new ResponseEntity<>(failureResponse, HttpStatus.BAD_REQUEST);
			}
			
			if(!validator.isValidAmount(receipt.getTotal())) {
				
				log.info("Total pattern invalid.");
				
				failureResponse.setDescription("The receipt is invalid.");
				
				log.info("ProcessReceiptResponse: "+ failureResponse.toString());
				return new ResponseEntity<>(failureResponse, HttpStatus.BAD_REQUEST);
			}
			
			for(Item item : receipt.getItems()) {
				
				if(item.getShortDescription()==null || item.getShortDescription().toString().strip().length()==0 || 
						!validator.isValidDescription(item.getShortDescription().toString().strip()) ||
						item.getPrice()==null || item.getPrice().length()==0 ||
						!validator.isValidAmount(item.getPrice())) {
					
					failureResponse.setDescription("The receipt is invalid.");
					
					log.info("ProcessReceiptResponse: "+ failureResponse.toString());
					return new ResponseEntity<>(failureResponse, HttpStatus.BAD_REQUEST);
				}
			}
			
			if(!validator.isValidRetailer(receipt.getRetailer())) {
				
				log.info("Retailer patten invalid.");
				
				failureResponse.setDescription("The receipt is invalid.");
				
				log.info("ProcessReceiptResponse: "+ failureResponse.toString());
				return new ResponseEntity<>(failureResponse, HttpStatus.BAD_REQUEST);
			}

			
			log.info("format checks success");
			log.info("Receipt validated");
			
			int points = calculatePoints(receipt);
			
			log.info("generating ID...");
			
			String uuid = UUID.randomUUID().toString();
//			uuid = "a4a214d2-17ad-48ec-8563-0e5fb0549f21";
			
			while(!isIdUnique(uuid)) {
				uuid = UUID.randomUUID().toString();
			}
			
			processReceiptResponse.setId(uuid);
			
			savePoints(uuid, points);
			
			log.info("ProcessReceiptResponse: "+ processReceiptResponse.toString());
			return new ResponseEntity<>(processReceiptResponse, HttpStatus.OK);
				
		} catch(Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			
			failureResponse.setDescription("Internal Server Error");
			return new ResponseEntity<>(failureResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	private void savePoints(String uuid, int points) {
		
		try {
			Map<String, String> data = new HashMap<>();
			data.put("points", String.valueOf(points));
			data.put("created_at", new Date().toString());
			
			
			hashOperations.putAll(uuid, data);


			log.info("Receipt ID and points saved");
			
		} catch(Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		
	}

	private int calculatePoints(ProcessReceiptRequest receipt) {
		
		log.info("calculating points...");
		
		try {
			int points = 0;
			
//			Rule 1 One point for every alphanumeric character in the retailer name
			points += validator.getAlphaNumericCount(receipt.getRetailer().strip());
			
//			Rule 2 50 points if the total is a round dollar amount with no cents
			double fractionalPart = Float.parseFloat(receipt.getTotal()) - Math.floor(Float.parseFloat(receipt.getTotal()));
			if(fractionalPart == 0.0) points += 50;
			
//			Rule 3 25 points if the total is a multiple of 0.25
			if(fractionalPart%0.25==0) points += 25;
			
//			Rule 4 5 points for every two items on the receipt
			int itemCount = receipt.getItems().length;
			points += (itemCount/2)*5;
			
//			Rule 5 If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer. The result is the number of points earned
			for(Item item : receipt.getItems()) {

				String desc = item.getShortDescription();
				double price = Float.parseFloat(item.getPrice());
				
				if(desc.strip().length()%3==0) {
					price = price*0.2;
					points += Math.ceil(price);
				}
			}
			
//			Rule 6 6 points if the day in the purchase date is odd
			int day = Integer.parseInt(receipt.getPurchaseDate().toString().split("-")[2]);
			if(day%2==1) points += 6;
			
//			Rule 7 10 points if the time of purchase is after 2:00pm and before 4:00pm
			int hour = Integer.parseInt(receipt.getPurchaseTime().toString().split(":")[0]);
			int min = Integer.parseInt(receipt.getPurchaseTime().toString().split(":")[1]);
			
			if(hour>=14 && hour<16) {
				
				if(hour==14 && min==0) points += 0;
				else points += 10;
				
			}
			log.info("calculated points: "+ points);
			return points;
			
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			return 0;
		}
		
	}
	
	public ResponseEntity<Object> getPoints(String uuid) {
		
		log.info("Get Points Request received: "+ uuid);
		
		try {
			
			Map<String, Object> data = hashOperations.entries(uuid);
			log.info(data.toString());
			
			if(data!=null && data.get("points")!=null) {
				
				log.info("points retrived: "+data.get("points"));
				
				getPointsResponse.setPoints(Integer.parseInt(data.get("points").toString()));
				
				log.info("GetPointsResponse: "+ getPointsResponse.toString());

				return new ResponseEntity<>(getPointsResponse, HttpStatus.OK);
			}
			else {
				
				log.info("ID not found: "+uuid);
				
				failureResponse.setDescription("No receipt found for that ID.");
				
				log.info("GetPointsResponse: "+ failureResponse.toString());
				
				return new ResponseEntity<>(failureResponse, HttpStatus.NOT_FOUND);
			}
			
		} catch(Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			
			failureResponse.setDescription("Internal Server Error");
			return new ResponseEntity<>(failureResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public boolean isIdUnique(String uuid) {
		
		try {
			if(redisTemplate.hasKey(uuid)) {
				log.info("Receipt ID is not unique. ");
				return false;
			}
			else {
				
				log.info("Receipt ID is unique.");
				return true;
			}
			
		} catch(Exception e) {
			log.error(e.toString());
			throw e;
		}
	}
	
	public String getDummyJsonDataFromHash(String hashKey, String fieldKey) {
        Map<Object, Object> data = redisTemplate.opsForHash().entries(hashKey);
        return (String) data.get(fieldKey);
    }
}