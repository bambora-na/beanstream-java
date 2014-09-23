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
package com.beanstream.domain;

/**
 * Custom fields to set on a transaction.
 * Limited to 256 characters long.
 * 
 * @author bowens
 */
public class CustomFields {
    private String ref1;

    private String ref2;

    private String ref3;

    private String ref4;

    private String ref5;

    public String getRef1() {
        return ref1;
    }

    public void setRef1(String ref) {
        if (ref != null && ref.length() > 256)
            throw new IllegalArgumentException("Ref cannot be longer than 256 characters!");
        this.ref1 = ref;
    }

    public String getRef2() {
        return ref2;
    }

    public void setRef2(String ref) {
        if (ref != null && ref.length() > 256)
            throw new IllegalArgumentException("Ref cannot be longer than 256 characters!");
        this.ref2 = ref;
    }

    public String getRef3() {
        return ref3;
    }

    public void setRef3(String ref) {
        if (ref != null && ref.length() > 256)
            throw new IllegalArgumentException("Ref cannot be longer than 256 characters!");
        this.ref3 = ref;
    }

    public String getRef4() {
        return ref4;
    }

    public void setRef4(String ref) {
        if (ref != null && ref.length() > 256)
            throw new IllegalArgumentException("Ref cannot be longer than 256 characters!");
        this.ref4 = ref;
    }

    public String getRef5() {
        return ref5;
    }

    public void setRef5(String ref) {
        if (ref != null && ref.length() > 256)
            throw new IllegalArgumentException("Ref cannot be longer than 256 characters!");
        this.ref5 = ref;
    }
    
    
}
