package com.rewards.receiptprocessor.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rewards.receiptprocessor.exceptions.ReceiptProcessorException;
import com.rewards.receiptprocessor.model.requests.Item;
import com.rewards.receiptprocessor.model.requests.ProcessReceiptRequest;
import com.rewards.receiptprocessor.model.responses.GetPointsResponse;
import com.rewards.receiptprocessor.model.responses.ProcessReceiptResponse;
import com.rewards.receiptprocessor.services.ReceiptProcessorService;
import com.rewards.receiptprocessor.utils.Validator;

@Service
public class ReceiptProcessorServiceImpl implements ReceiptProcessorService {

	private static final Logger log = LogManager.getLogger(ReceiptProcessorServiceImpl.class);

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	public void setHashOperations() {
		this.hashOperations = redisTemplate.opsForHash();
	}

	@Autowired
	Validator validator;

	@Override
	public ResponseEntity<ProcessReceiptResponse> processReceipt(ProcessReceiptRequest receipt)
			throws ReceiptProcessorException {

		log.debug("Process Receipt Request received: " + receipt.toString());

		if (receipt.getRetailer() == null || receipt.getRetailer().toString().length() == 0
				|| receipt.getPurchaseDate() == null || receipt.getPurchaseDate().toString().length() == 0
				|| receipt.getPurchaseTime() == null || receipt.getPurchaseTime().toString().length() == 0
				|| receipt.getItems() == null || receipt.getItems().length < 1 || receipt.getTotal() == null
				|| receipt.getTotal().length() == 0) {

			throw new ReceiptProcessorException("The receipt is invalid.", HttpStatus.BAD_REQUEST,
					"The receipt is invalid.");
		}

		if (!validator.isValidTime(receipt.getPurchaseTime()) || !validator.isValidDate(receipt.getPurchaseDate())) {

			log.debug("Invalid date or time.");

			throw new ReceiptProcessorException("The receipt is invalid.", HttpStatus.BAD_REQUEST,
					"The receipt is invalid.");

		}

		if (!validator.isValidAmount(receipt.getTotal())) {

			log.debug("Total pattern invalid.");

			throw new ReceiptProcessorException("The receipt is invalid.", HttpStatus.BAD_REQUEST,
					"The receipt is invalid.");
		}

		for (Item item : receipt.getItems()) {

			if (item.getShortDescription() == null || item.getShortDescription().toString().strip().length() == 0
					|| !validator.isValidDescription(item.getShortDescription().toString().strip())
					|| item.getPrice() == null || item.getPrice().length() == 0
					|| !validator.isValidAmount(item.getPrice())) {

				throw new ReceiptProcessorException("The receipt is invalid.", HttpStatus.BAD_REQUEST,
						"The receipt is invalid.");
			}
		}

		if (!validator.isValidRetailer(receipt.getRetailer())) {

			log.debug("Retailer patten invalid.");

			throw new ReceiptProcessorException("The receipt is invalid.", HttpStatus.BAD_REQUEST,
					"The receipt is invalid.");
		}

		log.info("Receipt validated");

		int points = calculatePoints(receipt);

		String uuid = UUID.randomUUID().toString();
		
		ProcessReceiptResponse processReceiptResponse = ProcessReceiptResponse.builder().id(uuid).build();

		savePoints(uuid, points);

		log.debug("ProcessReceiptResponse: " + processReceiptResponse.toString());

		return new ResponseEntity<>(processReceiptResponse, HttpStatus.OK);

	}

	private void savePoints(String uuid, int points) {

		log.debug("saving points...");

		try {

			Map<String, String> data = new HashMap<>();

			data.put("points", String.valueOf(points));

			data.put("created_at", new Date().toString());

			hashOperations.putAll(uuid, data);

			log.info("Receipt ID and points saved");

		} catch (Exception e) {

			log.error(e);
		}
	}

	private int calculatePoints(ProcessReceiptRequest receipt) {

		log.debug("calculating points...");

		try {
			int points = 0;

//			Rule 1 One point for every alphanumeric character in the retailer name
			points += validator.getAlphaNumericCount(receipt.getRetailer().strip());

//			Rule 2 50 points if the total is a round dollar amount with no cents
			double fractionalPart = Float.parseFloat(receipt.getTotal())
					- Math.floor(Float.parseFloat(receipt.getTotal()));
			if (fractionalPart == 0.0)
				points += 50;

//			Rule 3 25 points if the total is a multiple of 0.25
			if (fractionalPart % 0.25 == 0)
				points += 25;

//			Rule 4 5 points for every two items on the receipt
			int itemCount = receipt.getItems().length;
			points += (itemCount / 2) * 5;

//			Rule 5 If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer. The result is the number of points earned
			for (Item item : receipt.getItems()) {

				String desc = item.getShortDescription();
				double price = Float.parseFloat(item.getPrice());

				if (desc.strip().length() % 3 == 0) {
					price = price * 0.2;
					points += Math.ceil(price);
				}
			}

//			Rule 6 6 points if the day in the purchase date is odd
			int day = Integer.parseInt(receipt.getPurchaseDate().toString().split("-")[2]);
			if (day % 2 == 1)
				points += 6;

//			Rule 7 10 points if the time of purchase is after 2:00pm and before 4:00pm
			int hour = Integer.parseInt(receipt.getPurchaseTime().toString().split(":")[0]);
			int min = Integer.parseInt(receipt.getPurchaseTime().toString().split(":")[1]);

			if (hour >= 14 && hour < 16) {

				if (hour == 14 && min == 0)
					points += 0;
				else
					points += 10;

			}
			
			log.info("Points calculated: " + points);
			
			return points;

		} catch (Exception e) {
			
			log.error(e);

			return 0;
		}

	}

	public ResponseEntity<GetPointsResponse> getPoints(String uuid) throws ReceiptProcessorException {

		log.debug("Get Points Request received: " + uuid);

		Map<String, Object> data = hashOperations.entries(uuid);

		if (data != null && data.get("points") != null) {

			log.info("Points retrived: " + data.get("points"));
			
			GetPointsResponse getPointsResponse = GetPointsResponse.builder().points(Integer.parseInt(data.get("points").toString())).build();

			log.debug("GetPointsResponse: " + getPointsResponse.toString());

			return new ResponseEntity<>(getPointsResponse, HttpStatus.OK);

		} else {

			log.debug("ID not found: " + uuid);

			throw new ReceiptProcessorException("No receipt found for that ID.", HttpStatus.NOT_FOUND,
					"No receipt found for that ID.");

		}
	}

	public String getDummyJsonDataFromHash(String hashKey, String fieldKey) {
		
		Map<Object, Object> data = redisTemplate.opsForHash().entries(hashKey);
		
		return (String) data.get(fieldKey);
	}
}