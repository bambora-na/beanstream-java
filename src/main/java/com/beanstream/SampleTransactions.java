package com.beanstream;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.CardPaymentRequest;
import com.beanstream.responses.PaymentResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

/**
 * Example code.
 * 
 * @author bowens
 */
public class SampleTransactions {
    
    public static void main(String[] args) {
        SampleTransactions t = new SampleTransactions();
        //t.testPayment();
        t.testVoidPayment();
    }
    
    private AtomicInteger sequence = new AtomicInteger(1); 
    private void testPayment() {
        
            
        CardPaymentRequest req = new CardPaymentRequest();
        req.setAmount( "100.00" );
        req.setMerchant_id( "300200578" );
        req.setOrder_number( "402" );
        req.getCard().setName( "John Doe" )
            .setNumber( "5100000010001004" )
            .setExpiry_month( "12" )
            .setExpiry_year( "18" )
            .setCvd( "123" );


        Gateway beanstream = new Gateway("v1", 300200578, "4BaD82D9197b4cc4b70a221911eE9f70");
        
        try {
            
            PaymentResponse response = beanstream.payments().makePayment(req);
            System.out.println("Aproved? "+response.isApproved());
            
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
        }
            
    }
    
    private String getRandomOrderId(String prefix){
    	String orderId = null; 
    	Date date = new Date();
    	StringBuilder sb = new StringBuilder();
    	if(prefix!=null){
    		sb.append(prefix);
    		sb.append("_");
    	}
    	SimpleDateFormat df = new SimpleDateFormat("MMkkmmssSSSS");
    	sb.append(sequence.getAndIncrement());
    	sb.append("_");
    	sb.append(df.format(date));
    	orderId = sb.toString();
    	if(orderId.length() >30){
    		orderId = orderId.substring(0, 29);
    	}
    	return orderId;
    }
    
    private void testVoidPayment(){
    	
    	Gateway beanstream = new Gateway("v1", 300200578, "4BaD82D9197b4cc4b70a221911eE9f70");
    	
    	CardPaymentRequest paymentRequest = new CardPaymentRequest();
    	paymentRequest.setAmount( "90.00" );
    	paymentRequest.setMerchant_id( "300200578" );
    	paymentRequest.setOrder_number( getRandomOrderId("PEDRO"));
    	paymentRequest.getCard().setName( "John Doe" )
            .setNumber( "5100000010001004" )
            .setExpiry_month( "12" )
            .setExpiry_year( "18" )
            .setCvd( "123" );
        
        try {
			PaymentResponse response = beanstream.payments().makePayment(paymentRequest);
			if(response.isApproved()){
				Gson gsonpp = new GsonBuilder().setPrettyPrinting().create();
				System.out.println("Your Payment has been approved response: \n"+gsonpp.toJson(response));
				
				response = beanstream.payments().voidPayment(response.id, 90.00);
				
				if("VP".equals(response.type)){
					System.out.println("The payment was voided");
					
				}else{
					System.out.println("The payment was not voided");
				}
			}
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
        }
    	
    }
}
