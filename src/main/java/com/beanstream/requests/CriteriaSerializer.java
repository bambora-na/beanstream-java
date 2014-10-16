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
 * Enums are zero-based, yet the fields in the Query search are 1-based. So we have to increment each output value by 1
 * when writing to json.
 * 
 * @author bowens
 */
public class CriteriaSerializer implements JsonSerializer<Criteria> {

    @Override
    public JsonElement serialize(Criteria criteria, Type type, JsonSerializationContext jsc) {
        
        JsonObject json = new JsonObject();
        json.addProperty("field", criteria.getField().ordinal()+1 ); // increase by 1
        try {
            json.addProperty("operator", URLEncoder.encode(criteria.getOperator().toString(), "UTF-8") );
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CriteriaSerializer.class.getName()).log(Level.SEVERE, "Wrong encoding scheme!", ex);
        }
        json.addProperty("value", criteria.getValue() );
        
        return json;
    }

    
    
}
