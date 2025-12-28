package com.noisevisionproduction.playmeetwebsite.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.verify;

class LogsPrintTest{

    @Spy
    private Logger logger = LoggerFactory.getLogger(TestLogsPrint.class);

    @InjectMocks
    private TestLogsPrint testLogsPrint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogError() {
        String errorMessage = "Test error message";
        Throwable throwable = new RuntimeException("Test exception");

        testLogsPrint.logError(errorMessage, throwable);

        verify(logger).error(errorMessage, throwable);
    }

    public static class TestLogsPrint extends LogsPrint {

    }
}