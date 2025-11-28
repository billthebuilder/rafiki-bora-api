package rafikibora.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Account", description = "Basic account details associated with a user.")
public class AccountDto {
    @Schema(description = "Account number uniquely identifying the account", example = "0712345678")
    String accountNumber;

    @Schema(description = "Account holder's display name", example = "Jane Doe")
    String name;

    @Schema(description = "Primary Account Number (PAN) linked to the account if applicable",
            example = "543212******7890")
    String pan;
}
