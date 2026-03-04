package adam_barnett.madlibs.madlib_machine.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String googleId;

    private String email;
    private String name;
    private String pictureUrl;

    public User(String googleId, String email, String name, String pictureUrl) {
        this.googleId = googleId;
        this.email = email;
        this.name = name;
        this.pictureUrl = pictureUrl;
    }
}
