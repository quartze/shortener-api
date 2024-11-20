package com.quartze.shortenerurl.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNewUserBody {
    @Email(message = "Email must be properly formatted.")
    @NotNull
    private String email;

    @NotNull
    @Size(min = 8, max = 36)
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
            message = "Password must be 8-20 characters long, include at least one digit, one uppercase letter, one lowercase letter, and one special character, and must not contain spaces"
    )
    private String password;
}
