package adam_barnett.madlibs.madlib_machine.madlib.mvc;

import adam_barnett.madlibs.madlib_machine.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity representing a completed madlib stored in the database.
 * Each instance maps to one row in the "madlibs" table.
 */
@Entity
@Table(name = "madlibs")
@Getter
@NoArgsConstructor
public class Madlib {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String completedText;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    @Getter @Setter
    private String blankedText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Madlib(String completedText) {
        this.completedText = completedText;
        this.createdAt = LocalDateTime.now();
    }

    public Madlib(String completedText, String blankedText, User user) {
        this.completedText = completedText;
        this.blankedText = blankedText;
        this.createdAt = LocalDateTime.now();
        this.user = user;
    }
}
