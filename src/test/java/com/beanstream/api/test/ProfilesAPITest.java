package com.beanstream.api.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.beanstream.connection.HttpsConnector;
import com.beanstream.domain.Address;
import com.beanstream.domain.Card;
import com.beanstream.domain.PaymentProfile;
import com.beanstream.domain.Token;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.responses.LegatoTokenResponse;
import com.beanstream.responses.ProfileResponse;

public class ProfilesAPITest extends BasePaymentsTest {

	@Test
	public void invalidCardCreateProfile() {
		Address billing = getTestBillingAddress();
		Card card = getTestCard();
		ProfileResponse createdProfile = null;

		try {
			Card nillCard = null;
			createdProfile = gateway.profiles().createProfile(nillCard, billing);
			Assert.fail("Fail test because the card was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			card.setName(null);
			createdProfile = gateway.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the card name was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			card.setName("Jhon Garcia");
			card.setNumber("");
			createdProfile = gateway.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the card number was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			card.setNumber("5100000010001004");
			card.setExpiryYear("");
			createdProfile = gateway.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the card expiry year was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			card.setExpiryYear("2018");
			card.setExpiryMonth(null);
			createdProfile = gateway.profiles().createProfile(card, billing);
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
			createdProfile = gateway.profiles().createProfile(card, null);
			Assert.fail("Fail test because the billing address was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			billing.setAddressLine1(null);
			createdProfile = gateway.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the billing address line 1 was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			billing.setAddressLine1("12635 NW 98TH ST");
			billing.setCity(null);
			createdProfile = gateway.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the billing address city was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			billing.setCity("Miami");
			billing.setCountry(null);
			createdProfile = gateway.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the billing address country was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			billing.setCountry("US");
			billing.setEmailAddress(null);
			createdProfile = gateway.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the billing address email was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			billing.setEmailAddress("pagarciaortega@gmail.com");
			billing.setPhoneNumber(null);
			createdProfile = gateway.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the billing address phone number was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

		try {
			billing.setPhoneNumber("786-241-8879");
			billing.setProvince(null);
			createdProfile = gateway.profiles().createProfile(card, billing);
			Assert.fail("Fail test because the billing address province/state was empty");
		} catch (BeanstreamApiException ex) {
			Assert.assertTrue("", ex.getHttpStatusCode() == 400);
		}

	}

	@Test
	public void testProfileCrudUsingCard() throws BeanstreamApiException {
		String profileId = null;
		try {
			Address billing = getTestCardValidAddress();
			Card card = getTestCard();
			// test create profile
			ProfileResponse createdProfile = gateway.profiles()
					.createProfile(card, billing);
			profileId = createdProfile.getId();
			Assert.assertNotNull(
					"Test failed because it should create the profile and return a valid id",
					profileId);

			// test get profile by id
			PaymentProfile paymentProfile = gateway.profiles()
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
			gateway.profiles().updateProfile(paymentProfile);

			// refresh the updated profile
			paymentProfile = gateway.profiles().getProfileById(profileId);
			
			Assert.assertEquals("Language was updated to Francais",
					paymentProfile.getLanguage(), "fr");
			
			// delete the payment profile
			gateway.profiles().deleteProfileById(profileId);
			try {
				gateway.profiles().getProfileById(profileId);
				Assert.fail("This profile was deleted, therefore should throw an exception");
			} catch (BeanstreamApiException e) {
				profileId = null;
			}
		} catch (BeanstreamApiException ex) {
			Assert.fail("Test can not continue, "+ex.getMessage());
		} catch (Exception ex) {
			Assert.fail("unexpected exception occur, test can not continue");
		} finally {
			if (profileId != null) {
				ProfileResponse response = gateway.profiles()
						.deleteProfileById(profileId);
			}
		}

	}

	
	@Test
	public void testProfileCrudUsingToken() throws BeanstreamApiException {
		String profileId = null;
		try {
			HttpsConnector connector = new HttpsConnector(300200578,
					"4BaD82D9197b4cc4b70a221911eE9f70");
			
			Address billing = getTestCardValidAddress();
			LegatoTokenResponse tokenResponse = tokenizeCard(connector, "5100000010001004", "123",12, 19);
			// test create profile
			Token token = new Token("John Doe",tokenResponse.getToken());
			
			ProfileResponse createdProfile = gateway.profiles()
					.createProfile(token, billing);
			profileId = createdProfile.getId();
			Assert.assertNotNull(
					"Test failed because it should create the profile and return a valid id",
					profileId);

			// test get profile by id
			PaymentProfile paymentProfile = gateway.profiles()
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
			gateway.profiles().updateProfile(paymentProfile);

			// refresh the updated profile
			paymentProfile = gateway.profiles().getProfileById(profileId);
			
			Assert.assertEquals("Language was updated to Francais",
					paymentProfile.getLanguage(), "fr");
			
			// delete the payment profile
			gateway.profiles().deleteProfileById(profileId);
			try {
				gateway.profiles().getProfileById(profileId);
				Assert.fail("This profile was deleted, therefore should throw an exception");
			} catch (BeanstreamApiException e) {
				profileId = null;
			}
		} catch (BeanstreamApiException ex) {
			Assert.fail("Test can not continue, "+ex.getMessage());
		} catch (Exception ex) {
			Assert.fail("unexpected exception occur, test can not continue");
		} finally {
			if (profileId != null) {
				ProfileResponse response = gateway.profiles()
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
			ProfileResponse createdProfile = gateway.profiles()
					.createProfile(card, billing);
			profileId = createdProfile.getId();
			Assert.assertNotNull(
					"Test failed because it should create the profile and return a valid id",
					profileId);

			// test getCards
			List<Card> profileCards = gateway.profiles().getCards(profileId);
			Assert.assertFalse("this profile should have one credit card",
					profileCards.isEmpty());
			Card card1 = profileCards.get(0);

			// test getCard
			Card freshCard = gateway.profiles().getCard(profileId,
					card1.getId());
			Assert.assertNotNull(
					"Test failed because it should return a valid card",
					freshCard);
			// add a new card
			Card newCard = getTestCard();
			newCard.setCvd("123");
			newCard.setName("John Doe");
			newCard.setNumber("4030000010001234");
			newCard.setExpiryMonth("01");
			newCard.setExpiryYear("19");
			
			
			ProfileResponse newCardResp = gateway.profiles().addCard(profileId, newCard);
			
			
			// update the card expires date
			freshCard.setExpiryMonth("01");
			freshCard.setExpiryYear("19");
                        freshCard.setName("Bob Two");
			
			
			ProfileResponse profileResponse = gateway.profiles().updateCard(profileId, freshCard);
			//2659 Douglas Street	V8T 4M3
	
//			Address newCardAddr = getAddress("Pedro Garcia", "VICTORIA", "BC", "CA", "2659 Douglas Street", "V8T4M3", "TEST@BEANSTREAM.COM", "12501234567");
			freshCard = gateway.profiles().getCard(profileId,
					freshCard.getId());
			Assert.assertEquals("the Expiry Month was updated but the change is not reflected", "01",freshCard.getExpiryMonth());
			Assert.assertEquals("the Expiry Year was updated but the change is not reflected", "19",freshCard.getExpiryYear());
		} catch (BeanstreamApiException ex) {
			Assert.fail(ex.getMessage());
		} catch (Exception ex) {
			Assert.fail("unexpected exception occur, test can not continue : "
					+ ex.getMessage());
		} finally {
			if (profileId != null) {
				ProfileResponse response = gateway.profiles()
						.deleteProfileById(profileId);
			}
		}
	}

}
