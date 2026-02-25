package adam_barnett.madlibs.madlib_machine.madlib;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name="COMPLETED_MADLIB")
public class CompleteMadlib {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    private long id;

    @NotNull
    @Getter @Setter private String text;

    @Override
    public String toString() {
        return text;
    }
}
