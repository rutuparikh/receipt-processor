package com.rewards.receiptprocessor.repository;

import java.util.HashMap;

public interface RedisRepository {
	
	public void createEntry(String key, HashMap<String, String> data);
	
	
}
