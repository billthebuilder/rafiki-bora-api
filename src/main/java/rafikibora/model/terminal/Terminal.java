package rafikibora.model.terminal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import rafikibora.model.transactions.Transaction;
import rafikibora.model.users.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "terminals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE terminals SET is_deleted=true,status=false WHERE terminal_id=?")
// Excludes all deleted records by default
//@Where(clause = "is_deleted <> true")
public class Terminal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="terminal_id", columnDefinition = "INT(10)")
    private Long id;

    @Column(name = "tid", unique = true, columnDefinition = "VARCHAR(16)")
    private String tid;

    @Column(name = "serial_no", unique = true, columnDefinition = "VARCHAR(28)")
    private String serialNo;

    @Column(name = "model_type", columnDefinition = "VARCHAR(10)")
    private String modelType;

    @Column(name = "status",  nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mid", referencedColumnName = "mid")
    @JsonIgnore
    private User mid;

    /**
     * MakerChecker relation to user
     */

    @ManyToOne
    @JoinColumn(name="created_by",  referencedColumnName = "userid")
    @JsonIgnore
    private User terminalMaker;

    @ManyToOne
    @JoinColumn(name="approved_by", referencedColumnName = "userid")
    @JsonIgnore
    private User terminalChecker;


    @Column(name = "is_deleted", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @PrePersist
    public void prePersist() {
        createdOn = LocalDateTime.now();
    }

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @PreUpdate
    public void preUpdate() {
        updatedOn = LocalDateTime.now();
    }


    @OneToMany(mappedBy="terminal",cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<Transaction>();

    /**
     * Assigning Terminals to Agents relation
     */

    @ManyToOne
    @JoinColumn(name="userid")
    @JsonIgnoreProperties(value = "assignedTerminals",
            allowSetters = true)
    private User agent;

    /**
     * Ensures status and isDeleted values are also updated in the
     * current session
     */
    @PreRemove
    public void deleteTerminal () {
        this.isDeleted = true;
        this.status = false;
    }
}