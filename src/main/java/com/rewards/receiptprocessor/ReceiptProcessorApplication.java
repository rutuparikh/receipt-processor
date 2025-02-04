package com.rewards.receiptprocessor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@SpringBootApplication
public class ReceiptProcessorApplication {
	
	private static final Logger log = LogManager.getLogger(ReceiptProcessorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ReceiptProcessorApplication.class, args);
		log.info("Hello from Receipt Processor Application");
	}

}
