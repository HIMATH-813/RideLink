package com.ridelink.ride_service.exception;

import java.time.Instant;
import java.util.stream.Collectors;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.ridelink.ride_service.model.RideStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RideNotFoundException.class)
    public ProblemDetail handleRideNotFound(RideNotFoundException exception, WebRequest request) {
        return problem(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler({NoAvailableDriverException.class, InvalidStateTransitionException.class})
    public ProblemDetail handleConflict(RuntimeException exception, WebRequest request) {
        return problem(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception, WebRequest request) {
        String detail = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return problem(HttpStatus.BAD_REQUEST, detail, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleBadRequest(IllegalArgumentException exception, WebRequest request) {
        return problem(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(FeignException.class)
    public ProblemDetail handleDriverServiceFailure(FeignException exception, WebRequest request) {
        return problem(HttpStatus.BAD_GATEWAY, "Driver Service request failed", request);
    }

    private ProblemDetail problem(HttpStatus status, String detail, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(status.getReasonPhrase());
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("path", request.getDescription(false).replace("uri=", ""));
        return problem;
    }
}
