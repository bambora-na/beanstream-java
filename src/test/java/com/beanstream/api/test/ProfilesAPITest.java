package com.beanstream.api.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.beanstream.domain.Address;
import com.beanstream.domain.Card;
import com.beanstream.domain.PaymentProfile;
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
	public void testProfileCrud() throws BeanstreamApiException {
		String profileId = null;
		try {
			Address billing = getTestCardValidAddress();
			Card card = getTestCard();
			// test create profile
			ProfileResponse createdProfile = beanstream.profiles()
					.createProfile(card, billing);
			profileId = createdProfile.getId();
			Assert.assertNotNull(
					"Test failed because it should create the profile and return a valid id",
					profileId);

			// test get profile by id
			PaymentProfile paymentProfile = beanstream.profiles()
					.getProfileById(profileId);
			Assert.assertEquals(
					"billing address assinged does not matches with the one sent at creation time",
					paymentProfile.getBilling(), billing);
			Assert.assertNotNull("Credit card was not in the response",
					paymentProfile.getCard());
			Assert.assertTrue("The default lenguage should be english","en".equals(paymentProfile.getLanguage()));
			
			// update the profile to francais
			paymentProfile.setLanguage("fr");
			paymentProfile.setComments("test updating profile sending billing info only");
			// update profile
			beanstream.profiles().updateProfile(paymentProfile);

			// refresh the updated profile
			paymentProfile = beanstream.profiles().getProfileById(profileId);
			
			Assert.assertEquals("Language was updated to Francais",
					paymentProfile.getLanguage(), "fr");
			
			// delete the payment profile
			beanstream.profiles().deleteProfileById(profileId);
			try {
				beanstream.profiles().getProfileById(profileId);
				Assert.fail("This profile was deleted, therefore should throw an exception");
			} catch (BeanstreamApiException e) {
				profileId = null;
			}

		} catch (Exception ex) {
			Assert.fail("unexpected exception occur, test can not continue : "
					+ ex.getMessage());
		} finally {
			if (profileId != null) {
				ProfileResponse response = beanstream.profiles()
						.deleteProfileById(profileId);
			}
		}

	}

	@Test
	public void testProfileCardsCrud() throws BeanstreamApiException {
		String profileId = null;
		try {
			Address billing = getTestBillingAddress();
			Card card = getTestCard();
			// test create profile
			ProfileResponse createdProfile = beanstream.profiles()
					.createProfile(card, billing);
			profileId = createdProfile.getId();
			Assert.assertNotNull(
					"Test failed because it should create the profile and return a valid id",
					profileId);

			// test getCards
			List<Card> profileCards = beanstream.profiles().getCards(profileId);
			Assert.assertFalse("this profile should have one credit card",
					profileCards.isEmpty());
			Card card1 = profileCards.get(0);

			// test getCard
			Card freshCard = beanstream.profiles().getCard(profileId,
					card1.getId());
			Assert.assertNotNull(
					"Test failed because it should return a valid card",
					freshCard);
			// update the card expires date
			freshCard.setExpiryMonth("01");
			freshCard.setExpiryYear("2030");
			freshCard.setName("John Doe");
			
			ProfileResponse profileResponse = beanstream.profiles().updateCard(profileId, freshCard);
			
			freshCard = beanstream.profiles().getCard(profileId,
					freshCard.getId());
			Assert.assertEquals("the Expiry Month was updated but the change is not reflected", "01",freshCard.getExpiryMonth());
			Assert.assertEquals("the Expiry Year was updated but the change is not reflected", "2030",freshCard.getExpiryYear());
			
		} catch (Exception ex) {
			Assert.fail("unexpected exception occur, test can not continue : "
					+ ex.getMessage());
		} finally {
			if (profileId != null) {
				ProfileResponse response = beanstream.profiles()
						.deleteProfileById(profileId);
			}
		}
	}

}
