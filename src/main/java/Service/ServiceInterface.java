package Service;

import java.util.List;
import java.util.Optional;

/**
 * Generic interface for service layer operations.
 *
 * @param <T> Type parameter representing the model class.
 */
public interface ServiceInterface<T> {
    T create(T t);
    T update(T t);
    Optional<T> delete(int id);
    Optional<T> get(int id);
    List<T> getAll();
}