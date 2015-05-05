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
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bowens
 */
public class CriteriaSerializerTest {
    
    public CriteriaSerializerTest() {
    }

    @Test
    public void testSerialize() {
        CriteriaSerializer serializer = new CriteriaSerializer();
        
        Criteria cEquals = new Criteria(QueryFields.Amount, Operators.Equals, null);
        JsonElement json = serializer.serialize(cEquals, null, null);
        Assert.assertEquals("Testing Equals", "\"%3D\"", json.getAsJsonObject().get("operator").toString() );
        
        Criteria cLessThan = new Criteria(QueryFields.Amount, Operators.LessThan, null);
        json = serializer.serialize(cLessThan, null, null);
        Assert.assertEquals("Testing Less Than", "\"%3C\"", json.getAsJsonObject().get("operator").toString() );
        
        Criteria cGreaterThan = new Criteria(QueryFields.Amount, Operators.GreaterThan, null);
        json = serializer.serialize(cGreaterThan, null, null);
        Assert.assertEquals("Testing Greater Than", "\"%3E\"", json.getAsJsonObject().get("operator").toString() );
        
        Criteria cLessThanEquals = new Criteria(QueryFields.Amount, Operators.LessThanEqual, null);
        json = serializer.serialize(cLessThanEquals, null, null);
        Assert.assertEquals("Testing Less Than Equals", "\"%3C%3D\"", json.getAsJsonObject().get("operator").toString() );
        
        Criteria cGreaterThanEquals = new Criteria(QueryFields.Amount, Operators.GreaterThanEqual, null);
        json = serializer.serialize(cGreaterThanEquals, null, null);
        Assert.assertEquals("Testing Greater Than Equals", "\"%3E%3D\"", json.getAsJsonObject().get("operator").toString() );
        
        Criteria cStartWith = new Criteria(QueryFields.Amount, Operators.StartWith, null);
        json = serializer.serialize(cStartWith, null, null);
        Assert.assertEquals("Testing Start With", "\"START%20WITH\"", json.getAsJsonObject().get("operator").toString() );
    }
    
}
