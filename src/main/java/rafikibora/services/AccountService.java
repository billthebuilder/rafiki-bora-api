package rafikibora.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rafikibora.dto.Response;
import rafikibora.exceptions.ResourceNotFoundException;
import rafikibora.model.account.Account;
import rafikibora.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;



    public List<Account> getAccounts() {
        return repository.findAll();
    }

    public ResponseEntity<?> getAccountById(int id) {
        Response response;
        Optional<Account> optional = repository.findById(id);
        Account account = null;
        if (optional.isPresent()) {
            account = optional.get();
        } else {
            response = new Response(Response.responseStatus.FAILED," Account not found for id :: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    public ResponseEntity<?> getAccountByName(String name) {
        Response response;
        Optional <Account> optional = repository.findByName(name);
        Account account = null;
        if (optional.isPresent()) {
            account = optional.get();
            //response = new Response(Response.responseStatus.SUCCESS,"Successful account");
        } else {
//            throw new RuntimeException(" Account not found for name :: " + name);
            response = new Response(Response.responseStatus.FAILED,"No account found for :: " + name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @Transactional
    public ResponseEntity<?> deleteAccount(int id) {
        if ( repository.findById(id).isPresent()) {
            repository.deleteById(id);
            // return "account disabled";
            return new ResponseEntity("Account Deleted", HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Account " + id + " Not Found");
        }
    }

    @Transactional
    public Account updateAccount(Account account, int accountid) {
        Account existingAccount = repository.findById(accountid).
                orElseThrow(
                        () -> new ResourceNotFoundException
                                ("Account " + accountid + " Not Found"));

        if (account.getName() != null) {
            existingAccount.setName(account.getName());
        }

        if ((String) account.getPan() != null) {
            existingAccount.setPan(account.getPan());
        }

        if ((Double) account.getBalance() != null) {
            existingAccount.setBalance(account.getBalance());
        }

        return repository.save(existingAccount);
    }


}
