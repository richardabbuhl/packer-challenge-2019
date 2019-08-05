package com.mobiquityinc.exception;

import org.junit.Test;

import static org.junit.Assert.*;

public class APIExceptionTest {

    @Test
    public void testAPIException() {
        APIException exception = new APIException("Fancy message");
        assertEquals("Fancy message", exception.getMessage());
    }

}