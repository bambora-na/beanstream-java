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
import com.beanstream.util.ProfilesUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
		ProfileRequest request = new ProfileRequest().billing(billing).card(
				card);
		return createProfile(request);
	}

	public ProfileResponse createProfile(Card card, Address billing,
			CustomFields custom, String language, String comments)
			throws BeanstreamApiException {
		ProfileRequest request = new ProfileRequest().card(card)
				.billing(billing).language(language).comments(comments)
				.custom(custom);
		return createProfile(request);
	}

	public ProfileResponse createProfile(ProfileRequest profileRequest)
			throws BeanstreamApiException {

		ProfilesUtils.validateProfileRequest(profileRequest);

		String url = BeanstreamUrls.getProfilesUrl(config.getPlatform(),
				config.getVersion());

		String response = connector.ProcessTransaction(HttpMethod.post, url,
				profileRequest);
		return gson.fromJson(response, ProfileResponse.class);

	}

	public PaymentProfile getProfileById(String profileId)
			throws BeanstreamApiException {
		ProfilesUtils.validateProfileId(profileId);
		String url = BeanstreamUrls.getProfilesUrl(config.getPlatform(),
				config.getVersion(), profileId);

		String response = connector.ProcessTransaction(HttpMethod.get, url,
				null);
		return gson.fromJson(response, PaymentProfile.class);

	}

	public ProfileResponse deleteProfileById(String profileId)
			throws BeanstreamApiException {

		ProfilesUtils.validateProfileId(profileId);
		String url = BeanstreamUrls.getProfilesUrl(config.getPlatform(),
				config.getVersion(), profileId);

		String response = connector.ProcessTransaction(HttpMethod.delete, url,
				null);
		return gson.fromJson(response, ProfileResponse.class);

	}

	public ProfileResponse updateProfile(PaymentProfile profile)
			throws BeanstreamApiException {
		Gateway.assertNotNull(profile, "profile to update is null");
		ProfilesUtils.validateProfileId(profile.getId());

		// validateCard(profile.getCard());
		ProfilesUtils.validateBillingAddress(profile.getBilling());

		String url = BeanstreamUrls.getProfilesUrl(config.getPlatform(),
				config.getVersion(), profile.getId());
		
		JsonObject req = new JsonObject();
		req.add("billing", gson.toJsonTree(profile.getBilling(),Address.class));
		req.add("custom", gson.toJsonTree(profile.getCustom(),CustomFields.class));
		req.addProperty("language",profile.getLanguage());
		req.addProperty("comments",profile.getComments());
		String response = connector.ProcessTransaction(HttpMethod.put, url,
				req);
		return gson.fromJson(response, ProfileResponse.class);
	}

	public List<Card> getCards(String profileId) throws BeanstreamApiException {
		ProfilesUtils.validateProfileId(profileId);
		String url = BeanstreamUrls.getProfileCardsUrl(config.getPlatform(),
				config.getVersion(), profileId);

		String response = connector.ProcessTransaction(HttpMethod.get, url,
				null);
		ProfileCardsResponse pcr = gson.fromJson(response,
				ProfileCardsResponse.class);
		return pcr.getCards();

	}

	public Card getCard(String profileId, String cardId)
			throws BeanstreamApiException {

		ProfilesUtils.validateProfileId(profileId);
		Gateway.assertNotEmpty(cardId, "card id is empty");
		String url = BeanstreamUrls.getProfileCardUrl(config.getPlatform(),
				config.getVersion(), profileId, cardId);

		String response = connector.ProcessTransaction(HttpMethod.get, url,
				null);
		ProfileCardsResponse pcr = gson.fromJson(response,
				ProfileCardsResponse.class);
		Card card = null;
		if (pcr.getCards().isEmpty()) {
			card = null;
		} else {
			card = pcr.getCards().get(0);
//			card.setId(cardId);
		}
		return card;

	}

	public ProfileResponse updateCard(String profileId, Card card)
			throws BeanstreamApiException {
		
		ProfilesUtils.validateProfileId(profileId);
		Gateway.assertNotNull(card, "card it to to update is empty");
		String cardId = card.getId();
		Gateway.assertNotEmpty(cardId, "card id it to update is empty");
		String url = BeanstreamUrls.getProfileCardUrl(config.getPlatform(),
				config.getVersion(), profileId, cardId);
		ProfilesUtils.validateCard(card);
		
		// send the card json without id
		JsonElement _card = gson.toJsonTree(card,Card.class);
		String response = connector.ProcessTransaction(HttpMethod.put, url,
				_card);
		return gson.fromJson(response, ProfileResponse.class);

	}

	public ProfileResponse addCard(String profileId, Card card)
			throws BeanstreamApiException {
		ProfilesUtils.validateProfileId(profileId);
		String url = BeanstreamUrls.getProfileCardsUrl(config.getPlatform(),
				config.getVersion(), profileId);

		ProfilesUtils.validateCard(card);
		String response = connector.ProcessTransaction(HttpMethod.post, url,
				card);
		return gson.fromJson(response, ProfileResponse.class);

	}

	public ProfileResponse removeCard(String profileId, String cardId)
			throws BeanstreamApiException {
		ProfilesUtils.validateProfileId(profileId);
		Gateway.assertNotEmpty(cardId, "card it to remove is empty");
		String url = BeanstreamUrls.getProfileCardUrl(config.getPlatform(),
				config.getVersion(), profileId, cardId);

		String response = connector.ProcessTransaction(HttpMethod.delete, url,
				null);
		return gson.fromJson(response, ProfileResponse.class);

	}

}
