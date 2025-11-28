package rafikibora.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ReceiveMoneyResponse", description = "Response returned after attempting a receive-money transaction.")
public class ReceiveMoneyResponseDto {
    @Schema(description = "Response code or textual message indicating the result (e.g., '00' for success, '96' for system error)",
            example = "00")
    private String message;

    @Schema(description = "Amount that was processed for the transaction",
            example = "1500.00")
    private String txnAmount;
}
