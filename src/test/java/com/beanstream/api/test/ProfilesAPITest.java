package com.beanstream.api.test;

import org.junit.Assert;
import org.junit.Test;

import com.beanstream.domain.Address;
import com.beanstream.domain.Card;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.responses.ProfileResponse;

public class ProfilesAPITest extends BaseBeanstreamTest {

	@Test
	public void invalidCardCreateProfile() {
		Address billing = getTestBillingAddress();
		Card card = getTestCard();
		ProfileResponse createdProfile = null;

		try {
			createdProfile = beanstream.profiles().createProfile(null, billing);
			Assert.fail("Fail test because the card was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			card.setName(null);
			createdProfile = beanstream.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the card name was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			card.setName("Jhon Garcia");
			card.setNumber("");
			createdProfile = beanstream.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the card number was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			card.setNumber("5100000010001004");
			card.setExpiryYear("");
			createdProfile = beanstream.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the card expiry year was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			card.setExpiryYear("2018");
			card.setExpiryMonth(null);
			createdProfile = beanstream.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the card expiry month was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}
	}

	@Test
	public void invalidBillingAddrCreateProfile() {
		Address billing = getTestBillingAddress();
		Card card = getTestCard();
		ProfileResponse createdProfile = null;

		try {
			createdProfile = beanstream.profiles().createProfile(card, null);
			Assert.fail("Fail test because the billing address was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			billing.setAddressLine1(null);
			createdProfile = beanstream.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the billing address line 1 was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			billing.setAddressLine1("12635 NW 98TH ST");
			billing.setCity(null);
			createdProfile = beanstream.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the billing address city was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			billing.setCity("Miami");
			billing.setCountry(null);
			createdProfile = beanstream.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the billing address country was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			billing.setCountry("US");
			billing.setEmailAddress(null);
			createdProfile = beanstream.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the billing address email was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			billing.setEmailAddress("pagarciaortega@gmail.com");
			billing.setPhoneNumber(null);
			createdProfile = beanstream.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the billing address phone number was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			billing.setPhoneNumber("786-241-8879");
			billing.setProvince(null);
			createdProfile = beanstream.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the billing address province/state was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

	}

	@Test
	public void createProfile() throws BeanstreamApiException {
		Address billing = getTestBillingAddress();
		Card card = getTestCard();
		ProfileResponse createdProfile = beanstream.profiles().createProfile(card, billing);
		Assert.assertNotNull("Test failed because it should create the profile and return a valid id",createdProfile.getId());
		

	}

}
