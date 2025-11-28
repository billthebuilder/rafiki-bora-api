package rafikibora.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rafikibora.dto.ListTransactionDto;
import rafikibora.dto.SingleTransactionDto;
import rafikibora.dto.TransactionDto;
import rafikibora.model.transactions.Transaction;
import rafikibora.repository.TransactionRepository;
import rafikibora.services.TransactionService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/transactions/")
public class TransactionController {
    @Autowired
    private TransactionService service;

    @Autowired
    private TransactionRepository repository;

    /**
     * Get transaction by id
     * @param id
     * @param response
     * @throws IOException
     */
    @GetMapping("find/{id}")
    public void findTransactionById(@PathVariable String id, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNodes = mapper.createObjectNode();
        Transaction transaction = new Transaction();
        transaction = service.getTransactionById(Integer.parseInt(id));
        String data = "";
        if(transaction == null){
            jsonNodes.put("found", false);
            jsonNodes.put("msg", "No transaction found");
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNodes);
        }
        else{
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.buildTransactionJson(transaction).getTransaction());
        }
        response.getWriter().println(data);
    }


    /**
     * Get sum of amount of transactions by type
     * @return
     */

    @GetMapping("totals")
    public double totals() {
        return repository.totals();
    }

    /**
     * Get sum of amount of transactions by type
     * @param type
     * @return
     */

    @GetMapping(value = "sum/{type}")
    public Optional<Transaction> sum(@PathVariable String type, HttpServletResponse response) throws IOException{
        return repository.sum(this.getTransactionType(type));
    }

    /**
     * Get all transactions from an account
     * @param pan
     * @return
     */

    @GetMapping(value = "account/{pan}")
    public void accountTransactions(@PathVariable String pan, HttpServletResponse response) throws IOException {
        List<Transaction> transactions = service.getTransactionsByPan(pan);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNodes = mapper.createObjectNode();
        String data = "";

        if(transactions.isEmpty()){
            jsonNodes.put("found", false);
            jsonNodes.put("msg", "No transactions found for account: "+pan);
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNodes);
        } else{
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.buildTransactionListJson(transactions).getTransactions());
        }
        response.getWriter().println(data);
    }

    /**
     * get all transactions by type
     * @param type
     * @return
     */
    @GetMapping(value = "type/{type}")
    public void transactionsByType(@PathVariable String type, HttpServletResponse response) throws IOException {

        List<Transaction> transactions = service.getTransactionsByProcessingCode(this.getTransactionType(type));
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNodes = mapper.createObjectNode();
        String data = "";

        if(transactions.isEmpty()){
            jsonNodes.put("found", false);
            jsonNodes.put("msg", "No transactions found for type: "+type);
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNodes);
        } else{
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.buildTransactionListJson(transactions).getTransactions());
        }
        response.getWriter().println(data);
    }

    /**
     * Get all transactions done by all merchants' terminals
     * @param mid
     * @return
     */
    @GetMapping(value = "merchant/{mid}")
    public void merchantTransactions(@PathVariable String mid, HttpServletResponse response) throws IOException {
        List<Transaction> transactions =service.getMerchantTransactions(mid);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNodes = mapper.createObjectNode();
        String data = "";

        if(transactions.isEmpty()){
            jsonNodes.put("found", false);
            jsonNodes.put("msg", "No transactions found for merchant: "+mid);
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNodes);
        } else{
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.buildTransactionListJson(transactions).getTransactions());
        }
        response.getWriter().println(data);
    }

    /**
     * get all transactions initiated from a terminal
     * @param tid
     * @return
     */
    @GetMapping(value = "terminal/{tid}")
    public void terminalTransactions(@PathVariable String tid, HttpServletResponse response) throws IOException {
        List<Transaction> transactions =service.getTerminalTransactions(tid);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNodes = mapper.createObjectNode();
        String data = "";

        if(transactions.isEmpty()){
            jsonNodes.put("found", false);
            jsonNodes.put("msg", "No transactions found for terminal: "+tid);
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNodes);
        } else {
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.buildTransactionListJson(transactions).getTransactions());
        }
        response.getWriter().println(data);
    }

    /**
     * Get all transactions
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "all")
    public void allTransactions(HttpServletResponse response) throws IOException{
        List<Transaction> transactions = service.getAllTransactions();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNodes = mapper.createObjectNode();
        String data = "";

        if(transactions.isEmpty()){
            jsonNodes.put("found", false);
            jsonNodes.put("msg", "No transactions found");
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNodes);
        } else {
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.buildTransactionListJson(transactions).getTransactions());
        }
        response.getWriter().println(data);
    }

    /**
     * build transactions list json
     * @param transactions
     * @return
     */
    private ListTransactionDto buildTransactionListJson(List<Transaction> transactions){
        return new ListTransactionDto(transactions.stream().map(t -> new TransactionDto(t)).collect(Collectors.toList()));
    }

    /**
     * build transaction json
     * @param transaction
     * @return
     */
    private SingleTransactionDto buildTransactionJson(Transaction transaction){
        return new SingleTransactionDto(new TransactionDto(transaction));
    }

    /**
     * Transaction type
     * @param transType
     * @return
     */
    private String getTransactionType(String transType){
        String type = "";
        switch (transType){
            case "sale":
                type = "000000";
                break;
            case "receive_money":
                type = "010000";
                break;
            case "send_money":
                type = "260000";
                break;
            case "deposit":
                type = "210000";
                break;
            default:
                type = "UNKNOWN TRANSACTION TYPE";
                break;
        }
        return type;
    }
}
