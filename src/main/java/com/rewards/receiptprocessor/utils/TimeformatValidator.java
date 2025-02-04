package com.rewards.receiptprocessor.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeformatValidator {
	
	public static boolean isValidTime(String timeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime.parse(timeStr, formatter);
            return true;
            
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
