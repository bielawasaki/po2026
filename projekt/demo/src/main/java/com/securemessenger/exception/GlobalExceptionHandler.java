package com.securemessenger.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request
    ) {

        int status = ex.getStatusCode().value();

        HttpStatus httpStatus = HttpStatus.resolve(status);

        String error = httpStatus != null
                ? httpStatus.getReasonPhrase()
                : "HTTP Error";

        String message = ex.getReason() != null
                ? ex.getReason()
                : "Request failed";

        ApiError response = new ApiError(
                status,
                error,
                message,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(
        org.springframework.web.multipart.MaxUploadSizeExceededException.class
    )
    public ResponseEntity<ApiError> handleMaxUploadSize(
            HttpServletRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CONTENT_TOO_LARGE)
                .body(new ApiError(
                        413,
                        "Content Too Large",
                        "Avatar file exceeds the allowed size",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(
        org.springframework.web.bind.MissingServletRequestParameterException.class
    )
    public ResponseEntity<ApiError> handleMissingParameter(
            org.springframework.web.bind.MissingServletRequestParameterException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity
                .badRequest()
                .body(new ApiError(
                        400,
                        "Bad Request",
                        "Missing required parameter: " + ex.getParameterName(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(
        org.springframework.web.multipart.support.MissingServletRequestPartException.class
    )
    public ResponseEntity<ApiError> handleMissingPart(
            org.springframework.web.multipart.support.MissingServletRequestPartException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.badRequest()
                .body(new ApiError(
                        400,
                        "Bad Request",
                        "Missing required file: " + ex.getRequestPartName(),
                        request.getRequestURI()
                ));
    }
}