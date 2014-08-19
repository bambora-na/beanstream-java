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
package com.beanstream;

/**
 *
 * @author bowens
 */
public class Configuration {
    private int merchantId;
    private String apiPasscode;
    private String profilesPasscode;
    private String version = "v1";
    private String platform = "www";

    public Configuration() {
    }

    public Configuration(int merchantId, String apiPasscode) {
        this.merchantId = merchantId;
        this.apiPasscode = apiPasscode;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public Configuration setMerchantId(int merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public String getApiPasscode() {
        return apiPasscode;
    }

    public Configuration setApiPasscode(String apiPasscode) {
        this.apiPasscode = apiPasscode;
        return this;
    }

    public String getProfilesPasscode() {
        return profilesPasscode;
    }

    public Configuration setProfilesPasscode(String profilesPasscode) {
        this.profilesPasscode = profilesPasscode;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Configuration setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public Configuration setPlatform(String platform) {
        this.platform = platform;
        return this;
    }
    
    
}
