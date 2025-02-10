package com.rewards.receiptprocessor;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.rewards.receiptprocessor.controller.ReceiptProcessorController;
import com.rewards.receiptprocessor.services.ReceiptProcessorService;

@SpringBootTest
class ReceiptprocessorApplicationTests {

	@Autowired
    private ReceiptProcessorController receiptProcessorController;

    @Autowired
    private ReceiptProcessorService receiptProcessorService;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	private HashOperations<String, String, Object> hashOperations;
	
	@Autowired
    public void setHashOperations() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Test
    @DisplayName("Context Load Test")
    public void contextLoads() throws Exception {
        
    	assertThat(receiptProcessorController).isNotNull();
        assertThat(receiptProcessorService).isNotNull();
        assertThat(redisTemplate).isNotNull();
        assertThat(hashOperations).isNotNull();
    }

}
