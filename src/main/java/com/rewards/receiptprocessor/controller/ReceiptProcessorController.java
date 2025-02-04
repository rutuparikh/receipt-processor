package com.rewards.receiptprocessor.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rewards.receiptprocessor.model.ProcessReceiptRequest;
import com.rewards.receiptprocessor.model.Receipt;
import com.rewards.receiptprocessor.model.ReceiptId;
import com.rewards.receiptprocessor.services.ReceiptProcessorService;

@RestController
@RequestMapping("/receipts")
public class ReceiptProcessorController {
	
	private static final Logger log = LogManager.getLogger(ReceiptProcessorController.class);
	
	@Autowired
    private ReceiptProcessorService receiptProcessor;
	
	@GetMapping("/")
	public String index() {
		log.info("Home Page");
		return "Hello from Receipt Processor!";
	}
	
	@PostMapping(value = "/process", consumes = "application/json", produces = "application/json")
	public ReceiptId processReceipt(@RequestBody ProcessReceiptRequest request) {
		ReceiptId idResponse = receiptProcessor.processReceipt(request);
		System.out.println();
		return idResponse;
	}
	
//	@PostMapping("/save")
//    public String saveValue(@RequestParam(required = true) String key, @RequestParam(required = true) String value) {
//		receiptProcessor.saveValue(key, value);
//        return "Value saved";
//    }
//
//    @GetMapping("/get")
//    public String getValue(@RequestParam String key) {
//        Object value = receiptProcessor.getValue(key);
//        return "Value: " + value;
//    }

}
