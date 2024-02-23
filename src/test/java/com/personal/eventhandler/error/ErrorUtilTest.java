package com.personal.eventhandler.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ErrorUtilTest {

    @Test
    void getRetryBackoffInMillisTest_withEqualRetries() {
        ErrorUtil underTest = new ErrorUtil();
        int retry1 = underTest.getRetryBackoffInMillis(1);
        int retry2 = underTest.getRetryBackoffInMillis(1);

        assertNotEquals(retry1, retry2);
    }

    @Test
    void getRetryBackoffInMillisTest_withUnEqualRetries() {
        ErrorUtil underTest = new ErrorUtil();
        int retry1 = underTest.getRetryBackoffInMillis(1);
        int retry2 = underTest.getRetryBackoffInMillis(2);

        assertTrue(retry1 < retry2);
    }
}
