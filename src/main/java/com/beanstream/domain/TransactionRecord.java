// The MIT License (MIT)
//
// Copyright (c) 2014 Beanstream Internet Commerce Corp, Digital River, Inc.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
//

package com.beanstream.domain;

import com.google.gson.annotations.SerializedName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a single transaction from the query of past transactions.
 * Returned by the ReportingAPI.
 * @author bowens
 */
public class TransactionRecord
{
    @SerializedName("row_id") private int rowId;
    @SerializedName("trn_id") private int transactionId;
    @SerializedName("trn_date_time") private String dateTimeStr;
    private Date dateTime;
    @SerializedName("trn_type") private String type;
    @SerializedName("trn_order_number") private String orderNumber;
    @SerializedName("trn_payment_method") private String paymentMethod;
    @SerializedName("trn_comments") private String comments;
    @SerializedName("trn_masked_card") private String maskedCard;
    @SerializedName("trn_amount") private double amount;
    @SerializedName("trn_returns") private double returns;
    @SerializedName("trn_completions") private double completions;
    @SerializedName("trn_voided") private String voided;
    @SerializedName("trn_response") private int response;
    @SerializedName("trn_card_type") private String cardType;
    @SerializedName("trn_batch_no") private int batchNumber;
    @SerializedName("trn_avs_result") private String avsResult;
    @SerializedName("trn_cvd_result") private int cvdResult;
    @SerializedName("trn_card_expiry") private String cardExpiry;
    @SerializedName("message_id") private String messageId;
    @SerializedName("message_text") private String messageText;
    @SerializedName("trn_card_owner") private String cardOwner;
    @SerializedName("trn_ip") private String ipAddress;
    @SerializedName("trn_approval_code") private String approvalCode;
    @SerializedName("trn_reference") private int reference;
    @SerializedName("b_name") private String billingName;
    @SerializedName("b_email") private String billingEmail;
    @SerializedName("b_phone") private String billingPhone;
    @SerializedName("b_address1") private String billingAddress1;
    @SerializedName("b_address2") private String billingAddress2;
    @SerializedName("b_city") private String billingCity;
    @SerializedName("b_province") private String billingProvince;
    @SerializedName("b_postal") private String billingPostal;
    @SerializedName("b_country") private String billingCountry;
    @SerializedName("s_name") private String shippingName;
    @SerializedName("s_email") private String shippingEmail;
    @SerializedName("s_phone") private String shippingPhone;
    @SerializedName("s_address1") private String shippingAddress1;
    @SerializedName("s_address2") private String shippingAddress2;
    @SerializedName("s_city") private String shippingCity;
    @SerializedName("s_province") private String shippingProvince;
    @SerializedName("s_postal") private String shippingPostal;
    @SerializedName("s_country") private String shippingCountry;
    @SerializedName("ref1") private String ref1;
    @SerializedName("ref2") private String ref2;
    @SerializedName("ref3") private String ref3;
    @SerializedName("ref4") private String ref4;
    @SerializedName("ref5") private String ref5;
    @SerializedName("product_name") private String productName;
    @SerializedName("product_id") private String productId;
    @SerializedName("customer_code") private String customerCode;

    public int getRowId()
    { return rowId; }

    public void setRowId(int rowId)
    { this.rowId=rowId; }

    public int getTransactionId()
    { return transactionId; }

    public void setTransactionId(int transactionId)
    { this.transactionId=transactionId; }

    public String getDateTimeStr()
    { return dateTimeStr; }

    public void setDateTimeStr(String dateTime)
    { this.dateTimeStr=dateTime; }
    
    public Date getDateTime() throws ParseException { 
        if (dateTime == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            dateTime = sdf.parse(dateTimeStr);
        }
        return dateTime; 
    }

    public void setDateTime(Date dateTime)
    { this.dateTime=dateTime; }

    public String getType()
    { return type; }

    public void setType(String type)
    { this.type=type; }

    public String getOrderNumber()
    { return orderNumber; }

    public void setOrderNumber(String orderNumber)
    { this.orderNumber=orderNumber; }

    public String getPaymentMethod()
    { return paymentMethod; }

    public void setPaymentMethod(String paymentMethod)
    { this.paymentMethod=paymentMethod; }

    public String getComments()
    { return comments; }

    public void setComments(String comments)
    { this.comments=comments; }

    public String getMaskedCard()
    { return maskedCard; }

    public void setMaskedCard(String maskedCard)
    { this.maskedCard=maskedCard; }

    public double getAmount()
    { return amount; }

    public void setAmount(double amount)
    { this.amount=amount; }

    public double getReturns()
    { return returns; }

    public void setReturns(double returns)
    { this.returns=returns; }

    public double getCompletions()
    { return completions; }

    public void setCompletions(double completions)
    { this.completions=completions; }

    public String getVoided()
    { return voided; }

    public void setVoided(String voided)
    { this.voided=voided; }

    public int getResponse()
    { return response; }

    public void setResponse(int response)
    { this.response=response; }

    public String getCardType()
    { return cardType; }

    public void setCardType(String cardType)
    { this.cardType=cardType; }

