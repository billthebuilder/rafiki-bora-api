package rafikibora.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TerminalAssignmentRequest {
    @NotBlank(message = "user email cannot be empty")
    private String email;

    @NotBlank(message = "terminal tid cannot be empty")
    private String tid;
}
