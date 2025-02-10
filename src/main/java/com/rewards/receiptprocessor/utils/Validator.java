package com.rewards.receiptprocessor.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	@Value("${regex.retailer}")
	private String retailerRegex;
	
	@Value("${regex.description}")
	private String descriptionRegex;
	
	@Value("${regex.amount}")
	private String amountRegex;
	
	public boolean isValidDate(String dateString) {

	    try {
	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	    	sdf.setLenient(false);
	    	
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
        	
        	DateTimeFormatter strictTimeFormatter = DateTimeFormatter.ofPattern(timeFormat).withResolverStyle(ResolverStyle.STRICT);
        	LocalTime.parse(timeString, strictTimeFormatter);
        	
        	return true;
            
        } catch (Exception e) {
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
	
	public boolean isValidRetailer(String retailer) {
		
		System.out.println(retailer);
		
		try {
			Pattern pattern = Pattern.compile(retailerRegex);
	        Matcher matcher = pattern.matcher(retailer);
	        
	        return matcher.matches();
	        
		} catch(Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isValidDescription(String description) {
		
		System.out.println(description);
		
		try {
			Pattern pattern = Pattern.compile(descriptionRegex);
	        Matcher matcher = pattern.matcher(description);
	     
	        return matcher.matches();
	        
		} catch(Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isValidAmount(String amount) {
		
		System.out.println(amount);
		
		try {
			Pattern pattern = Pattern.compile(amountRegex);
	        Matcher matcher = pattern.matcher(amount);
	        
	        return matcher.matches();
	        
		} catch(Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			return false;
		}
	}
	
	
}
