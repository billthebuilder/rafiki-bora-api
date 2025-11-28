package rafikibora.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(name = "Response", description = "Generic API response wrapper with status and message.")
public class Response {
    @Schema(description = "Outcome status of the operation", example = "SUCCESS")
    private responseStatus status;

    @Schema(description = "Human readable message detailing the result", example = "User Deleted Successfully")
    private String message;
    
//    private String authToken;
//    private String email;

    public enum responseStatus{
        SUCCESS, FAILED
    }
}