package rafikibora.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "TerminalAssignmentRequest", description = "Request to assign a terminal (by TID) to a merchant identified by email.")
public class TerminalAssignmentRequest {
    @Schema(description = "Merchant's email that uniquely identifies the merchant", example = "merchant@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "user email cannot be empty")
    private String email;

    @Schema(description = "Terminal Identifier (TID) to be assigned to the merchant", example = "T1234567", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "terminal tid cannot be empty")
    private String tid;
}
