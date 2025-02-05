package com.rewards.receiptprocessor.model;

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
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProcessReceiptRequest {
	
	@NonNull
	private String retailer;
	
	private String purchaseDate;
	
	private String purchaseTime;
	
	private Item[] items;
	
	private float total;
	
}
