package com.beanstream;


import com.beanstream.connection.BeanstreamUrls;
import com.beanstream.connection.HttpsConnector;
import com.beanstream.domain.Card;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.CardPaymentRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    /*public static void main(String[] args) {
        SampleTransactions t = new SampleTransactions();
        t.httpConnector();
    }*/
    
    /*private void httpConnector() {
        HttpsConnector connector = new HttpsConnector("300200578", "4BaD82D9197b4cc4b70a221911eE9f70");
        try {
            URL url = new URL("https://www.beanstream.com/api/v1/payments");
            CardPaymentRequest req = new CardPaymentRequest();
            req.amount = "100.00";
            req.merchant_id = "300200578";
            req.order_number = "1";
            req.card = new Card();
                req.card.name = "John Doe";
                req.card.number = "5100000010001004";
                req.card.expiry_month = "12";
                req.card.expiry_year = "18";
                req.card.cvd = "123";
            
            
            
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(SampleTransactions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(SampleTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }*/
}
