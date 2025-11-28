package rafikibora.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TerminalToAgentResponse {

    @NotBlank(message = "agent email id cannot be empty")
    private String agentEmail;

    @NotBlank(message = " terminal id cannot be empty")
    private String tid;
}
