package com.rewards.receiptprocessor.model.responses;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
public class ProcessReceiptResponse {
	
	private final String id;
	
}
