package rafikibora.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import rafikibora.model.users.Role;

import java.util.Set;

@Data
@Schema(name = "UserSummary", description = "Brief summary of a user with key identifiers and roles.")
public class UserSummary {
    @Schema(description = "Unique identifier of the user", example = "42")
    private Long userId;

    @Schema(description = "Email address of the user", example = "jane.doe@example.com")
    private String email;

    @Schema(description = "Set of roles assigned to the user")
    private Set<Role> roles;
}
