package com.quartze.shortenerurl.helpers;

import com.quartze.shortenerurl.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError(
                        HttpStatus.BAD_REQUEST,
                        "Something went wrong"
                )
        );
    }

    @ExceptionHandler(BodyIsRequiredException.class)
    public ResponseEntity<ApiError> handleCurrentUrlIsMapped(BodyIsRequiredException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(ShortsUrlNonExistException.class)
    public ResponseEntity<ApiError> handleShortsUrlNonExist(ShortsUrlNonExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiError(
                        HttpStatus.NOT_FOUND,
                        "Short url does not exists with provided ID"
                )
        );
    }

    @ExceptionHandler(TokenIsInvalidException.class)
    public ResponseEntity<ApiError> handleShortsUrlNonExist(TokenIsInvalidException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiError(
                        HttpStatus.UNAUTHORIZED,
                        "Unauthorized."
                )
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleShortsUrlNonExist(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError(
                        HttpStatus.BAD_REQUEST,
                        "User already exists."
                )
        );
    }

    @ExceptionHandler(UserInvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleShortsUrlNonExist(UserInvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiError(
                        HttpStatus.UNAUTHORIZED,
                        "Email or password are wrong."
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleShortsUrlNonExist(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError(
                        HttpStatus.BAD_REQUEST,
                        Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()
                )
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiError> handleShortsUrlNonExist(MissingRequestHeaderException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError(
                        HttpStatus.BAD_REQUEST,
                        "Authorization headers are required."
                )
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleShortsUrlNonExist(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                new ApiError(
                        HttpStatus.METHOD_NOT_ALLOWED,
                        "Given method is not allowed"
                )
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError(
                        HttpStatus.BAD_REQUEST,
                        "Body is required!"
                )
        );
    }

    @ExceptionHandler(InvalidEmailForgotPassword.class)
    public ResponseEntity<ApiError> handleInvalidForgotPassword(InvalidEmailForgotPassword ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError(
                        HttpStatus.BAD_REQUEST,
                        "This email is not registered."
                )
        );
    }
}
