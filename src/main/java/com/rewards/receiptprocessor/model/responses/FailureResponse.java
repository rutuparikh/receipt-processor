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
public class FailureResponse {
	
	private final String description;
}
