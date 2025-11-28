package rafikibora.model.account;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import rafikibora.model.transactions.Transaction;
import rafikibora.model.users.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
// Update record instead of deleting it
@SQLDelete(sql = "UPDATE accounts SET is_deleted=true,status=false WHERE account_id=?")
// Excludes all deleted records by default
//@Where(clause = "is_deleted <> true")
@Table(name = "accounts")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_id", columnDefinition = "INT(10)")
    private int id;

    @Column(name = "name",nullable = false, columnDefinition = "VARCHAR(50)")
    private String name;

    @Column(name = "account_number", unique = true, nullable = false, columnDefinition = "VARCHAR(10)")
    public String accountNumber;

    @Column(name = "pan", columnDefinition = "VARCHAR(16)")
    private String pan;

    @Column(name = "phone_number", columnDefinition = "VARCHAR(10)")
    private String phoneNumber;

    @Column(name = "is_deleted", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @PrePersist
    public void prePersist() {
        dateCreated = LocalDateTime.now();
    }


    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;

    @PreUpdate
    public void preUpdate() { dateUpdated = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name="created_by", referencedColumnName = "userid")
    @JsonIgnore
    private User accountMaker;

//    @Column(name = "created_by")
//    private Integer accountMakers;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="approved_by", referencedColumnName = "userid")
    private User accountChecker;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean status;

    @Column(name = "balance", columnDefinition = "DOUBLE(12,2) DEFAULT 0.00")
    private double balance;

    @OneToMany(mappedBy="sourceAccount",cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> withdrawals = new ArrayList<Transaction>();

    @OneToMany(mappedBy="destinationAccount",cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> deposits = new ArrayList<Transaction>();

    /**
     * Ensures status and isDeleted values are also updated in the
     * current session
     */
    @PreRemove
    public void deleteAccount () {
        this.isDeleted = true;
        this.status = false;
    }
}
