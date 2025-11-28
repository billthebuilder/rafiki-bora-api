package rafikibora.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({"found", "transaction"})
@Schema(name = "SingleTransaction", description = "Wrapper for a single transaction with a 'found' indicator.")
public class SingleTransactionDto {
    private final TransactionDto transaction;

    public SingleTransactionDto(TransactionDto transaction){
        this.transaction= transaction;
    }

    @JsonProperty("transaction")
    public TransactionDto getTransaction() {
        return transaction;
    }
    @JsonProperty("found")
    public boolean isFound(){
        return true;
    }
}
