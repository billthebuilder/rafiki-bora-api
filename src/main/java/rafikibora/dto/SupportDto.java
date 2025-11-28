package rafikibora.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Support", description = "Support request details submitted by a user.")
public class SupportDto {
        @Schema(description = "Name of the requester", example = "Jane Doe")
        String name;
        @Schema(description = "Identifier related to the support context (e.g., user id, ticket id)", example = "42")
        String id;
        @Schema(description = "Brief description of the reason for support", example = "Unable to log in with my credentials")
        String reason;
    }

