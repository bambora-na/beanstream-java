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
package com.beanstream.api.test;

import com.beanstream.domain.Transaction;
import com.beanstream.domain.TransactionRecord;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.CardPaymentRequest;
import com.beanstream.requests.Criteria;
import com.beanstream.requests.Operators;
import com.beanstream.requests.QueryFields;
import com.beanstream.responses.PaymentResponse;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;

/**
 *
 * @author bowens
 */
public class ReportsAPITest extends BasePaymentsTest {
    
    @Test
    public void testGetAllTransactionRecordsBetweenDates() {
        CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("TEST_QUERY"), "12.34");
        try {
            PaymentResponse payment = gateway.payments().makePayment(paymentRequest);
            Assert.assertNotNull("Payment was null, cannot proceed with test", payment);
            Assert.assertNotNull("Payment had no ID, cannot proceed with test", payment.id);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date startDate = cal.getTime(); // yesterday
            Date endDate = new Date(); // today

            List<TransactionRecord> query = gateway.reports().query(startDate, endDate, 1, 100, null);
            for (TransactionRecord tr : query) {
                Assert.assertNotNull(tr.getDateTime());
            }
            Assert.assertNotNull(query);
            Assert.assertFalse("The transaction list should not be empty.", query.isEmpty());
                
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(ReportsAPITest.class.getName()).log(Level.SEVERE, "Error accessing Beanstream API", ex);
            Assert.assertTrue("Unexpected Exception", false);
        } catch (ParseException ex) {
            Assert.assertTrue("Unexpected Date format Exception "+ex.getMessage(), false);
        }
    }
    
    @Test
    public void testGetAllTransactionRecordsWithCriteria() {
        CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("TEST_QUERY"), "12.34");
        paymentRequest.getCard().setName("Sideshow Bob");
        
        try {
            PaymentResponse payment = gateway.payments().makePayment(paymentRequest);
            Assert.assertNotNull("Payment was null, cannot proceed with test", payment);
            Assert.assertNotNull("Payment had no ID, cannot proceed with test", payment.id);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date startDate = cal.getTime(); // yesterday
            Date endDate = new Date(); // today
            Criteria[] searchFilter = new Criteria[]{
                new Criteria(QueryFields.CardOwner, Operators.StartWith, "Sideshow")
            };
            List<TransactionRecord> results = gateway.reports().query(startDate, endDate, 1, 100, searchFilter);
            Assert.assertNotNull(results);
            Assert.assertFalse("The transaction list should not be empty.", results.isEmpty());
            boolean foundIt = false;
            for (TransactionRecord t : results) {
                if (t.getCardOwner().contains("Sideshow Bob") )
                    foundIt = true;
            }
            Assert.assertTrue("Did not find our transaction record in the query!", foundIt);
            
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(ReportsAPITest.class.getName()).log(Level.SEVERE, "Error accessing Beanstream API", ex);
            Assert.assertTrue("Unexpected Exception", false);
        }
    }
    
    @Ignore("Test is timing out currently, disabling for now")
    @Test
    public void testGetSingleTransactionRecordById() {
        CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("TEST"), "90.00");
        try {
            PaymentResponse payment = gateway.payments().makePayment(paymentRequest);
            Assert.assertNotNull("Payment was null, cannot proceed with test", payment);
            Assert.assertNotNull("Payment had no ID, cannot proceed with test", payment.id);
            
            Transaction transaction = gateway.reports().getTransaction(payment.id);
            Assert.assertNotNull(transaction);
            Assert.assertEquals("Amounts did not match", "90.00", transaction.getAmount());
            
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(ReportsAPITest.class.getName()).log(Level.SEVERE, "Error accessing Beanstream API "+ex.getCode()+", "+ex.getCategory()+", "+ex.getMessage(), ex);
            Assert.assertTrue("Unexpected Exception", false);
        }
    }
}
