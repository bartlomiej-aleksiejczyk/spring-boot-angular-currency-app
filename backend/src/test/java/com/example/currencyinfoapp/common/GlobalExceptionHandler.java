package com.example.currencyinfoapp.common;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

class GlobalExceptionHandlerTest {

        private GlobalExceptionHandler globalExceptionHandler;

        @BeforeEach
        void setUp() {
                globalExceptionHandler = new GlobalExceptionHandler();
        }

        @Test
        void testHandleMethodArgumentNotValid() {
                BindException bindException = new BindException(new Object(), "object");
                MethodArgumentNotValidException ex =
                                new MethodArgumentNotValidException(null, bindException);

                ProblemDetail problemDetail = globalExceptionHandler.handleValidationException(ex);

                assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
                assertEquals("Bad Request", problemDetail.getTitle());
                assertEquals("Please make sure to provide a valid request, ",
                                problemDetail.getDetail());
                assertEquals(URI.create(
                                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"),
                                problemDetail.getType());
        }

        @Test
        void testHandleGenericException() {
                Exception ex = new Exception("Unexpected error");
                WebRequest request = Mockito.mock(WebRequest.class);

                ResponseEntity<ProblemDetail> response =
                                globalExceptionHandler.handleGenericException(ex, request);
                ProblemDetail problemDetail = response.getBody();

                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
                assertEquals("Internal Server Error", problemDetail.getTitle());
                assertEquals("An unexpected error occurred. Please try again later.",
                                problemDetail.getDetail());
                assertEquals(URI.create(
                                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/500"),
                                problemDetail.getType());
        }

        @Test
        void testHandleIllegalArgumentException() {
                IllegalArgumentException ex =
                                new IllegalArgumentException("Invalid argument provided");

                ResponseEntity<ProblemDetail> response =
                                globalExceptionHandler.handleIllegalArgumentException(ex);
                ProblemDetail problemDetail = response.getBody();

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());


                assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
                assertEquals("Invalid Argument", problemDetail.getTitle());
                assertEquals("Invalid argument provided", problemDetail.getDetail());
                assertEquals(URI.create(
                                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"),
                                problemDetail.getType());
        }

        @Test
        void testHandleNoResourceFoundException() {
                NoResourceFoundException ex = new NoResourceFoundException(HttpMethod.GET,
                                "test/non/existant/endpoint");
                WebRequest request = Mockito.mock(WebRequest.class);

                ResponseEntity<Object> response =
                                globalExceptionHandler.handleNoResourceFoundException(ex, null,
                                                HttpStatus.NOT_FOUND, request);
                ProblemDetail problemDetail = (ProblemDetail) response.getBody();

                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

                assertEquals(HttpStatus.NOT_FOUND.value(), problemDetail.getStatus());
                assertEquals("Resource Not Found", problemDetail.getTitle());
                assertEquals("No static resource test/non/existant/endpoint.",
                                problemDetail.getDetail());
                assertEquals(URI.create(
                                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/404"),
                                problemDetail.getType());
        }
}