    public int getBatchNumber()
    { return batchNumber; }

    public void setBatchNumber(int batchNumber)
    { this.batchNumber=batchNumber; }

    public String getAVSResult()
    { return avsResult; }

    public void setAVSResult(String avsResult)
    { this.avsResult=avsResult; }

    public int getCVDResult()
    { return cvdResult; }

    public void setCVDResult(int cvdResult)
    { this.cvdResult=cvdResult; }

    public String getCardExpiry()
    { return cardExpiry; }

    public void setCardExpiry(String cardExpiry)
    { this.cardExpiry=cardExpiry; }

    public String getMessageId()
    { return messageId; }

    public void setMessageId(String messageId)
    { this.messageId=messageId; }

    public String getMessageText()
    { return messageText; }

    public void setMessageText(String messageText)
    { this.messageText=messageText; }

    public String getCardOwner()
    { return cardOwner; }

    public void setCardOwner(String cardOwner)
    { this.cardOwner=cardOwner; }

    public String getIPAddress()
    { return ipAddress; }

    public void setIPAddress(String ipAddress)
    { this.ipAddress=ipAddress; }

    public String getApprovalCode()
    { return approvalCode; }

    public void setApprovalCode(String approvalCode)
    { this.approvalCode=approvalCode; }

    public int getReference()
    { return reference; }

    public void setReference(int reference)
    { this.reference=reference; }

    public String getBillingName()
    { return billingName; }

    public void setBillingName(String billingName)
    { this.billingName=billingName; }

    public String getBillingEmail()
    { return billingEmail; }

    public void setBillingEmail(String billingEmail)
    { this.billingEmail=billingEmail; }

    public String getBillingPhone()
    { return billingPhone; }

    public void setBillingPhone(String billingPhone)
    { this.billingPhone=billingPhone; }

    public String getBillingAddress1()
    { return billingAddress1; }

    public void setBillingAddress1(String billingAddress1)
    { this.billingAddress1=billingAddress1; }

    public String getBillingAddress2()
    { return billingAddress2; }

    public void setBillingAddress2(String billingAddress2)
    { this.billingAddress2=billingAddress2; }

    public String getBillingCity()
    { return billingCity; }

    public void setBillingCity(String billingCity)
    { this.billingCity=billingCity; }

    public String getBillingProvince()
    { return billingProvince; }

    public void setBillingProvince(String billingProvince)
    { this.billingProvince=billingProvince; }

    public String getBillingPostal()
    { return billingPostal; }

    public void setBillingPostal(String billingPostal)
    { this.billingPostal=billingPostal; }

    public String getBillingCountry()
    { return billingCountry; }

    public void setBillingCountry(String billingCountry)
    { this.billingCountry=billingCountry; }

    public String getShippingName()
    { return shippingName; }

    public void setShippingName(String shippingName)
    { this.shippingName=shippingName; }

    public String getShippingEmail()
    { return shippingEmail; }

    public void setShippingEmail(String shippingEmail)
    { this.shippingEmail=shippingEmail; }

    public String getShippingPhone()
    { return shippingPhone; }

    public void setShippingPhone(String shippingPhone)
    { this.shippingPhone=shippingPhone; }

    public String getShippingAddress1()
    { return shippingAddress1; }

    public void setShippingAddress1(String shippingAddress1)
    { this.shippingAddress1=shippingAddress1; }

    public String getShippingAddress2()
    { return shippingAddress2; }

    public void setShippingAddress2(String shippingAddress2)
    { this.shippingAddress2=shippingAddress2; }

    public String getShippingCity()
    { return shippingCity; }

    public void setShippingCity(String shippingCity)
    { this.shippingCity=shippingCity; }

    public String getShippingProvince()
    { return shippingProvince; }

    public void setShippingProvince(String shippingProvince)
    { this.shippingProvince=shippingProvince; }

    public String getShippingPostal()
    { return shippingPostal; }

    public void setShippingPostal(String shippingPostal)
    { this.shippingPostal=shippingPostal; }

    public String getShippingCountry()
    { return shippingCountry; }

    public void setShippingCountry(String shippingCountry)
    { this.shippingCountry=shippingCountry; }

    public String getRef1()
    { return ref1; }

    public void setRef1(String ref1)
    { this.ref1=ref1; }

    public String getRef2()
    { return ref2; }

    public void setRef2(String ref2)
    { this.ref2=ref2; }

    public String getRef3()
    { return ref3; }

    public void setRef3(String ref3)
    { this.ref3=ref3; }

    public String getRef4()
    { return ref4; }

    public void setRef4(String ref4)
    { this.ref4=ref4; }

    public String getRef5()
    { return ref5;}

    public void setRef5(String ref5) 
    { this.ref5 = ref5; }

    public String getProductName()
    { return productName; }

    public void setProductName(String productName)
    { this.productName=productName; }

    public String getProductId()
    { return productId; }

    public void setProductId(String productId)
    { this.productId=productId; }

    public String getCustomerCode()
    { return customerCode; }

    public void setCustomerCode(String customerCode)
    { this.customerCode=customerCode; }

}
