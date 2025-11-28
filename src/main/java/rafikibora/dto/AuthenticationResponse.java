package rafikibora.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Schema(name = "AuthenticationResponse", description = "Response returned after a login attempt, including JWT and user roles on success.")
public class AuthenticationResponse {
    @Schema(description = "Status of the authentication request", example = "SUCCESS")
    private responseStatus status;

    @Schema(description = "Human readable message describing the result", example = "Successful Login")
    private String message;

    @Schema(description = "JWT Bearer token to be used for subsequent requests", example = "eyJhbGciOiJIUzUxMiJ9...", accessMode = Schema.AccessMode.READ_ONLY)
    private String authToken;

    @Schema(description = "Authenticated user's email", example = "jane.doe@example.com")
    private String email;

    @Schema(description = "List of roles/authorities granted to the user", example = "[\"ADMIN\", \"MERCHANT\"]")
    private List<?> roles;

    public enum responseStatus{
        SUCCESS, FAILED
    }
}