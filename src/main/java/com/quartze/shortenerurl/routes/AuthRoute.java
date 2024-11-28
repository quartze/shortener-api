package com.quartze.shortenerurl.routes;

import com.quartze.shortenerurl.exceptions.BodyIsRequiredException;
import com.quartze.shortenerurl.exceptions.TokenIsInvalidException;
import com.quartze.shortenerurl.helpers.ApiError;
import com.quartze.shortenerurl.models.Auth;
import com.quartze.shortenerurl.models.User;
import com.quartze.shortenerurl.requests.CreateNewUserBody;
import com.quartze.shortenerurl.requests.LoginToUserBody;
import com.quartze.shortenerurl.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@Tag(name = "Auth")
public class AuthRoute {

    private final UserService userService;

    @Autowired
    public AuthRoute(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    @Operation(
            summary = "Login into account",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get Authorization Tokens"),
                    @ApiResponse(responseCode = "400", description = "Body fields required or Email or password are invalid", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )),
                    @ApiResponse(responseCode = "500", description = "Something went wrong",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            ))
            }
    )
    public ResponseEntity<Auth> loginAccount(@Valid @RequestBody LoginToUserBody body) {
        if (body.getEmail() == null || body.getPassword() == null)
            throw new BodyIsRequiredException("Fields `email`, `password` are required!");

        return ResponseEntity.ok(userService.login(body.getEmail(), body.getPassword()));
    }

    @PostMapping("/auth/register")
    @Operation(
            summary = "Register new account",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get Authorization Tokens"),
                    @ApiResponse(responseCode = "400", description = "Body fields are required or User already exists", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )),
                    @ApiResponse(responseCode = "500", description = "Something went wrong",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            ))
            }
    )
    public ResponseEntity<Auth> registerAccount(@Valid @RequestBody CreateNewUserBody body) {
        if (body.getEmail() == null || body.getPassword() == null)
            throw new BodyIsRequiredException("Fields `email`, `password` are required!");

        return ResponseEntity.ok(userService.register(body.getEmail(), body.getPassword()));
    }

    @PostMapping("/auth/refresh")
    @Operation(
            summary = "Refresh Authorization token",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get Authorization Tokens"),
                    @ApiResponse(responseCode = "400", description = "Token is invalid or token is expired.", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )),
                    @ApiResponse(responseCode = "500", description = "Something went wrong",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            ))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Auth> refreshAccount(@Parameter(hidden = true) @RequestHeader("refresh") String token) {
        if (token == null) throw new TokenIsInvalidException();

        token = token.replace("Bearer ", "");

        return ResponseEntity.ok(userService.refreshToken(token));
    }

    @GetMapping("/auth/me")
    @Operation(
            summary = "User information",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "User informations"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Token is expired.", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )),
                    @ApiResponse(responseCode = "500", description = "Something went wrong",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            ))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<User> getMeInfo() {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userService.getUser(userId);

        return ResponseEntity.ok(user);
    }
}
