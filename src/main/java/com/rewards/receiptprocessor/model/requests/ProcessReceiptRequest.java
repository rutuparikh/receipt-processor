package com.rewards.receiptprocessor.model.requests;

import java.util.UUID;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@RequiredArgsConstructor
@Builder
@Data
public class ProcessReceiptRequest {
	
	@NonNull
	private final String retailer;
	
	private final String purchaseDate;
	
	private final String purchaseTime;
	
	private final Item[] items;
	
	private final String total;
	
}
