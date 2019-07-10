package com.beanstream.util;

import org.apache.http.HttpStatus;

import com.beanstream.Gateway;
import com.beanstream.domain.Address;
import com.beanstream.domain.Card;
import com.beanstream.domain.Token;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.ProfileRequest;
import com.beanstream.responses.BeanstreamResponse;

public class ProfilesUtils {

	public static void validateProfileId(String profileId)
			throws BeanstreamApiException {
		Gateway.assertNotEmpty(profileId,
				"profile id is not valid because is empty");
	}

	public static void validateCard(Card card) throws BeanstreamApiException {
		Gateway.assertNotNull(card,
				"profile request is not valid because the card object is null");
		Gateway.assertNotEmpty(card.getName(),
				"profile request is not valid because the name on card is empty");
		Gateway.assertNotEmpty(card.getNumber(),
				"profile request is not valid because the card number is empty");
		Gateway.assertNotEmpty(card.getExpiryMonth(),
				"profile request is not valid because the card expiry month is empty");
		Gateway.assertNotEmpty(card.getExpiryYear(),
				"profile request is not valid because the card expiry year is empty");
	}

	public static void validateToken(Token token) throws BeanstreamApiException {
		Gateway.assertNotNull(token,
				"profile request is not valid because the token object is null");
		Gateway.assertNotEmpty(token.getName(),
				"profile request is not valid because the name on card (token name) is empty");
		Gateway.assertNotEmpty(token.getCode(),
				"profile request is not valid because the token code is empty");
	}

	public static void validateBillingAddr(Address billing)
			throws BeanstreamApiException {
		Gateway.assertNotNull(billing,
				"profile request is not valid because the billing address object is null");
		Gateway.assertNotEmpty(billing.getName(),
				"profile request is not valid because the billing address name is empty");
		Gateway.assertNotEmpty(billing.getEmailAddress(),
				"profile request is not valid because the billing address email is empty");
		Gateway.assertNotEmpty(
				billing.getPhoneNumber(),
				"profile request is not valid because the billing address phone number is empty");
		Gateway.assertNotEmpty(billing.getAddressLine1(),
				"profile request is not valid because the billing address line1 is empty");

		Gateway.assertNotEmpty(billing.getCity(),
				"profile request is not valid because the billing address city is empty");
		Gateway.assertNotEmpty(
				billing.getProvince(),
				"profile request is not valid because the billing address province/state is empty");
		Gateway.assertNotEmpty(billing.getCountry(),
				"profile request is not valid because the billing address country is empty");
	}

	/**
	 * Validate all required properties in the profile request are valid Throws
	 * a BeanstreamApiException with a bad request status, if any required
	 * property is missing
	 */
	public static void validateProfileReq(ProfileRequest profileRequest, boolean validateBillingAddress)
			throws BeanstreamApiException {
		Gateway.assertNotNull(profileRequest, "profile request object is null");
		Card card = profileRequest.getCard();
		Address billing = profileRequest.getBilling();
		Token token = profileRequest.getToken();
		
		if (card == null && token == null) {
			BeanstreamResponse response = BeanstreamResponse.fromMessage("invalid create request, both token and card objects are null");
            throw BeanstreamApiException.getMappedException(HttpStatus.SC_BAD_REQUEST, response);
		}
		
		if (token == null) {
			validateCard(card);
		}
		
		if (card == null) {
			validateToken(token);
		}

		if (validateBillingAddress){
			validateBillingAddr(billing);	
		}
	}
}
