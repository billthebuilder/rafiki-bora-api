package rafikibora.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


 @Data
 @Schema(name = "LoginRequest", description = "Credentials required to authenticate and obtain a JWT access token.")
 public class LoginRequest {
     @Schema(description = "User's email address used as the username", example = "jane.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
     @NotBlank(message = "Email address cannot be empty")
     @Email(message = "Please provide valid email address")
     private String email;

     @Schema(description = "User's password", example = "P@ssw0rd!", accessMode = Schema.AccessMode.WRITE_ONLY, requiredMode = Schema.RequiredMode.REQUIRED)
     @NotBlank(message = "Password cannot be empty")
     private String password;
 }
