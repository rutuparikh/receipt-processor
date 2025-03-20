package com.rewards.receiptprocessor.repository;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository{
	
	private final RedisTemplate<String, Object> redisTemplate;

	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	public void setHashOperations() {
		this.hashOperations = redisTemplate.opsForHash();
	}
	
	@Override
	public void createEntry(String id, HashMap<String,String> data) {
		hashOperations.putAll(id, data);
	}
	
}
