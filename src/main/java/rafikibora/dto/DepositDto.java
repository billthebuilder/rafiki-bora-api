package rafikibora.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties
@Schema(name = "Deposit", description = "Payload for performing a deposit transaction from a customer's account to a merchant.")
public class DepositDto {

    @Schema(description = "Merchant's Primary Account Number (PAN) or account identifier receiving the funds",
            example = "543212******7890")
    private String merchantPan;

    @Schema(description = "Customer's PAN or source account identifier",
            example = "472500******1122")
    private String customerPan;

    @Schema(description = "Amount to deposit in the smallest currency unit or decimal amount depending on integration",
            example = "1000.00")
    private String amountTransaction;

    @Schema(description = "Transmission date and time of the transaction (ISO-like or YYMMDDhhmmss as agreed)",
            example = "250101120000")
    private String DateTimeTransmission;

    @Schema(description = "Terminal Identifier (TID) where the transaction originates",
            example = "T1234567")
    private String terminal;

    @Schema(description = "Merchant Identifier (MID) associated with the merchant",
            example = "MID0011223344")
    private String merchant;

    @Schema(description = "Currency code for the transaction (ISO 4217 numeric or alpha)",
            example = "KES")
    private String currencyCode;

    @Schema(description = "ISO8583 processing code for the transaction",
            example = "210000")
    private String processingCode;
}

