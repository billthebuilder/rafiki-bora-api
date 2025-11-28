package rafikibora.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "TerminalToAgentResponse", description = "Request to assign a terminal (by TID) to an agent identified by email under the current merchant.")
public class TerminalToAgentResponse {

    @Schema(description = "Agent's email receiving the terminal assignment", example = "agent@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "agent email id cannot be empty")
    private String agentEmail;

    @Schema(description = "Terminal Identifier (TID) to assign to the agent", example = "T1234567", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = " terminal id cannot be empty")
    private String tid;
}
