package rafikibora.model.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * The entity allowing interaction with the userroles table.
 * The join table between users and roles.
 * <p>
 * Table enforces a unique constraint of the combination of userid and roleid.
 * These two together form the primary key.
 * <p>
 * When you have a compound primary key, you must implement Serializable for Hibernate
 * When you implement Serializable you must implement equals and hash code
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "userroles")
@IdClass(UserRolesId.class)
public class UserRoles
        implements Serializable
{
    /**
     * 1/2 of the primary key (long) for userroles.
     * Also is a foreign key into the users table
     */
    @Id
    @ManyToOne
    @NotNull
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties(value = "roles",
            allowSetters = true)
    private User user;

    /**
     * 1/2 of the primary key (long) for userroles.
     * Also is a foreign key into the roles table
     */
    @Id
    @ManyToOne
    @NotNull
    @JoinColumn(name = "roleid")
    @JsonIgnoreProperties(value = "users",
            allowSetters = true)
    private Role role;

//    /**
//     * Given the params, create a new user role combination object
//     *
//     * @param user The user object of this relationship
//     * @param role The role object of this relationship
//     */
//    public UserRoles(
//            User user,
//            Role role)
//    {
//        this.user = user;
//        this.role = role;
//    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof UserRoles))
        {
            return false;
        }
        UserRoles that = (UserRoles) o;
        return ((user == null) ? 0 : user.getUserid()) == ((that.user == null) ? 0 : that.user.getUserid()) &&
                ((role == null) ? 0 : role.getRoleid()) == ((that.role == null) ? 0 : that.role.getRoleid());
    }

    @Override
    public int hashCode()
    {
        // return Objects.hash(user.getUserId(), role.getRoleId());
        return 37;
    }
}
