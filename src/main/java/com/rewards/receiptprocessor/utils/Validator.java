package com.rewards.receiptprocessor.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Validator {
	
	private static final Logger log = LogManager.getLogger(Validator.class);
	
	@Value("${date.format}")
	private String dateFormat;
	
	@Value("${time.format}")
	private String timeFormat;
	
	public boolean isValidDate(String dateString) {

	    try {
	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	    	
	    	Date date = sdf.parse(dateString);
	    	
	        return true;
	    }
	    catch (ParseException e) {
	    	log.error(e);
	        return false;
	    }
	}
	
	public boolean isValidTime(String timeString) {
        
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        	
        	sdf.parse(timeString);
        	
            return true;
            
        } catch (ParseException e) {
        	log.error(e);
            return false;
        }
    }
	
	public int getAlphaNumericCount(String retailer) {
		
		int count = 0;
		
		for(char ch : retailer.toCharArray()) {
			if(Character.isAlphabetic(ch) || Character.isDigit(ch)) count++;
		}
		
		return count;
	}
}
