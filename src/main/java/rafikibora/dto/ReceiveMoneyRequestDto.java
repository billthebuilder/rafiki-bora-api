package rafikibora.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ReceiveMoneyRequest", description = "Payload sent by terminal/application to initiate a receive-money payout to a customer.")
public class ReceiveMoneyRequestDto {

    @Schema(description = "Customer's PAN or account identifier receiving funds",
            example = "472500******1122")
    private String pan;

    @Schema(description = "ISO8583 processing code indicating the receive-money operation",
            example = "260000")
    private String processingCode;

    @Schema(description = "Transaction amount to be received",
            example = "1500.00")
    private String txnAmount;

    @Schema(description = "Transmission date time when request is initiated (e.g., YYMMDDhhmmss)",
            example = "250101120000")
    private String transmissionDateTime;

    @Schema(description = "Terminal Identifier (TID) originating the request",
            example = "T1234567")
    private String tid;

    @Schema(description = "Merchant Identifier (MID) under which the terminal operates",
            example = "MID0011223344")
    private String mid;

    @Schema(description = "One-time token provided to the recipient to redeem the funds",
            example = "845392761")
    private String receiveMoneyToken;

    @Schema(description = "Transaction currency (ISO 4217)",
            example = "KES")
    private String currency;
}
