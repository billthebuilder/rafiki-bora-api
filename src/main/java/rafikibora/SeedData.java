
package rafikibora;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rafikibora.model.account.Account;
import rafikibora.model.terminal.Terminal;
import rafikibora.model.transactions.Transaction;
import rafikibora.model.users.Role;
import rafikibora.model.users.User;
import rafikibora.model.users.UserRoles;
import rafikibora.repository.*;

import java.util.Date;


/**
 * Adds seed data to the database for testing purposes
 */
@Transactional
@Component
@AllArgsConstructor
public class SeedData
        implements CommandLineRunner
{

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final TerminalRepository terminalRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // ################### ROLES ########################
        // Admin
        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");
        adminRole = roleRepository.save(adminRole);

        // Merchant
        Role merchantRole = new Role();
        merchantRole.setRoleName("MERCHANT");
        merchantRole = roleRepository.save(merchantRole);

        // Customer
        Role customerRole = new Role();
        customerRole.setRoleName("CUSTOMER");
        customerRole = roleRepository.save(customerRole);

        // Agent
        Role agentRole = new Role();
        agentRole.setRoleName("AGENT");
        agentRole = roleRepository.save(agentRole);


        // ####### USERS ###########
        // ## Admins
        User admin1 = new User();
        admin1.setFirstName("Admin0");
        admin1.setLastName("Admin0");
        admin1.setEmail("admin0@mail.com");
        admin1.setUsername("admin0@mail.com");
        admin1.setPhoneNo("0720000000");
        admin1.setPassword(passwordEncoder.encode("123456"));
        admin1.setStatus(true);
        admin1.getRoles().add(new UserRoles(admin1, adminRole));
        userRepository.save(admin1);

        User admin2 = new User();
        admin2.setFirstName("Admin1");
        admin2.setLastName("Admin1");
        admin2.setEmail("admin1@mail.com");
        admin2.setUsername("admin1@mail.com");
        admin2.setPhoneNo("0720000001");
        admin2.setPassword(passwordEncoder.encode("123456"));
        admin2.setStatus(true);
        admin2.getRoles().add(new UserRoles(admin2, adminRole));
        userRepository.save(admin2);

        User agent1 = new User();
        agent1.setFirstName("Agent0");
        agent1.setLastName("Agent0");
        agent1.setEmail("agent@rafiki.com");
        agent1.setUsername("agent@rafiki.com");
        agent1.setPhoneNo("0720942928");
        agent1.setPassword(passwordEncoder.encode("123456"));
        agent1.setStatus(true);
        agent1.getRoles().add(new UserRoles(agent1, agentRole));
        userRepository.save(agent1);

        // ## Customers
        User cust1 = new User();
        cust1.setFirstName("ANTHONY");
        cust1.setLastName("MUTHUMA");
        cust1.setEmail("anthony.muthuma@rafiki.com");
        cust1.setUsername("anthony.muthuma@rafiki.com");
        cust1.setPhoneNo("0714385058");
        cust1.setPassword(passwordEncoder.encode("password"));
        cust1.setStatus(true);
        cust1.getRoles().add(new UserRoles(cust1, customerRole));
        userRepository.save(cust1);

        User cust2 = new User();
        cust2.setFirstName("RUFUSY");
        cust2.setLastName("IDACHI");
        cust2.setEmail("rufusy.idachi@rafiki.com");
        cust2.setUsername("rufusy.idachi@rafiki.com");
        cust2.setPhoneNo("0714385059");
        cust2.setPassword(passwordEncoder.encode("password"));
        cust2.setStatus(true);
        cust2.getRoles().add(new UserRoles(cust2, customerRole));
        userRepository.save(cust2);

        // ## Merchants

        User merchant1 = new User();
        merchant1.setFirstName("BETTY");
        merchant1.setLastName("KIRII");
        merchant1.setEmail("betty.kirii@rafiki.com");
        merchant1.setUsername("betty.kirii@rafiki.com");
        merchant1.setBusinessName("BETTY'S SHOP");
        merchant1.setPhoneNo("0714385056");
        merchant1.setMid("123456789123456");
        merchant1.setPassword(passwordEncoder.encode("123456"));
        merchant1.setStatus(true);
        merchant1.getRoles().add(new UserRoles(merchant1, merchantRole));
        userRepository.save(merchant1);

        User merchant2 = new User();
        merchant2.setFirstName("BILL");
        merchant2.setLastName("BRANDON");
        merchant2.setEmail("bill.brandon@rafiki.com");
        merchant2.setUsername("bill.brandon@rafiki.com");
        merchant2.setBusinessName("BILL'S SHOP");
        merchant2.setPhoneNo("0714385057");
        merchant2.setMid("123456789123457");
        merchant2.setPassword(passwordEncoder.encode("123456"));
        merchant2.setStatus(true);
        merchant2.getRoles().add(new UserRoles(merchant2, merchantRole));
        userRepository.save(merchant2);

        // Agents
        User agent2 = new User();
        agent2.setFirstName("Agent 1");
        agent2.setLastName("Mobutu");
        agent2.setEmail("agent1@rafiki.com");
        agent2.setUsername("agent1@rafiki.com");
        agent2.setPhoneNo("0110942927");
        agent2.setPassword(passwordEncoder.encode("123456"));
        agent2.setStatus(true);
        agent2.getRoles().add(new UserRoles(agent2, agentRole));
        userRepository.save(agent2);

        User agent3 = new User();
        agent3.setFirstName("Agent 2");
        agent3.setLastName("Mobutu-2");
        agent3.setEmail("agent2@rafiki.com");
        agent3.setUsername("agent2@rafiki.com");
        agent3.setPhoneNo("0220942927");
        agent3.setPassword(passwordEncoder.encode("123456"));
        agent3.setStatus(true);
        agent3.getRoles().add(new UserRoles(agent3, agentRole));
        userRepository.save(agent3);

        //#################### ACCOUNTS ########################
        // Merchant accounts
        Account merchantAcc1 = new Account();
        merchantAcc1.setAccountNumber("0714385056");
        merchantAcc1.setBalance(100000);
        merchantAcc1.setName("BETTY'S SHOP");
        merchantAcc1.setStatus(true);
        merchantAcc1.setPan("5196010116643992");
        merchantAcc1.setAccountMaker(admin1);
        merchantAcc1.setAccountChecker(admin2);
        merchantAcc1.setPhoneNumber("0714385056");
        merchantAcc1.setUser(merchant1);
        accountRepository.save(merchantAcc1);

        Account merchantAcc2 = new Account();
        merchantAcc2.setAccountNumber("0714385057");
        merchantAcc2.setBalance(250000);
        merchantAcc2.setName("BILL'S SHOP");
        merchantAcc2.setStatus(true);
        merchantAcc2.setPan("4478150181885102");
        merchantAcc2.setUser(merchant2);
        merchantAcc2.setAccountMaker(admin1);
        merchantAcc2.setAccountChecker(admin2);
        merchantAcc2.setPhoneNumber("0714385057");
        accountRepository.save(merchantAcc2);

        // Customer accounts
        Account custAcc1 = new Account();
        custAcc1.setAccountNumber("0714385058");
        custAcc1.setName("ANTHONY MUTHUMA");
        custAcc1.setBalance(50000.0);
        custAcc1.setPhoneNumber("0714385058");
        custAcc1.setPan("4478150096571201");
        custAcc1.setAccountMaker(admin1);
        custAcc1.setAccountChecker(admin2);
        custAcc1.setUser(cust1);
        accountRepository.save(custAcc1);

        Account custAcc2 = new Account();
        custAcc2.setAccountNumber("0714385059");
        custAcc2.setName("RUFUSY IDACHI");
        custAcc2.setBalance(50000.0);
        custAcc2.setPhoneNumber("0714385059");
        custAcc2.setPan("5196010174673147");
        custAcc2.setUser(cust2);
        custAcc2.setAccountMaker(admin1);
        custAcc2.setAccountChecker(admin2);
        accountRepository.save(custAcc2);

        // ####################### TERMINALS #######################################

        Terminal terminal1 = new Terminal();
        terminal1.setTid("00000001");
        terminal1.setSerialNo("2006173003221017313880837");
        terminal1.setModelType("Move/2500");
        terminal1.setStatus(true);
        terminal1.setMid(merchant1); // assigned to merchant 1
        terminal1.setTerminalMaker(admin1);
        terminal1.setTerminalChecker(admin2);
        terminalRepository.save(terminal1);

        Terminal terminal2 = new Terminal();
        terminal2.setTid("00000002");
        terminal2.setSerialNo("2006173003221017313880838");
        terminal2.setModelType("Move/2500");
        terminal2.setStatus(true);
        terminal2.setMid(merchant1); // assigned to merchant 1
        terminal2.setTerminalMaker(admin1);
        terminal2.setTerminalChecker(admin2);
        terminalRepository.save(terminal2);

        Terminal terminal3 = new Terminal();
        terminal3.setTid("00000003");
        terminal3.setSerialNo("2006173003221017313880839");
        terminal3.setModelType("Move/2500");
        terminal3.setStatus(true);
        terminal3.setMid(merchant2); // assigned to merchant 2
        terminal3.setTerminalMaker(admin1);
        terminal3.setTerminalChecker(admin2);
        terminal3.setAgent(agent2);
        terminalRepository.save(terminal3);

        // ####################### TRANSACTIONS #######################################
        // send money transactions
        Transaction transaction1 = new Transaction();
        transaction1.setAmountTransaction(13000);
        transaction1.setCurrencyCode("040");
        transaction1.setDateTimeTransmission(new Date());
        transaction1.setPan("5196010116643992");
        transaction1.setProcessingCode("260000");
        transaction1.setRecipientEmail("mulungojohnpaul@gmail.com");
        transaction1.setToken("12345");
        transaction1.setTerminal(terminal1);
        transaction1.setReferenceNo(terminal1.getTid()+System.currentTimeMillis());
        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setAmountTransaction(1500);
        transaction2.setCurrencyCode("040");
        transaction2.setDateTimeTransmission(new Date());
        transaction2.setPan("5196010116643992");
        transaction2.setProcessingCode("260000");
        transaction2.setRecipientEmail("mulungojohnpaul@gmail.com");
        transaction2.setToken("12346");
        transaction2.setTerminal(terminal1);
        transaction2.setReferenceNo(terminal1.getTid()+System.currentTimeMillis());
        transactionRepository.save(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setAmountTransaction(1500);
        transaction3.setCurrencyCode("040");
        transaction3.setDateTimeTransmission(new Date());
        transaction3.setPan("5196010116643992");
        transaction3.setProcessingCode("260000");
        transaction3.setRecipientEmail("mulungojohnpaul@gmail.com");
        transaction3.setToken("12347");
        transaction3.setTerminal(terminal2);
        transaction3.setReferenceNo(terminal2.getTid()+System.currentTimeMillis());
        transactionRepository.save(transaction3);

        // receive money
        Transaction transaction4 = new Transaction();
        transaction4.setAmountTransaction(1500);
        transaction4.setCurrencyCode("040");
        transaction4.setDateTimeTransmission(new Date());
        transaction4.setPan("4478150181885102");
        transaction4.setProcessingCode("010000");
        transaction4.setTerminal(terminal3);
        transaction4.setReferenceNo(terminal3.getTid()+System.currentTimeMillis());
        transactionRepository.save(transaction4);

        Transaction transaction5 = new Transaction();
        transaction5.setAmountTransaction(15000);
        transaction5.setCurrencyCode("040");
        transaction5.setDateTimeTransmission(new Date());
        transaction5.setPan("4478150181885102");
        transaction5.setProcessingCode("010000");
        transaction5.setReferenceNo(terminal3.getTid()+System.currentTimeMillis());
        transaction5.setTerminal(terminal3);
        transactionRepository.save(transaction5);

        // sale
        Transaction transaction6 = new Transaction();
        transaction6.setAmountTransaction(15000);
        transaction6.setCurrencyCode("040");
        transaction6.setDateTimeTransmission(new Date());
        transaction6.setPan("5196010174673147");
        transaction6.setProcessingCode("000000");
        transaction6.setTerminal(terminal1);
        transaction6.setReferenceNo(terminal1.getTid()+System.currentTimeMillis());
        transactionRepository.save(transaction6);

        Transaction transaction7 = new Transaction();
        transaction7.setAmountTransaction(15050);
        transaction7.setCurrencyCode("040");
        transaction7.setDateTimeTransmission(new Date());
        transaction7.setPan("5196010174673147");
        transaction7.setProcessingCode("000000");
        transaction7.setTerminal(terminal3);
        transaction7.setReferenceNo(terminal3.getTid()+System.currentTimeMillis());
        transactionRepository.save(transaction7);

        // deposit
        Transaction transaction8 = new Transaction();
        transaction8.setAmountTransaction(15050);
        transaction8.setCurrencyCode("040");
        transaction8.setDateTimeTransmission(new Date());
        transaction8.setPan("4478150096571201");
        transaction8.setProcessingCode("210000");
        transaction8.setTerminal(terminal2);
        transaction8.setReferenceNo(terminal2.getTid()+System.currentTimeMillis());
        transactionRepository.save(transaction8);

        Transaction transaction9 = new Transaction();
        transaction9.setAmountTransaction(15050);
        transaction9.setCurrencyCode("040");
        transaction9.setDateTimeTransmission(new Date());
        transaction9.setPan("4478150096571201");
        transaction9.setProcessingCode("210000");
        transaction9.setTerminal(terminal1);
        transaction9.setReferenceNo(terminal1.getTid()+System.currentTimeMillis());
        transactionRepository.save(transaction9);


    }

}

