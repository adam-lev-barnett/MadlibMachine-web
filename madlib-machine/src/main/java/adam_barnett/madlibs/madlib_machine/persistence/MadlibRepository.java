package adam_barnett.madlibs.madlib_machine.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data repository for Madlib entities.
 * Spring auto-generates all standard database operations (save, findAll, findById, delete, etc.)
 * — no SQL or implementation needed.
 */
public interface MadlibRepository extends JpaRepository<Madlib, UUID> {
    List<Madlib> findByUser(User user);
}
