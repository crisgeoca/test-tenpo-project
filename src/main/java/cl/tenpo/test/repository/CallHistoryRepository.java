package cl.tenpo.test.repository;

import cl.tenpo.test.entity.CallHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@code CallHistory} entities.
 * <p>
 * This interface extends {@code JpaRepository} to provide CRUD operations and
 * database interaction for {@code CallHistory} entities. It leverages Spring Data JPA
 * to simplify data access and manipulation.
 * </p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Supports basic CRUD operations like save, delete, and find.</li>
 *   <li>Allows custom query methods to be defined for specific use cases.</li>
 *   <li>Uses {@code Long} as the ID type for {@code CallHistory} entities.</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b></p>
 * <ul>
 *   <li>This interface is thread-safe as it relies on Spring's managed beans and transaction handling.</li>
 * </ul>
 *
 * @author Christian Giovani Cachaya Bolivar
 * @version 1.0
 */
@Repository
public interface CallHistoryRepository extends JpaRepository<CallHistory, Long> {
}
