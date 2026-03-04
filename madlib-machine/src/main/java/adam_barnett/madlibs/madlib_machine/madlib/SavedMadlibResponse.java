package adam_barnett.madlibs.madlib_machine.madlib;

import java.time.LocalDateTime;
import java.util.UUID;

/** DTO for returning a stored madlib from the database */
public record SavedMadlibResponse(UUID id, String completedText, LocalDateTime createdAt) {
}
