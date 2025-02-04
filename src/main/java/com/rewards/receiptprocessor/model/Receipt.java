package com.rewards.receiptprocessor.model;

import java.util.UUID;
import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Receipt {
	
	@Id
    private UUID id;
	
	private ProcessReceiptRequest request;
	
	private int points;
	
}
