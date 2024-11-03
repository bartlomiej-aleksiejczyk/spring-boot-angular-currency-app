package com.example.currencyinfoapp.common;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        ProblemDetail problemDetail = handleValidationException(ex);
        return ResponseEntity.status(status.value()).body(problemDetail);
    }

    private ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        String details = getErrorsDetails(ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), details);
        problemDetail.setType(
                URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"));
        problemDetail.setTitle("Bad request");
        problemDetail.setInstance(ex.getBody().getInstance());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    private String getErrorsDetails(MethodArgumentNotValidException ex) {
        return Optional.ofNullable(ex.getDetailMessageArguments())
                .map(args -> Arrays.stream(args).filter(msg -> !ObjectUtils.isEmpty(msg)).reduce(
                        "Please make sure to provide a valid request, ", (a, b) -> a + " " + b))
                .orElse("").toString();
    }
}
