package rafikibora.services;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rafikibora.dto.*;
import rafikibora.exceptions.AddNewUserException;
import rafikibora.exceptions.BadRequestException;
import rafikibora.exceptions.InvalidCheckerException;
import rafikibora.exceptions.ResourceNotFoundException;
import rafikibora.model.account.Account;
import rafikibora.model.terminal.Terminal;
import rafikibora.model.users.Role;
import rafikibora.model.users.User;
import rafikibora.model.users.UserRoles;
import rafikibora.repository.AccountRepository;
import rafikibora.repository.RoleRepository;
import rafikibora.repository.TerminalRepository;
import rafikibora.repository.UserRepository;
import rafikibora.security.util.exceptions.RafikiBoraException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService implements UserServiceI {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TerminalRepository terminalRepository;
    private final RoleRepository roleRepository;
    private final JwtProviderI jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    //it is where we store details of the present security context of the application
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = this.findByName(authentication.getName());
        return user;
    }

    @Override
    public ResponseEntity<AuthenticationResponse> login(LoginRequest loginRequest) {
        AuthenticationResponse authResponse;
        try {
            authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (Exception ex) {
            authResponse = new AuthenticationResponse(AuthenticationResponse.responseStatus.FAILED, ex.getMessage(), null, null, null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        List<?> userRoles = userDetails.getAuthorities().stream().map(s ->
                new SimpleGrantedAuthority(s.getAuthority())).
                filter(Objects::nonNull).
                collect(Collectors.toList());
        String token = jwtProvider.generateToken(userDetails);
        boolean validateToken = jwtProvider.validateToken(token);
        if (!validateToken) {
            jwtProvider.generateToken(userDetails);
        }
        authResponse = new AuthenticationResponse(AuthenticationResponse.responseStatus.SUCCESS, "Successful Login", token, loginRequest.getEmail(), userRoles);
        return ResponseEntity.ok().body(authResponse);
    }

    //test validity of credentials
    private void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new RafikiBoraException("User is Disabled");
        } catch (BadCredentialsException e) {
            throw new RafikiBoraException("Invalid Credentials");
        }
    }

    //find user by Id
    public ResponseEntity<?> getUserById(int id) {
        Response response;
        Optional<User> optional = Optional.ofNullable(userRepository.findById(id));
        User user = null;
        if (optional.isPresent()){
            user = optional.get();
        } else {
            response = new Response(Response.responseStatus.FAILED," User not found for id :: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    //soft delete user
    @Transactional
    public ResponseEntity<?> deleteUser(long id) {
        Response response;
        User user = userRepository.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User does not exist");
        }
        userRepository.delete(user);
        response = new Response(Response.responseStatus.SUCCESS, "User Deleted Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //find user by name
    @Override
    public User findByName(String name) {
        User user = userRepository.findByEmail(name.toLowerCase());
        if (user == null) {
            throw new ResourceNotFoundException("User with email " + name + " not found!");
        }
        return user;
    }

    //list users of specific roles
    @Override
    public List<User> getUserByRole(String roleName) {

        List<User> users = userRepository.findByRoles_Role_RoleNameContainingIgnoreCase(roleName);

        return users;
    }

    //list user by id
    public User getUserById(long id) {

        User user = userRepository.findById(id);
        log.info("user:",user);
        return user;
    }

    //list all users
    @Override
    public List<User> viewUsers() {
        return userRepository.findAll();
    }

    //Setting user Maker
    @Transactional
    public void addUser(User user) {
       Account account = new Account();
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EntityExistsException("Email already exists");
        }
        User currentUser = getCurrentUser();
        user.setUserMaker(currentUser);

        Role role = null;
        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            role = roleRepository.findByRoleName("ADMIN");
        }
        if (user.getRole().equalsIgnoreCase("MERCHANT")) {
            role = roleRepository.findByRoleName("MERCHANT");
            String mid = UUID.randomUUID().toString().substring(0, 16);
            user.setMid(mid);
        }
        if (user.getRole().equalsIgnoreCase("CUSTOMER")) {
            role = roleRepository.findByRoleName("CUSTOMER");
        }

        try {
            if(!user.getRole().equalsIgnoreCase("ADMIN")&&!user.getRole().equalsIgnoreCase("AGENT")) {
                account.setAccountMaker(user.getUserMaker());
                account.setAccountNumber(user.getPhoneNo());
                account.setName(user.getFirstName() + "  " + user.getLastName());
                account.setUser(user);
                account.setPhoneNumber(user.getPhoneNo());
                accountRepository.save(account);
            }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.getRoles().add(new UserRoles(user, role));
    userRepository.save(user);

}catch (Exception ex){
            throw new AddNewUserException(ex.getMessage());
}



    }

    //Setting user Checker
    @Transactional
    public User approveUser(String email) {
        User currentUser = getCurrentUser();
        User user = userRepository.findByEmail(email);
        Account account=accountRepository.findByAccountNumber(user.getPhoneNo());

        if (user == null) {
            throw new ResourceNotFoundException("This user does not exist");
        }
        // A user cannot be approved by the same Admin who created them
        if (currentUser == user.getUserMaker()) {
            throw new InvalidCheckerException("You cannot approve this user!");
        }
        if(account.getUser()==user&& account.getAccountMaker()!=currentUser){
            account.setAccountChecker(currentUser);
            account.setStatus(true);
            accountRepository.save(account);
        }


        user.setUserChecker(currentUser);
        user.setStatus(true);
        return userRepository.save(user);
    }

    //Make merchant On board their Agents
    @Override
    public void addAgent(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EntityExistsException("Email already exists");
        }
        User currentUser = getCurrentUser();
        Role role = null;
        if (user.getRole().equalsIgnoreCase("AGENT")) {
            role = roleRepository.findByRoleName("AGENT");
        }
        Set<UserRoles> retrievedRoles = currentUser.getRoles();

        for (UserRoles userRole : retrievedRoles) {
            if (userRole.getRole().getRoleName().equalsIgnoreCase("MERCHANT")) {

                //user.setStatus(true);
                user.getRoles().add(new UserRoles(user, role));
                user.setUserMaker(currentUser);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);

            }
        }

    }

    //allow user to update their information
    public User updateUser(User user, int userid) {
        User existinguser = getCurrentUser();
        if (existinguser == null) {
            UserService.log.error("User " + userid + " Not Found");
        }

        if (user.getEmail() != null) {
            existinguser.setEmail(user.getEmail());
        }

        if (user.getPhoneNo() != null) {
            existinguser.setPhoneNo(user.getPhoneNo());
        }

        if (user.getFirstName() != null) {
            existinguser.setFirstName(user.getFirstName());
        }

        if (user.getLastName() != null) {
            existinguser.setLastName(user.getLastName());
        }

        if (user.getPassword() != null) {
            existinguser.setPassword(passwordEncoder.encode(user.getPassword()));
        }


        return userRepository.save(existinguser);
    }

    //assign terminals to Merchants
    public void assignTerminals(TerminalAssignmentRequest terminalAssignmentRequest) {

        String merchantEmail = terminalAssignmentRequest.getEmail();
        String terminalTid = terminalAssignmentRequest.getTid();

        User merchant = userRepository.findByEmail(merchantEmail);
        Optional<Terminal> terminal = terminalRepository.findByTid(terminalTid);

        try {
            Terminal t = terminal.get();
            t.setMid(merchant);
            terminalRepository.save(t);

        } catch (Exception ex) {
            log.error("Error assigning terminals: " + ex.getMessage());
            throw ex;
        }

    }

    //assign terminals from merchants to agents
    public void assignTerminalsToAgent(TerminalToAgentResponse terminalToAgentResponse) {
        User merchant = getCurrentUser();
        User agent = null;

        String agentEmail = terminalToAgentResponse.getAgentEmail();
        String terminalId = terminalToAgentResponse.getTid();

        agent = userRepository.findByEmail(agentEmail);
        Optional<Terminal> terminal = terminalRepository.findByTid(terminalId);
        Terminal t = terminal.get();
        try {

            // check that both agent and terminal belong to this merchant
            // An agent is made by a merchant, so check that the maker of this agent
            // corresponds to the merchant whose terminal we're assigning.
            if (merchant == agent.getUserMaker() && merchant == t.getMid()) {
                // Add this terminal to the list of assigned terminals
                // for this agent
                t.setAgent(agent);
              terminalRepository.save(t);

            } else {
                log.info("Invalid credentials to perform this actions");
                throw new BadRequestException("Invalid credentials to perform this actions");
            }
        } catch (Exception er) {
            log.error("Error assigning terminals: " + er.getMessage());
            throw new BadRequestException("Error assigning terminals: " + er.getMessage());
        }
    }

    //list all agents belonging to a merchant but not assigned to any terminal
    public List<User> unAssignedAgents(){
        User merchant = getCurrentUser();
       return  userRepository.findByUserMakerAndAssignedTerminalsIsNull(merchant);

    }


}
