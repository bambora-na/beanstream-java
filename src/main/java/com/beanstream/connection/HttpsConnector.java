/* The MIT License (MIT)
 *
 * Copyright (c) 2014 Beanstream Internet Commerce Corp, Digital River, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.beanstream.connection;

import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.responses.BeanstreamResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpsConnector {

	private final int merchantId;
	private final String apiPasscode;

	public HttpsConnector(int merchantId, String apiPasscode) {
		this.merchantId = merchantId;
		this.apiPasscode = apiPasscode;
	}

	// this should be refactored to to use java naming conventions (start
	// lowerCase for methods and properties, and Capital for class, Enum,
	// Contructors etc.)
	public String ProcessTransaction(HttpMethod httpMethod, String url,
			Object data) throws BeanstreamApiException {

		try {

			Gson gson = new Gson();
			String json = data != null ? gson.toJson(data) : null;

			// this is a temporary println while SDK is in development
			Gson gsonpp = new GsonBuilder().setPrettyPrinting().create();
			System.out.println(gsonpp.toJson(data));

			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				@Override
				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						HttpEntity entity = response.getEntity();
						String res = entity != null ? EntityUtils
								.toString(entity) : null;
						throw mappedException(status, res);
					}
				}

			};

			if (HttpMethod.post.equals(httpMethod)) {
				StringEntity entity = new StringEntity(json);
				HttpPost http = new HttpPost(url);
				http.setEntity(entity);
				return process(http, responseHandler);

			} else if (HttpMethod.put.equals(httpMethod)) {
				StringEntity entity = new StringEntity(json);
				HttpPut http = new HttpPut(url);
				http.setEntity(entity);
				return process(http, responseHandler);

			} else if (HttpMethod.get.equals(httpMethod)) {
				return process(new HttpGet(url), responseHandler);

			} else if (HttpMethod.delete.equals(httpMethod)) {
				return process(new HttpDelete(url), responseHandler);
			}

			return null;

		} catch (UnsupportedEncodingException ex) {
			throw handleException(ex, null);

		} catch (IOException ex) {
			throw handleException(ex, null);
		}

	}

	private String process(HttpUriRequest http,
			ResponseHandler<String> responseHandler) throws IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		String auth = Base64
				.encode((merchantId + ":" + apiPasscode).getBytes());

		http.addHeader("Content-Type", "application/json");
		http.addHeader("Authorization", "Passcode " + auth);

		String responseBody = httpclient.execute(http, responseHandler);

		return responseBody;
	}

	private HttpRequest getHttp(HttpMethod httpMethod, StringEntity entity) {
		if (HttpMethod.post.equals(httpMethod)) {
			HttpPost http = new HttpPost();
			http.setEntity(entity);
			return http;
		} else if (HttpMethod.put.equals(httpMethod)) {
			HttpPut http = new HttpPut();
			http.setEntity(entity);
			return http;
		} else if (HttpMethod.get.equals(httpMethod)) {
			HttpGet http = new HttpGet();
			return http;
		} else if (HttpMethod.delete.equals(httpMethod)) {
			HttpDelete http = new HttpDelete();
			return http;
		}
		return null;
	}

    /**
     * Provide a detailed error message when connecting to the Beanstream API fails.
     */
    private BeanstreamApiException handleException(Exception ex, HttpsURLConnection connection) {
        String message = "";
        if (connection != null) {
            try {
                int responseCode = connection.getResponseCode();
                message = responseCode+" "+connection.getContent().toString();
            } catch (IOException ex1) {
                Logger.getLogger(HttpsConnector.class.getName()).log(Level.SEVERE, "Error getting response code", ex1);
            }
        } else {
            message = "Connection error";
        }
        return new BeanstreamApiException(ex, message);
    }

    /**
     * Each exception will have a code and an ID that can help distinguish if the error
     * is card-holder facing (ie. Insufficient Funds) or programmer-facing (wrong API key).
     */
    private BeanstreamApiException mappedException(int status, String res) {

        BeanstreamResponse response = null;
        try {
            response = BeanstreamResponse.fromJson(res);
        } catch(Exception e) {
            BeanstreamApiException ex = BeanstreamApiException.getMappedException(status);
            return ex;
        }

        return BeanstreamApiException.getMappedException(status, response);
    }

}