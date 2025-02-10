package com.rewards.receiptprocessor.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application.properties")
@TestPropertySource("classpath:application.properties")
@Import(Properties.class)
public class ValidatorTests {
	
	@InjectMocks
	private Validator validator;
	
	@BeforeEach
	public void setup() {
		ReflectionTestUtils.setField(validator, "dateFormat", "yyyy-MM-dd");
		ReflectionTestUtils.setField(validator, "timeFormat", "HH:mm");
		ReflectionTestUtils.setField(validator, "retailerRegex", "^[\\w\\s\\-&]+$");
		ReflectionTestUtils.setField(validator, "descriptionRegex", "^[\\w\\s\\-]+$");
		ReflectionTestUtils.setField(validator, "amountRegex", "^\\d+(\\.\\d{2})?$");
	}

	@Test
	@DisplayName("testIsValidDateFormat1ReturnsTrue")
	public void testIsValidDateFormat1ReturnsTrue() {
		
		assertTrue(validator.isValidDate("2023-10-03"));
	}
	
	@Test
	@DisplayName("testIsValidDateFormat2ReturnsFalse")
	public void testIsValidDateFormat2ReturnsFalse() {
		
		assertFalse(validator.isValidDate("2023-30-03"));
	}
	
	@Test
	@DisplayName("testIsValidDateFormat3ReturnsFalse")
	public void testIsValidDateFormat3ReturnsFalse() {
		
		assertFalse(validator.isValidDate("30-03-2023"));
	}
	
	@Test
	@DisplayName("testIsValidTimeFormat1ReturnsTrue")
	public void testIsValidTimeFormat1ReturnsTrue() {
		
		assertTrue(validator.isValidTime("23:20"));
	}
	
	@Test
	@DisplayName("testIsValidTimeFormat1ReturnsFalse")
	public void testIsValidTimeFormat1ReturnsFalse() {
		
		assertFalse(validator.isValidTime("23:20:40"));
	}
	
	@Test
	@DisplayName("testGetAlphaNumericCount")
	public void testGetAlphaNumericCount() {
		
		int expected = 11;
		
		int actual = validator.getAlphaNumericCount("Target 1&2new");
		
		assertNotNull(actual);
		assertEquals(actual, expected);
		
	}
	
	@Test
	@DisplayName("testIsValidRetailerTestCase1")
	public void testIsValidRetailerTestCase1() {
		
		assertTrue(validator.isValidRetailer("Target"));
	}
	
	@Test
	@DisplayName("testIsValidRetailerTestCase2")
	public void testIsValidRetailerTestCase2() {
		
		assertTrue(validator.isValidRetailer("M&M Corner Market"));
	}
	
	@Test
	@DisplayName("testIsValidRetailerTestCase3")
	public void testIsValidRetailerTestCase3() {
		
		assertTrue(validator.isValidRetailer("M&M Corner Market-new"));
	}
	
	@Test
	@DisplayName("testIsValidRetailerTestCase4")
	public void testIsValidRetailerTestCase4() {
		
		assertFalse(validator.isValidRetailer("Target's store"));
	}
	
	@Test
	@DisplayName("testIsValidDescriptionTestCase1")
	public void testIsValidDescriptionTestCase1() {
		
		assertTrue(validator.isValidDescription("Gatorade"));
	}
	
	@Test
	@DisplayName("testIsValidDescriptionTestCase2")
	public void testIsValidDescriptionTestCase2() {
		
		assertTrue(validator.isValidDescription("Mountain Dew 12PK"));
	}
	
	@Test
	@DisplayName("testIsValidDescriptionTestCase3")
	public void testIsValidDescriptionTestCase3() {
		
		assertFalse(validator.isValidDescription("M&M"));
	}
	
	@Test
	@DisplayName("testIsValidDescriptionTestCase4")
	public void testIsValidDescriptionTestCase4() {
		
		assertFalse(validator.isValidDescription("Emil's Cheese Pizza"));
	}
	
	@Test
	@DisplayName("testIsValidDescriptionTestCase5")
	public void testIsValidDescriptionTestCase5() {
		
		assertTrue(validator.isValidDescription("Emils Cheese Pizza-new"));
	}
	
	@Test
	@DisplayName("testIsValidAmountTestCase1")
	public void testIsValidAmountTestCase1() {
		
		assertTrue(validator.isValidAmount("10.00"));
	}
	
	@Test
	@DisplayName("testIsValidAmountTestCase2")
	public void testIsValidAmountTestCase2() {
		
		assertTrue(validator.isValidAmount("10.23"));
	}
	
	@Test
	@DisplayName("testIsValidAmountTestCase3")
	public void testIsValidAmountTestCase3() {
		
		assertFalse(validator.isValidAmount("10.003"));
	}
	
	@Test
	@DisplayName("testIsValidAmountTestCase4")
	public void testIsValidAmountTestCase4() {
		
		assertFalse(validator.isValidAmount("10.1"));
	}
}
