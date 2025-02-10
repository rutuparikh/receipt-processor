package com.rewards.receiptprocessor.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application.properties")
@TestPropertySource("classpath:application.properties")
@Import(Properties.class)
public class ReceiptProcessorServiceTests {

	@InjectMocks
	ReceiptProcessorService receiptProcessorService;

	@Mock
	RedisTemplate<String, Object> redisTemplate;

	@Mock
	HashOperations<String, String, Object> hashOperations;

	@BeforeEach
	public void setup() {

		when(redisTemplate.<String, Object>opsForHash()).thenReturn(hashOperations);
	}

	@Test
	@DisplayName("testIsIdUniqueReturnsTrue")
	void testIsIdUniqueReturnsTrue() {

		String id = "a4a214d2-17ad-48ec-8563-0e5fb0549f21";
		String key = "points";
		String value = "34";

		when(hashOperations.entries(id)).thenReturn(Map.of(key, value));
		when(redisTemplate.hasKey(id)).thenReturn(false);

		String result = receiptProcessorService.getDummyJsonDataFromHash(id, key);

		assertNotNull(result);

		boolean isUnique = receiptProcessorService.isIdUnique(id);

		assertTrue(isUnique);
		verify(redisTemplate).hasKey(id);
		System.out.println("verified");
	}

	@Test
	@DisplayName("testIsIdUniqueReturnsFalse")
    void testIsIdUniqueReturnsFalse() {
		
		String id = "a4a214d2-17ad-48ec-8563-0e5fb0549f21";
		String key = "points";
		String value = "34";

		when(hashOperations.entries(id)).thenReturn(Map.of(key, value));
		when(redisTemplate.hasKey(id)).thenReturn(true);

		String result = receiptProcessorService.getDummyJsonDataFromHash(id, key);

		assertNotNull(result);

		boolean isUnique = receiptProcessorService.isIdUnique(id);

		assertFalse(isUnique);
		
		verify(redisTemplate).hasKey(id);
		System.out.println("verified");
    }

	@Test
	@DisplayName("testSavePoints")
	void testSavePoints() {

		String id = "a4a214d2-17ad-48ec-8563-0e5fb0549f21";
		String key = "points";
		String value = "34";

		when(hashOperations.entries(id)).thenReturn(Map.of(key, value));

		String result = receiptProcessorService.getDummyJsonDataFromHash(id, key);

		assertNotNull(result);
		assertEquals(value, result);

		verify(redisTemplate).opsForHash();
		verify(hashOperations).entries(id);

	}

}
