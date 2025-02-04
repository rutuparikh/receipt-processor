package com.rewards.receiptprocessor.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.rewards.receiptprocessor.controller.ReceiptProcessorController;
import com.rewards.receiptprocessor.model.ProcessReceiptRequest;
import com.rewards.receiptprocessor.model.Receipt;
import com.rewards.receiptprocessor.model.ReceiptId;

@Service
public class ReceiptProcessorService {
	
	private static final Logger log = LogManager.getLogger(ReceiptProcessorService.class);
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	public ReceiptId processReceipt(ProcessReceiptRequest receipt) {
		
		log.info("Process Receipt Request received:"+ receipt.toString());
		
		if(receipt.getRetailer()==null || receipt.getRetailer().toString().length()==0 || 
				receipt.getPurchaseDate()==null || receipt.getPurchaseDate().toString().length()==0 ||
				receipt.getPurchaseTime()==null || receipt.getPurchaseTime().toString().length()==0 ||
				receipt.getItems()==null || receipt.getItems().toString().length()==0 ||
				receipt.getTotal() <= 0.0 ) {
			
//			return Bad request;
			return null;
		}
		
		
		return null;
		
	}

//    public void saveValue(String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
//    }
//
//    public Object getValue(String key) {
//        return redisTemplate.opsForValue().get(key);
//    }
}