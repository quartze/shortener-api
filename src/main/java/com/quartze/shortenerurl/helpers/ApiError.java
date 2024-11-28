package com.quartze.shortenerurl.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    private HttpStatus status;
    private String message;
    private final long timestamp = System.currentTimeMillis();

    @Override
    public String toString() {
        return "{" +
                "\"message\": \"" + message + '\"' +
                ",\"status\": \"" + status + "\"" +
                ",\"timestamp\": " + timestamp +
                '}';
    }
}
