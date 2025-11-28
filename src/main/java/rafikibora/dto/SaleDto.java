package rafikibora.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties
@Schema(name = "Sale", description = "Payload for performing a sale (purchase) transaction at a terminal for a merchant.")
public class SaleDto {

    @Schema(description = "Card Primary Account Number (PAN) used for the sale",
            example = "472500******1122")
    private String pan;

    @Schema(description = "Amount of the transaction in decimal or smallest currency unit as agreed",
            example = "500.00")
    private String amountTransaction;

    @Schema(description = "Transmission date time of the transaction (e.g., YYMMDDhhmmss)",
            example = "250101120000")
    private String transmissionDateTime;

    @Schema(description = "Terminal Identifier (TID) where the sale occurs",
            example = "T1234567")
    private String terminal;

    @Schema(description = "Merchant Identifier (MID) associated with the terminal",
            example = "MID0011223344")
    private String merchant;

    @Schema(description = "Currency code for the transaction (ISO 4217)",
            example = "KES")
    private String currencyCode;

    @Schema(description = "ISO8583 processing code indicating a sale",
            example = "000000")
    private String processingCode;
}
