package rafikibora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rafikibora.model.account.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Integer> {
    Optional<Account> findByName(String name);
    Account findByAccountNumber(String accountNumber);
    Optional<Account> findByPan(String merchantPan);
}

