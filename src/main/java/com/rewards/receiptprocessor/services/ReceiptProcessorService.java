package com.rewards.receiptprocessor.services;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rewards.receiptprocessor.model.GetPointsResponse;
import com.rewards.receiptprocessor.model.Item;
import com.rewards.receiptprocessor.model.ProcessReceiptRequest;
import com.rewards.receiptprocessor.model.ProcessReceiptResponse;
import com.rewards.receiptprocessor.utils.Validator;

@Service
public class ReceiptProcessorService {
	
	private static final Logger log = LogManager.getLogger(ReceiptProcessorService.class);
	
	@Value("${date.format}")
	private String dateFormat;
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	ProcessReceiptResponse processReceiptResponse;
	
	@Autowired
	GetPointsResponse getPointsResponse;
	
	@Autowired
	Validator validator;
	
	public ResponseEntity<ProcessReceiptResponse> processReceipt(ProcessReceiptRequest receipt) {
		
		log.info("Process Receipt Request received: "+ receipt.toString());
		
		try {
			
			processReceiptResponse.setId("");
//			null/empty checks
			if(receipt.getRetailer()==null || receipt.getRetailer().toString().length()==0 || 
					receipt.getPurchaseDate()==null || receipt.getPurchaseDate().toString().length()==0 ||
					receipt.getPurchaseTime()==null || receipt.getPurchaseTime().toString().length()==0 ||
					receipt.getItems()==null || receipt.getItems().toString().length()==0 ||
					receipt.getTotal() <= 0.0 ) {

				log.info("ProcessReceiptResponse: "+ processReceiptResponse.toString());
				return ResponseEntity.badRequest().body(processReceiptResponse);

			}
			
			log.info("null/empty check success");
			
//			format checks
			if(!validator.isValidTime(receipt.getPurchaseTime()) ||
					!validator.isValidDate(receipt.getPurchaseDate())){
				log.info("ProcessReceiptResponse: "+ processReceiptResponse.toString());
				return ResponseEntity.badRequest().body(processReceiptResponse);
			}
			
			
			for(Item item : receipt.getItems()) {
				if(item.getShortDescription()==null || item.getShortDescription().toString().strip().length()==0 || item.getPrice()<=0.0) {
					log.info("ProcessReceiptResponse: "+ processReceiptResponse.toString());
					return ResponseEntity.badRequest().body(processReceiptResponse);
				}
			}
			log.info("format check success");
			
			int points = calculatePoints(receipt);
			
			log.info("generating ID...");
			
			String uuid = UUID.randomUUID().toString();
			processReceiptResponse.setId(uuid);
			
			savePoints(uuid, points);
		
				
		} catch(Exception e) {
			log.error(e.toString());
			log.info("ProcessReceiptResponse: "+ processReceiptResponse.toString());
			return ResponseEntity.internalServerError().body(processReceiptResponse);
		}
		log.info("ProcessReceiptResponse: "+ processReceiptResponse.toString());
		return ResponseEntity.status(201).body(processReceiptResponse);
		
	}

	private void savePoints(String uuid, int points) {
		
		try {
			redisTemplate.opsForValue().set(uuid, points);
			log.info("Receipt ID and points saved");
			
		} catch(Exception e) {
			log.error(e.toString());
		}
		
	}

	private int calculatePoints(ProcessReceiptRequest receipt) {
		
		log.info("calculating points...");
		
		try {
			int points = 0;
			
//			Rule 1
			points += validator.getAlphaNumericCount(receipt.getRetailer().strip());
			
//			Rule 2
			double fractionalPart = receipt.getTotal() - Math.floor(receipt.getTotal());
			if(fractionalPart == 0.0) points += 50;
			
//			Rule 3
			if(fractionalPart%0.25==0) points += 25;
			
//			Rule 4
			int itemCount = receipt.getItems().length;
			points += (itemCount/2)*5;
			
//			Rule 5		
			for(Item item : receipt.getItems()) {

				String desc = item.getShortDescription();
				double price = item.getPrice();
				
				if(desc.strip().length()%3==0) {
					price = price*0.2;
					points += Math.ceil(price);
				}
			}
			
//			Rule 6
			int day = Integer.parseInt(receipt.getPurchaseDate().toString().split("-")[2]);
			if(day%2==1) points += 6;
			
//			Rule 7
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
			return 0;
		}
		
	}
	
	public ResponseEntity<GetPointsResponse> getPoints(String uuid) {
		
		log.info("Get Points Request received: "+ uuid);
		
		try {
			
			if(redisTemplate.opsForValue().get(uuid)!=null) {
				int points = (int) redisTemplate.opsForValue().get(uuid);
				log.info("points retrived: "+points);
				
				getPointsResponse.setPoints(points);
				
				return ResponseEntity.ok(getPointsResponse);
			}
			else {
				
				log.info("ID not found: "+uuid);
				
				return ResponseEntity.notFound().build();
			}
			
		} catch(Exception e) {
			log.error(e.toString());
			return ResponseEntity.internalServerError().build();
		}
	}
}