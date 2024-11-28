package com.quartze.shortenerurl.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNewShortUrlsBody {
    @NotNull(message = "original_url is required.")
    @Pattern(
            regexp = "^https://([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(:[0-9]{1,5})?(/.*)?$",
            message = "URL must be a valid HTTPS URL"
    )
    private String original_url;

    private Boolean is_nsfw = false;
}
