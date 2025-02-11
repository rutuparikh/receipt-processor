package com.rewards.receiptprocessor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rewards.receiptprocessor.exceptions.ReceiptProcessorException;
import com.rewards.receiptprocessor.model.requests.ProcessReceiptRequest;
import com.rewards.receiptprocessor.model.responses.GetPointsResponse;
import com.rewards.receiptprocessor.model.responses.ProcessReceiptResponse;
import com.rewards.receiptprocessor.services.ReceiptProcessorService;

@RestController
@RequestMapping("/receipts")
public class ReceiptProcessorController {
	
    private ReceiptProcessorService receiptProcessorService;
	
	public ReceiptProcessorController(ReceiptProcessorService receiptProcessorService) {
		this.receiptProcessorService = receiptProcessorService;
	}
	
	@GetMapping("/{id}/points")
	public ResponseEntity<GetPointsResponse> getPoints(@PathVariable(required = true) String id) throws ReceiptProcessorException {
		
		return receiptProcessorService.getPoints(id);
		
	}
	
	@PostMapping(value = "/process", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ProcessReceiptResponse> processReceipt(@RequestBody (required=true) ProcessReceiptRequest request) throws ReceiptProcessorException {
		
		return receiptProcessorService.processReceipt(request);
	
	}

}
