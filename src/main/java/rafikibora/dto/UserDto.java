package rafikibora.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
@Schema(name = "User", description = "Payload for creating or updating a user. Includes identity, contact and role information.")
public class UserDto {

    @Schema(description = "User's first name", example = "Jane")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "Unique email used as username for login", example = "jane.doe@example.com")
    private String email;

    @Schema(description = "Unique username for login", example = "jane.doe")
    private String userName;

    @Schema(description = "User's phone number (used as account number in some flows)", example = "0712345678")
    private String phoneNo;

    @Schema(description = "Raw password to be encoded by the server on creation/update", example = "P@ssw0rd!", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Schema(description = "Role to assign to the user (e.g., ADMIN, MERCHANT, CUSTOMER, AGENT)", example = "MERCHANT")
    private String role;

}
