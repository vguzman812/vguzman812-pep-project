package DAO;

import java.util.List;
import java.util.Optional;

/**
 * Generic interface for repository/dao layer operations.
 *
 * @param <T> Type parameter representing the model class.
 */
public interface DaoInterface<T> {
    // get could be null if no result present, 
    // so we are using an optional to handle that
    Optional<T> get(int id);

    // returns a list of all results
    List<T> getAll();

    T create(T t);

    T update(T t);

    // Returns the deleted resource, or null if no resource found
    // because null possible, we use optional
    Optional<T> delete(int id);
}