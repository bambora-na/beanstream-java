package com.beanstream.api.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.beanstream.Gateway;
import com.beanstream.domain.Address;
import com.beanstream.domain.Card;
import com.beanstream.requests.CardPaymentRequest;

public abstract class BaseBeanstreamTest {
	protected AtomicInteger sequence = new AtomicInteger(1);
	Gateway beanstream = new Gateway("v1", 300200578,
			"4BaD82D9197b4cc4b70a221911eE9f70", // payments API passcode
            "D97D3BE1EE964A6193D17A571D9FBC80", // profiles API passcode
            "4e6Ff318bee64EA391609de89aD4CF5d");// reporting API passcode
	
	protected String getRandomOrderId(String prefix) {
		String orderId = null;
		Date date = new Date();
		StringBuilder sb = new StringBuilder();
		if (prefix != null) {
			sb.append(prefix);
			sb.append("_");
		}
		SimpleDateFormat df = new SimpleDateFormat("MMkkmmssSSSS");
		sb.append(sequence.getAndIncrement());
		sb.append("_");
		sb.append(df.format(date));
		orderId = sb.toString();
		if (orderId.length() > 30) {
			orderId = orderId.substring(0, 29);
		}
		return orderId;
	}
	
	protected Address getTestCardValidAddress(){
		Address billing = new Address();
		billing.setName("JANE DOE");
		billing.setCity("VICTORIA");
		billing.setProvince("BC");
		billing.setCountry("CA");
		billing.setAddressLine1("123 FAKE ST.");
		billing.setPostalCode("V9T2G6");
		billing.setEmailAddress("TEST@BEANSTREAM.COM");
		billing.setPhoneNumber("12501234567");
		return billing;
	}
	protected Address getTestShippingAddress(){
		return getAddress("Shipping","Miami","FL","US","2564 NW 10TH AVE","33689","client@domain.com","789-325-4565");
		
	}
	protected Address getTestBillingAddress(){
		return getAddress("Billing","Miami","FL","US","3524 NW 72TH ST","33601","client@domain.com","305-325-4565");
		
	}
	
	protected Address getAddress(String name, String city, String state, String country, String addressLine1, String postalCode, String email, String phoneNumber){
		Address addr = new Address();
		addr.setName(name);
		addr.setCity(city);
		addr.setProvince(state);
		addr.setCountry(country);
		addr.setAddressLine1(addressLine1);
		addr.setPostalCode(postalCode);
		addr.setEmailAddress(email);
		addr.setPhoneNumber(phoneNumber);
		return addr;
		
	}

	protected CardPaymentRequest getCreditCardPaymentRequest(String orderId,
			String merchantId, String amount) {
		CardPaymentRequest paymentRequest = new CardPaymentRequest();
		paymentRequest.setAmount(amount);
		paymentRequest.setMerchantId(merchantId);
		paymentRequest.setOrderNumber(orderId);
		paymentRequest.getCard().setName("John Doe")
				.setNumber("5100000010001004").setExpiryMonth("12")
				.setExpiryYear("18").setCvd("123");
		return paymentRequest;
	}
	
	protected Card getTestCard(){
		return new Card().setName("John Doe")
				.setNumber("5100000010001004").setExpiryMonth("12")
				.setExpiryYear("18").setCvd("123");
	}
}
