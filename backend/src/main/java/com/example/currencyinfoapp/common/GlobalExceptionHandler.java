package com.example.currencyinfoapp.common;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex, HttpHeaders headers,
                        HttpStatusCode status, WebRequest request) {
                ProblemDetail problemDetail = handleValidationException(ex);
                return ResponseEntity.status(status.value()).body(problemDetail);
        }

        protected ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
                String details = getErrorsDetails(ex);
                ProblemDetail problemDetail =
                                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, details);
                problemDetail.setType(URI.create(
                                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"));
                problemDetail.setTitle("Bad Request");
                problemDetail.setInstance(ex.getBody().getInstance());
                problemDetail.setProperty("timestamp", Instant.now());
                return problemDetail;
        }

        private String getErrorsDetails(MethodArgumentNotValidException ex) {
                return Optional.ofNullable(ex.getDetailMessageArguments()).map(args -> Arrays
                                .stream(args).filter(msg -> !ObjectUtils.isEmpty(msg))
                                .reduce("Please make sure to provide a valid request, ",
                                                (a, b) -> a + " " + b))
                                .orElse("Validation error").toString();
        }

        @ExceptionHandler(Exception.class)
        protected ResponseEntity<ProblemDetail> handleGenericException(Exception ex,
                        WebRequest request) {
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred. Please try again later.");
                problemDetail.setType(URI.create(
                                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/500"));
                problemDetail.setTitle("Internal Server Error");
                problemDetail.setProperty("timestamp", Instant.now());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        protected ResponseEntity<ProblemDetail> handleIllegalArgumentException(
                        IllegalArgumentException ex) {
                ProblemDetail problemDetail = ProblemDetail
                                .forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
                problemDetail.setType(URI.create(
                                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"));
                problemDetail.setTitle("Invalid Argument");
                problemDetail.setProperty("timestamp", Instant.now());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
        }

        @Override
        protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex,
                        HttpHeaders headers, HttpStatusCode status, WebRequest request) {

                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                                ex.getMessage());
                problemDetail.setType(URI.create(
                                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/404"));
                problemDetail.setTitle("Resource Not Found");
                problemDetail.setInstance(ex.getBody().getInstance());
                problemDetail.setProperty("timestamp", Instant.now());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
        }
}
