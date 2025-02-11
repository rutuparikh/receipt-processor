package com.rewards.receiptprocessor.model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Builder
@Data
public class Item {
	
	private final String shortDescription;
	
	private final String price;
}
