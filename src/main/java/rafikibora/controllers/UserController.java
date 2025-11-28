package rafikibora.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rafikibora.dto.ListSystemUser;
import rafikibora.dto.SystemUser;
import rafikibora.dto.TerminalAssignmentRequest;
import rafikibora.dto.TerminalToAgentResponse;
import rafikibora.exceptions.BadRequestException;
import rafikibora.model.users.User;
import rafikibora.services.UserService;
import rafikibora.services.UserServiceI;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/users")
@Slf4j
@Tag(name = "Users", description = "Operations related to user management")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    @Autowired
    private UserServiceI userServiceI;

    @Autowired
    private UserService userService;

    @PostMapping("/createuser")
    @Operation(summary = "Create user", description = "Create a new system user with a role assigned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Missing role or invalid request",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> addUser(@RequestBody @Parameter(description = "User payload") User user){
        if(user.getRole() == null)
            throw new BadRequestException("User has to have an assigned role");
        userServiceI.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Returns the User record for the currently authenticated user based off of the supplied access token
     * <br>Example: <a href="http://localhost:8080/users/profile">http://localhost:2019/users/getuserinfo</a>
     *
     * @param authentication The authenticated user object provided by Spring Security
     * @return JSON of the current user. Status of OK
     * @see UserService#findByName(String) UserService.findByName(authenticated user)
     */
    @GetMapping(value = "/user/profile",produces = {"application/json"})
    @Operation(summary = "Get current user profile", description = "Returns the profile of the authenticated user")
    public ResponseEntity<?> getCurrentUserInfo(Authentication authentication) {
        User user = userServiceI.findByName(authentication.getName());
        return new ResponseEntity<>(user,
                HttpStatus.OK);
    }

    @PostMapping("/user/approve/{email}")
    @Operation(summary = "Approve user", description = "Approve a user account by email")
    public ResponseEntity<?> approve(@PathVariable("email") @Parameter(description = "User email") String email){

        User approvedUser = userServiceI.approveUser(email);

        return new ResponseEntity<>(approvedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user by id")
    public ResponseEntity<?> deleteUser(@PathVariable @Param("id") @Parameter(description = "User id") long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/{roleName}")
    @Operation(summary = "Find users by role", description = "List users that have the specified role name")
    public void findUserByRoles(@PathVariable("roleName") @Parameter(description = "Role name") String roleName, HttpServletResponse response) throws IOException {
        List<User> users = userServiceI.getUserByRole(roleName);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNodes = mapper.createObjectNode();
        String data = "";

        if(users.isEmpty()){
            jsonNodes.put("found", false);
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNodes);
        } else{
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.buildUserListJson(users).getSystemUsers());
        }
        response.getWriter().println(data);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List users", description = "Returns all users")
    public void findAllUsers(HttpServletResponse response) throws IOException {
        List<User> users = userServiceI.viewUsers();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNodes = mapper.createObjectNode();
        String data = "";

        if(users.isEmpty()){
            jsonNodes.put("found", false);
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNodes);
        } else{
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.buildUserListJson(users).getSystemUsers());
        }
        response.getWriter().println(data);
    }

    //find user by the Id
    @GetMapping("user/{id}")
    @Operation(summary = "Get user by id", description = "Returns a single user by id")
    public ResponseEntity<User> findUserById(@PathVariable @Param("id") @Parameter(description = "User id") int id) {
        return (ResponseEntity<User>) userService.getUserById(id);
    }

    @PostMapping("/addagent")
    @Operation(summary = "Add agent", description = "Create a new agent user")
    public void addAgent (@RequestBody @Parameter(description = "Agent user payload") User user){
         userServiceI.addAgent(user);
    }


    @PostMapping(value = "/assignmerchantterminal")
    @Operation(summary = "Assign terminal to merchant", description = "Assign one or more terminals to a merchant")
    public ResponseEntity<?> assignmerchantterminal(@RequestBody @Parameter(description = "Terminal assignment request") TerminalAssignmentRequest terminalAssignmentRequest) throws Exception {
       userServiceI.assignTerminals(terminalAssignmentRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping(value = "/{id}", consumes = {"application/json"})
    @Operation(summary = "Update user", description = "Update an existing user by id")
    public ResponseEntity<?> updateAccount(@RequestBody @Parameter(description = "Updated user payload") User user, @PathVariable @Parameter(description = "User id") int id) {
        userServiceI.updateUser(user, id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/agenttoterminal")
    @Operation(summary = "Assign terminal to agent", description = "Assign one or more terminals to an agent")
        public ResponseEntity<?> terminalToAgent(@RequestBody @Parameter(description = "Agent to terminal assignment") TerminalToAgentResponse terminalToAgentResponse) throws Exception {
        userServiceI.assignTerminalsToAgent(terminalToAgentResponse);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("id/{id}")
    @Operation(summary = "Find by id", description = "Returns user by id")
    public User findById(@PathVariable("id") @Parameter(description = "User id") long id) {
        return userService.getUserById(id);
    }


    @GetMapping("/unAssignedAgents")
    @Operation(summary = "Unassigned agents", description = "List agents that are not assigned to a terminal")
    public List<User> unAssignedAgents(){
        return userService.unAssignedAgents();
    }


    private ListSystemUser buildUserListJson(List<User> users){
        return new ListSystemUser(users.stream().map(user -> new SystemUser(user)).collect(Collectors.toList()));
    }

}

