# Bambora Java SDK[![Build Status](https://travis-ci.org/Beanstream-DRWP/beanstream-java.svg?branch=master)](https://travis-ci.org/Beanstream-DRWP/beanstream-java)


Integration with Bambora payments gateway is a simple, flexible solution.

You can choose between a straightforward payment requiring very few parameters; or, you can customize a feature-rich integration.

To assist as a centralized record of all your sales, we also accept cash and cheque transactions.

For very detailed information on the Payments API, look at the Bambora [developer portal](https://dev.na.bambora.com/docs/references/payment_SDKs/take_payments/).

# Setup
Once you have cloned the repository you will need to load the project in your favourite IDE (NetBeans, Eclipse, etc.)

The project uses [Gradle](http://www.gradle.org/) to build the project. If you are using Netbeans it is very easy to install the Gradle plugin. Just navigate to Tools->Plugins, click on the Available Plugins tab and locate the 'Gradle Support' plugin. Click the Install button.

With the gradle plugin installed you can open the java project by navigating to File->Open Project and locating the java project. 

## TLS 1.2 support
For testing instructions with our TLS1.2-only server, please refer to our [developer portal](https://dev.na.bambora.com/docs/references/payment_SDKs/support_tls12/#java-sdk)

## Gradle
```
repositories {
  maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
}
dependencies {
  compile("com.beanstream.api:beanstream:1.0.0-SNAPSHOT") {changing=true}
}
```

## Maven
```xml
<repositories>
  <repository>
    <id>snapshots-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
  </repository>
</repositories>

<dependencies>
  <dependency>
	<groupId>com.beanstream.api</groupId>
	<artifactId>beanstream</artifactId>
	<version>1.0.0-SNAPSHOT</version>
  </dependency>
</dependencies>
```

# Bambora list of response codes/categories description
Please see the detailed list of codes/categories description [here](https://help.na.bambora.com/hc/en-us/articles/115013189148-Response-Message-Codes-and-Descriptions)

# Make a Payment
Before you begin making purchases, you need to create a Beanstream API object. It holds your user credentials and provides access to the various APIs.

```java
Gateway beanstream = new Gateway("v1", YOUR_MERCHANT_ID, "YOUR_API_KEY");
```

Next you can create the Card Payment Request, which is a payment using a credit card, and pass that to the Gateway object.

```java
CardPaymentRequest req = new CardPaymentRequest();
	req.setAmount("100.00")
	.setMerchantId("300200578")
	.setOrderNumber("myOrderNumber_00003");
	req.getCard()
			.setName("John Doe")
			.setNumber("5100000010001004")
			.setExpiryMonth("12")
			.setExpiryYear("18")
			.setCvd("123");

try {
            
	PaymentResponse response = beanstream.payments().makePayment(req);
	System.out.println("Aproved? "+response.isApproved());
	
} catch (BeanstreamApiException ex) {
	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
}			
```


# Void a Payment
To void a payment you will need a valid paymentId and the amount, and a Beanstream API (you can use the same Beanstream API instance for multiples requests even for different operations like (makePayment, voidPayment,...))


As with makePayment using your configured Beanstream API instance (Gateway object instance) call the voidPayment method of the PaymentAPI.
If the voidPayment is successful you will not have any exceptions, else you will need to handle the exception.


```java
String paymentId = ....;
double amount = ....;

try {
	PaymentResponse response = beanstream.payments().voidPayment(paymentId, amount);
	// void payment success, your response contains the payment transaction but with the type 'VP'
} catch (BeanstreamApiException ex) {
	// handle any errors
}	
```

# Pre Authorize a purchase
Pre authorize is the same process as doing a normal payment, with the only difference that with pre-authorize you have to steps
Pre-authorize a payment. Use this if you want to know if a customer has sufficient funds before processing a payment.
A real-world example of  this is pre-authorizing at the gas pump for $100 before you fill up, then end up only using $60 of gas; the customer is only charged $60. The final payment is used with preAuthCompletion() method.

Step 1 do the pre authorization
```java

CardPaymentRequest paymentRequest = ...;

try {
	PaymentResponse response = beanstream.payments().preAuth(paymentRequest);

} catch (BeanstreamApiException ex) {
	// handle any errors
}

```

Step 2 complete the purchase with the final amount

```java
try{
	PaymentResponse authResp = beanstream.payments().preAuthCompletion(preAuthorizedPaymentId, finalAmount, order_number); // order_number is optional
} catch (BeanstreamApiException ex) {
	// handle any errors
}
```

# API Payment Profiles

Payment Profiles allow you to store a customer's card number and other information, such as billing address and shipping address.
The card number stored on the profile is a multi-use token and is called the ID.

# Create a Profile
To create a payment profile you must first configure your gateway to use the profiles API key (instead of your Payments API key. Then you can call one of the createProfile methods on the Profiles API class and supply it with the minimum parameters of a Card and a Billing Address:

```java
Gateway beanstream = new Gateway("v1", 300200578,
				"4BaD82D9197b4cc4b70a221911eE9f70", // payments API passcode
				"D97D3BE1EE964A6193D17A571D9FBC80", // profiles API passcode
				"4e6Ff318bee64EA391609de89aD4CF5d");// reporting API passcode

Card card = new Card()
	.setName("John Doe")
	.setNumber("5100000010001004")
	.setExpiryMonth("12")
	.setExpiryYear("18")
	.setCvd("123");

Address billing = new Address.AddressBuilder()
	.name("JANE DOE")
	.city("VICTORIA")
	.province("BC")
	.country("CA")
	.addressLine1("123 FAKE ST.")
	.postalCode("V9T2G6")
	.emailAddress("TEST@BEANSTREAM.COM")
	.phoneNumber("12501234567")
	.build();

ProfileResponse createdProfile = beanstream.profiles().createProfile(card, billing);
```

This will return you a ProfileResponse object. In that object you will want to get the Id; this is the identifier that you will use to retrieve the payment profiles later on and is the Id you want to save in your database and load when the user signs into their account.

```java
profileId = createdProfile.getId();
```

# Create a Profile using a Token
```java
Gateway beanstream = new Gateway("v1", 300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70", // payments API passcode
                "D97D3BE1EE964A6193D17A571D9FBC80", // profiles API passcode
                "4e6Ff318bee64EA391609de89aD4CF5d");// reporting API passcode

Token token = new Token("JANE DOE","your_token_here");

Address billing = new Address.AddressBuilder()
	.name("JANE DOE")
	.city("VICTORIA")
	.province("BC")
	.country("CA")
	.addressLine1("123 FAKE ST.")
	.postalCode("V9T2G6")
	.emailAddress("TEST@BEANSTREAM.COM")
	.phoneNumber("12501234567")
	.build();

ProfileResponse createdProfile = beanstream.profiles().createProfile(token, billing);
String profileId = createdProfile.getId();
```


# Delete a Profile
Deleting a profile is very simple, you just need the profile ID and then use the Profiles API as such:
```java
beanstream.profiles().deleteProfileById(profileId);
```

# Retrieve a Profile
To retrieve an existing profile you need the profile ID again:
```java
PaymentProfile paymentProfile = beanstream.profiles().getProfileById(profileId);
```

This will return you a PaymentProfile object. A PaymentProfile object is transient, meaning that if you make changes to it they will not be automatically persisted. You must call updateProfile to persist the changes.

# Update A Profile
To update a profile you first must retrieve the profile object from the API. Then you can modify any fields it contains and submit the changed object back to the API's updateProfile method:
```java
PaymentProfile paymentProfile = beanstream.profiles().getProfileById(profileId);
paymentProfile.setLanguage("fr");
beanstream.profiles().updateProfile(paymentProfile);
```

for more information please check the class in src/test ProfilesAPITest.class so you can see more example and run the automatic test.

# Connection Timeouts
You can customize connection timeouts, and anything else with the connections by supplying a custom HttpClient to the Gateway object:

```java
RequestConfig reqC = RequestConfig.custom()
		.setSocketTimeout(1000) // 1 second in milliseconds
		.setConnectTimeout(1000) // 1 second in milliseconds
	.build();
HttpClient client = HttpClients.custom()
		.setDefaultRequestConfig(reqC)
	.build();

Gateway beanstream = new Gateway("v1", YOUR_MERCHANT_ID,
		"YOUR_PAYMENTS_API_PASSCODE", // payments API passcode
		"YOUR_PROFILES_API_PASSCODE", // profiles API passcode
		"YOUR_REPORTING_API_PASSCODE");// reporting API passcode

beanstream.setCustomHttpsClient(client);
```
