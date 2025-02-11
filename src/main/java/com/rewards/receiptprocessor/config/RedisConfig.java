package com.rewards.receiptprocessor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import com.rewards.receiptprocessor.exceptions.ReceiptProcessorException;

@Configuration
public class RedisConfig {
	
	@Value("${spring.redis.host}")
	private String hostname;
	
	@Value("${spring.redis.port}")
	private String port;
	
	@Bean
    public RedisConnectionFactory redisConnectionFactory() {
		
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
        
        lettuceConnectionFactory.setHostName(hostname);
        lettuceConnectionFactory.setPort(Integer.parseInt(port));
        lettuceConnectionFactory.setTimeout(5000);
        
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) throws ReceiptProcessorException{
        
    	RedisTemplate<String, Object> template = new RedisTemplate<>();
        
    	template.setConnectionFactory(connectionFactory);
        
        return template;
    }
   
}