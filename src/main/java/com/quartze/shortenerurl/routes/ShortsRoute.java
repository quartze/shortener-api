package com.quartze.shortenerurl.routes;

import com.quartze.shortenerurl.exceptions.BodyIsRequiredException;
import com.quartze.shortenerurl.helpers.ApiError;
import com.quartze.shortenerurl.models.ShortUrls;
import com.quartze.shortenerurl.requests.CreateNewShortUrlsBody;
import com.quartze.shortenerurl.services.ShortUrlsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shorts")
@Tag(name = "Short Urls")
public class ShortsRoute {

    private final ShortUrlsService shortUrlService;

    @Autowired
    public ShortsRoute(ShortUrlsService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping
    @Operation(
            summary = "Get short urls from current logged user",
            tags = {"Short Urls"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get ShortUrls from User"),
                    @ApiResponse(responseCode = "400", description = "Authorization headers are required", content = @Content(
                            schema = @Schema(implementation = ApiError.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(
                            schema = @Schema(implementation = ApiError.class)
                    )),
                    @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content(
                            schema = @Schema(implementation = ApiError.class)
                    ))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Page<ShortUrls>> getShortsFromUser(
            @Parameter(allowEmptyValue = true) @RequestParam(defaultValue = "1") int page,
            @Parameter(allowEmptyValue = true) @RequestParam(defaultValue = "12") int limit
    ) {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        PageRequest pageable = PageRequest.of(page-1, limit, Sort.by(Sort.Order.desc("createdAt")));

        Page<ShortUrls> shorts = shortUrlService.getShortUrlsByUser(userId, pageable);

        return ResponseEntity.ok(shorts);
    }

    @PostMapping
    @Operation(
            summary = "Add new anonymous or user short url", tags = {"Short Urls"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully created short url"),
                    @ApiResponse(responseCode = "400", description = "Something went wrong", content = @Content(
                            schema = @Schema(implementation = ApiError.class)
                    ))
            }
    )
    public ResponseEntity<ShortUrls> createShortsUrl(@Valid @RequestBody CreateNewShortUrlsBody body) {
        Long userId = null;
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!user.equals("anonymousUser")) userId = Long.parseLong(user);

        if (body.getOriginal_url() == null) throw new BodyIsRequiredException("Fields `original_url` are required!");

        ShortUrls url = shortUrlService.createNewShortsUrl(body.getOriginal_url(), body.getIs_nsfw(), userId);

        return ResponseEntity.ok(url);
    }

    @GetMapping("/{shortId}")
    @Operation(
            summary = "Get info about short url", tags = {"Short Urls"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully get info"),
                    @ApiResponse(responseCode = "400", description = "Something went wrong", content = @Content(
                            schema = @Schema(implementation = ApiError.class)
                    ))
            }
    )
    public ResponseEntity<ShortUrls> getShortsUrl(@PathVariable String shortId) {
        ShortUrls shorts = shortUrlService.getShortUrl(shortId);

        return ResponseEntity.ok(shorts);
    }

    @DeleteMapping("/{shortId}")
    @Operation(
            summary = "Remove short url from user", tags = {"Short Urls"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted short url"),
                    @ApiResponse(responseCode = "400", description = "Token is invalid or Token is expired or Something went wrong", content = @Content(
                            schema = @Schema(implementation = ApiError.class)
                    ))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Boolean> deleteShortsUrl(
            @PathVariable String shortId) {
        shortUrlService.deleteShortUrl(shortId);
        return ResponseEntity.ok(true);
    }
}
