package rafikibora.services;

import org.springframework.http.ResponseEntity;
import rafikibora.dto.AuthenticationResponse;
import rafikibora.dto.LoginRequest;
import rafikibora.dto.TerminalAssignmentRequest;
import rafikibora.dto.TerminalToAgentResponse;
import rafikibora.model.users.User;

import java.util.List;

public interface UserServiceI {

    ResponseEntity<AuthenticationResponse> login(LoginRequest loginRequest) throws Exception;

    List<User> getUserByRole(String roleName);

    User findByName(String name);

    ResponseEntity<?> deleteUser(long id);

    List<User> viewUsers();

    void addAgent(User user);

    void  addUser(User user);

    User approveUser(String email);

     User updateUser(User user, int userid);

   void  assignTerminals(TerminalAssignmentRequest terminalAssignmentRequest);

   void assignTerminalsToAgent (TerminalToAgentResponse terminalToAgentResponse);

}
