package pl.kubaretip.authservice.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kubaretip.authservice.security.model.AuthRequestModel;
import pl.kubaretip.authservice.security.model.TokenResponse;

/**
 * Authentication API spec for Swagger doc
 * Implemented and automatically overridden by Spring Security filters.
 * Request mapping value coming from JWTConfig class
 *
 * @see pl.kubaretip.authutils.jwt.JWTConfig
 */
@Tag(name = "Authorization", description = "Login endpoint")
@RequestMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AuthorizationController {

    @Operation(summary = "Login", description = "Generating JWT tokens", tags = {"Authorization"})
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "JWT token",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TokenResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public void fakeLogin(@Parameter(description = "Username and password",
            required = true, schema = @Schema(implementation = AuthRequestModel.class))
                          @RequestBody AuthRequestModel authRequestModel) {
        throw new IllegalStateException("Should not be called ! Implemented by Spring Security filters.");
    }
}
