Beanstream Java SDK
===============

Integration with Beanstream’s payments gateway is a simple, flexible solution.

You can choose between a straightforward payment requiring very few parameters; or, you can customize a feature-rich integration.

In addition to credit card transactions, Canadian merchants can process INTERAC payments. To assist as a centralized record of all your sales, we also accept cash and cheque transactions.

For very detailed information on the Payments API, look at the Beanstream developer portal's [documentation](http://developer.beanstream.com/documentation/take-payments/purchases-pre-authorizations/).

# Setup
Once you have cloned the repository you will need to load the project in your favourite IDE (NetBeans, Eclipse, etc.)

The project uses [Gradle](http://www.gradle.org/) to build the project. If you are using Netbeans it is very easy to install the Gradle plugin. Just navigate to Tools->Plugins, click on the Available Plugins tab and locate the 'Gradle Support' plugin. Click the Install button.

With the gradle plugin installed you can open the java project by navigating to File->Open Project and locating the java project. 


# Make a Payment
Before you begin making purchases, you need to create a Beanstream API object. It holds your user credentials and provides access to the various APIs.

```java
Gateway beanstream = new Gateway("v1", YOUR_MERCHANT_ID, "YOUR_API_KEY");
```

Next you can create the Card Payment Request, which is a payment using a credit card, and pass that to the Gateway object.

```java
CardPaymentRequest req = new CardPaymentRequest();
	req.setAmount( "100.00" );
	req.setMerchant_id( "300200578" );
	req.setOrder_number( "402" );
	req.getCard().setName( "John Doe" )
		.setNumber( "5100000010001004" )
		.setExpiry_month( "12" )
		.setExpiry_year( "18" )
		.setCvd( "123" );

try {
            
	PaymentResponse response = beanstream.payments().makePayment(req);
	System.out.println("Aproved? "+response.isApproved());
	
} catch (BeanstreamApiException ex) {
	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
}			
```

