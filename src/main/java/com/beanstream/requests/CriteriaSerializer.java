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
package com.beanstream.requests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Operators are translated from the enum and URL encoded.
 * 
 * @author bowens
 */
public class CriteriaSerializer implements JsonSerializer<Criteria> {

    @Override
    public JsonElement serialize(Criteria criteria, Type type, JsonSerializationContext jsc) {
        
        JsonObject json = new JsonObject();
        json.addProperty("field", criteria.getField().ordinal()+1 ); // increase by 1
        try {
            String operator = criteria.getOperator().toString();
            if ( operator.equals(Operators.StartWith.toString()) )
                operator = "START%20WITH"; // add a space
            else if ( operator.equals(Operators.Equals.toString()) )
                operator = URLEncoder.encode("=", "UTF-8");
            else if ( operator.equals(Operators.GreaterThan.toString()) )
                operator = URLEncoder.encode(">", "UTF-8");
            else if ( operator.equals(Operators.GreaterThanEqual.toString()) )
                operator = URLEncoder.encode(">=", "UTF-8");
            else if ( operator.equals(Operators.LessThan.toString()) )
                operator = URLEncoder.encode("<", "UTF-8");
            else if ( operator.equals(Operators.LessThanEqual.toString()) )
                operator = URLEncoder.encode("<=", "UTF-8");
            
            json.addProperty("operator",  operator);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CriteriaSerializer.class.getName()).log(Level.SEVERE, "Wrong encoding scheme!", ex);
        }
        json.addProperty("value", criteria.getValue() );
        
        return json;
    }

    
    
}
