package com.beanstream.api.test;

import com.beanstream.exceptions.*;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.apache.http.HttpStatus.*;

/**
 * Created by michael on 9/16/14.
 */
public class ExceptionFactoryTest {

    @Test
    public void theExceptionFactoryCreatesTheCorrectExceptionsBasedOnHttpStatusCode() {
        int anyOtherCode = -1;
        assertTrue(getMappedException(SC_MOVED_TEMPORARILY) instanceof RedirectionException);           // 302
        assertTrue(getMappedException(SC_BAD_REQUEST) instanceof InvalidRequestException);              // 400
        assertTrue(getMappedException(SC_UNAUTHORIZED) instanceof UnauthorizedException);               // 401
        assertTrue(getMappedException(SC_PAYMENT_REQUIRED) instanceof BusinessRuleException);           // 402
        assertTrue(getMappedException(SC_FORBIDDEN) instanceof ForbiddenException);                     // 403
        assertTrue(getMappedException(SC_METHOD_NOT_ALLOWED) instanceof InvalidRequestException);       // 405
        assertTrue(getMappedException(SC_UNSUPPORTED_MEDIA_TYPE) instanceof InvalidRequestException);   // 415
        assertTrue(getMappedException(anyOtherCode) instanceof InternalServerException);                // (any code other than the above, but intended for 500+)
    }

    private BeanstreamApiException getMappedException(int statusCode) {
        return BeanstreamApiException.getMappedException(statusCode);
    }
}