package com.quartze.shortenerurl.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginToUserBody {
    @Email(message = "Email must be properly formatted.")
    @NotNull(message = "email is required.")
    private String email;

    @NotNull(message = "password is required.")
    private String password;
}
