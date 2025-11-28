package rafikibora.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import rafikibora.model.transactions.Transaction;

@Schema(name = "Transaction", description = "Summary view of a transaction as exposed by the API.")
public class TransactionDto {
    @Schema(description = "Transaction identifier", example = "1024")
    private final String id;
    @Schema(description = "Primary Account Number (PAN) used in the transaction",
            example = "472500******1122")
    private final String pan;
    @Schema(description = "Transaction currency (ISO 4217)", example = "KES")
    private final String currencyCode;
    @Schema(description = "Transaction amount", example = "1000.00")
    private final String amount;
    @Schema(description = "Transaction type derived from processing code", example = "SALE")
    private final String type;
    @Schema(description = "Transaction date formatted for display", example = "2025-01-01 12:00:00")
    private final String date;
    @Schema(description = "Reference number for the transaction", example = "T12345671701234567")
    private final String referenceNo;

    public TransactionDto(Transaction transaction){
        this.id = String.valueOf(transaction.getId());
        this.pan = transaction.getPan();
        this.currencyCode = transaction.getCurrencyCode();
        this.amount = String.valueOf(transaction.getAmountTransaction());
        this.type = transaction.getTransactionType(transaction.getProcessingCode());
        this.date = transaction.getTransactionDate(transaction.getDateTimeTransmission());
        this.referenceNo = transaction.getReferenceNo();
    }

    public String getId() {
        return id;
    }

    public String getPan() {
        return pan;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getReferenceNo(){
        return referenceNo;
    }
}
