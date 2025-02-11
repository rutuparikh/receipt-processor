package com.rewards.receiptprocessor.services;

import org.springframework.http.ResponseEntity;

import com.rewards.receiptprocessor.exceptions.ReceiptProcessorException;
import com.rewards.receiptprocessor.model.requests.ProcessReceiptRequest;
import com.rewards.receiptprocessor.model.responses.GetPointsResponse;
import com.rewards.receiptprocessor.model.responses.ProcessReceiptResponse;

public interface ReceiptProcessorService {
	
	public ResponseEntity<ProcessReceiptResponse> processReceipt(ProcessReceiptRequest receipt) throws ReceiptProcessorException;
	
	public ResponseEntity<GetPointsResponse> getPoints(String id) throws ReceiptProcessorException;
	
}
