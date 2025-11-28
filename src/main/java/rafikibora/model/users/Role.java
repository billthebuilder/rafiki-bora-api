package rafikibora.model.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long roleid;

    @Column(length = 100, unique = true, nullable = false)
    @NotNull
    private String roleName;

    /**
     * Part of the join relationship between user and role
     * connects roles to the user role combination
     */
    @OneToMany(mappedBy = "role",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnoreProperties(value = "role",
            allowSetters = true)
    private Set<UserRoles> users = new HashSet<>();

//    /**
//     * Given the name, create a new role object. User gets added later
//     *
//     * @param roleName the name of the role in uppercase
//     */
//    public Role(String roleName)
//    {
//        this.roleName = roleName.toUpperCase();
//    }
//
//    /**
//     * Setter for role name
//     *
//     * @param roleName the new role name (String) for this role, in uppercase
//     */
//    public void setName(String roleName)
//    {
//        this.roleName = roleName.toUpperCase();
//    }
}
