package rafikibora.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "ListSystemUser", description = "Wrapper for a list of simplified system user records.")
public class ListSystemUser {

    private final List<SystemUser> systemUsers;

    public ListSystemUser(List<SystemUser> systemUsers){
        this.systemUsers = systemUsers;
    }

    public List<SystemUser> getSystemUsers() {
        return new ArrayList<>(systemUsers);
    }

}
