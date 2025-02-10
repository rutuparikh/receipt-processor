package com.rewards.receiptprocessor.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rewards.receiptprocessor.model.GetPointsResponse;
import com.rewards.receiptprocessor.model.ProcessReceiptRequest;
import com.rewards.receiptprocessor.model.ProcessReceiptResponse;
import com.rewards.receiptprocessor.services.ReceiptProcessorService;

@RestController
@RequestMapping("/receipts")
public class ReceiptProcessorController {
	
	private static final Logger log = LogManager.getLogger(ReceiptProcessorController.class);
	
	@Autowired
    private ReceiptProcessorService receiptProcessor;
	
	@GetMapping("/{id}/points")
	public ResponseEntity<Object> getPoints(@PathVariable(required = true) String id) {
		ResponseEntity<Object> response = receiptProcessor.getPoints(id);
		return response;
		
	}
	
	@PostMapping(value = "/process", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> processReceipt(@RequestBody (required=true) ProcessReceiptRequest request) {
		ResponseEntity<Object> response = receiptProcessor.processReceipt(request);
		return response;
	}

}
