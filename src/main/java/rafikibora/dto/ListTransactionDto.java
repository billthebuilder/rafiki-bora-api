package rafikibora.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"found", "transactions"})
@Schema(name = "ListTransaction", description = "Wrapper for a list of transactions with a simple 'found' indicator.")
public class ListTransactionDto {
    private final List<TransactionDto> transactions;

    public ListTransactionDto(List<TransactionDto> transactions){
        this.transactions = transactions;
    }

    @JsonProperty("transactions")
    public List<TransactionDto> getTransactions() {
        return new ArrayList<>(transactions);
    }

    @JsonProperty("found")
    public boolean isFound(){
        return true;
    }
}
