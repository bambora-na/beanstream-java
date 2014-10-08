package com.beanstream.api;

import java.util.List;

import com.beanstream.Configuration;
import com.beanstream.Gateway;
import com.beanstream.connection.BeanstreamUrls;
import com.beanstream.connection.HttpMethod;
import com.beanstream.connection.HttpsConnector;
import com.beanstream.domain.Address;
import com.beanstream.domain.Card;
import com.beanstream.domain.CustomFields;
import com.beanstream.domain.PaymentProfile;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.ProfileRequest;
import com.beanstream.responses.ProfileCardsResponse;
import com.beanstream.responses.ProfileResponse;
import com.google.gson.Gson;

public class ProfilesAPI {

	private Configuration config;
	private HttpsConnector connector;
	private final Gson gson = new Gson();

	public ProfilesAPI(Configuration config) {
		this.config = config;
		connector = new HttpsConnector(config.getMerchantId(),
				config.getProfilesApiPasscode());
	}

	public void setConfig(Configuration config) {
		this.config = config;
		connector = new HttpsConnector(config.getMerchantId(),
				config.getProfilesApiPasscode());
	}

	public ProfileResponse createProfile(Card card, Address billing)
			throws BeanstreamApiException {
		ProfileRequest request = new ProfileRequest();
		request.setBilling(billing);
		request.setCard(card);
		return createProfile(request);
	}

	public ProfileResponse createProfile(Card card, Address billing,
			CustomFields customs, String language, String comments)
			throws BeanstreamApiException {
		ProfileRequest request = new ProfileRequest();
		request.setBilling(billing);
		request.setCard(card);
		request.setLanguage(language);
		request.setComments(comments);
		request.setCustom(customs);
		return createProfile(request);
	}

	public ProfileResponse createProfile(ProfileRequest profileRequest)
			throws BeanstreamApiException {

		validateProfileRequest(profileRequest);

		String url = BeanstreamUrls.getProfilesUrl(config.getPlatform(),
				config.getVersion());

		String response = connector.ProcessTransaction(HttpMethod.post, url,
				profileRequest);
		return gson.fromJson(response, ProfileResponse.class);

	}

	public PaymentProfile getProfileById(String profileId)
			throws BeanstreamApiException {
		validateProfileId(profileId);
		String url = BeanstreamUrls.getProfilesUrl(config.getPlatform(),
				config.getVersion(), profileId);

		String response = connector.ProcessTransaction(HttpMethod.post, url,
				null);
		return gson.fromJson(response, PaymentProfile.class);

	}

	public List<Card> getCardsByProfileId(String profileId)
			throws BeanstreamApiException {
		validateProfileId(profileId);
		String url = BeanstreamUrls.getProfileCardsUrl(config.getPlatform(),
				config.getVersion(), profileId);

		String response = connector.ProcessTransaction(HttpMethod.post, url,
				null);
		ProfileCardsResponse pcr = gson.fromJson(response,
				ProfileCardsResponse.class);
		return pcr.getCards();

	}

	public Card getCard(String profileId, String cardId)
			throws BeanstreamApiException {

		validateProfileId(profileId);
		String url = BeanstreamUrls.getProfileCardUrl(config.getPlatform(),
				config.getVersion(), profileId, cardId);

		String response = connector.ProcessTransaction(HttpMethod.post, url,
				null);
		ProfileCardsResponse pcr = gson.fromJson(response,
				ProfileCardsResponse.class);
		Card card = null;
		if (pcr.getCards().isEmpty()) {
			card = null;
		} else {
			card = pcr.getCards().get(0);
			card.setId(cardId);
		}
		return card;

	}

	public ProfileResponse updateProfileCard(String profileId, Card card)
			throws BeanstreamApiException {
		validateProfileId(profileId);
		String cardId = card.getId();
		String url = BeanstreamUrls.getProfileCardUrl(config.getPlatform(),
				config.getVersion(), profileId, cardId);

		validateCard(card);
		// send the card json without id
		card.setId(null);
		String cardJson = gson.toJson(card, Card.class);
		// don't modify the user object because they might re use it
		card.setId(cardId);
		String response = connector.ProcessTransaction(HttpMethod.post, url,
				cardJson);
		return gson.fromJson(response, ProfileResponse.class);

	}

	public ProfileResponse addProfileCard(String profileId, Card card)
			throws BeanstreamApiException {
		validateProfileId(profileId);
		String url = BeanstreamUrls.getProfileCardsUrl(config.getPlatform(),
				config.getVersion(), profileId);

		validateCard(card);
		String response = connector.ProcessTransaction(HttpMethod.post, url,
				card);
		return gson.fromJson(response, ProfileResponse.class);

	}

	public ProfileResponse deleteProfileById(String profileId)
			throws BeanstreamApiException {

		validateProfileId(profileId);
		String url = BeanstreamUrls.getProfilesUrl(config.getPlatform(),
				config.getVersion(), profileId);

		String response = connector.ProcessTransaction(HttpMethod.delete, url,
				null);
		return gson.fromJson(response, ProfileResponse.class);

	}

	public ProfileResponse updateProfile(PaymentProfile profile)
			throws BeanstreamApiException {
		Gateway.assertNotNull(profile, "profile to update is null");
		validateProfileId(profile.getId());

		// validateCard(profile.getCard());
		validateBillingAddress(profile.getBilling());

		String url = BeanstreamUrls.getProfilesUrl(config.getPlatform(),
				config.getVersion(), profile.getId());
		// don't send the card in the update request only
		Card card = profile.getCard();
		profile.setCard(null);
		String profileJson = gson.toJson(profile);
		profile.setCard(card);
		String response = connector.ProcessTransaction(HttpMethod.delete, url,
				profileJson);
		return gson.fromJson(response, ProfileResponse.class);
	}

	private void validateProfileId(String profileId)
			throws BeanstreamApiException {
		Gateway.assertNotEmpty(profileId,
				"profile id is not valid because is empty");
	}

	private void validateCard(Card card) throws BeanstreamApiException {
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

	private void validateBillingAddress(Address billing)
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
	private void validateProfileRequest(ProfileRequest profileRequest)
			throws BeanstreamApiException {
		Gateway.assertNotNull(profileRequest, "profile request object is null");
		Card card = profileRequest.getCard();
		Address billing = profileRequest.getBilling();
		validateCard(card);
		validateBillingAddress(billing);
	}
}
