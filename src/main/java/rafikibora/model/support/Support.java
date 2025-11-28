package rafikibora.model.support;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.util.Date;
@Entity
@Table(name = "support")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE support SET is_deleted=true WHERE support_id=?")
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="support_id", columnDefinition = "INT(10)")
    private Long id;

    @Column(name = "tid", unique = true, columnDefinition = "VARCHAR(16)")
    private String tid;

    @Column(name = "name",nullable = false, columnDefinition = "VARCHAR(50)")
    private String name;

    @Column(name = "date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "reason",nullable = false, columnDefinition = "VARCHAR(50)")
    private String reason;

}
