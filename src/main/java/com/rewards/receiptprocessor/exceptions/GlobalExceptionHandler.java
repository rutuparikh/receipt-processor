package com.rewards.receiptprocessor.exceptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rewards.receiptprocessor.model.responses.FailureResponse;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ReceiptProcessorException.class)
	public ResponseEntity<FailureResponse> handleReceiptProcessorException(ReceiptProcessorException ex) {
        
		FailureResponse response = new FailureResponse(ex.getDescription());  

        return new ResponseEntity<>(response, ex.getErrorCode());
    }

}
