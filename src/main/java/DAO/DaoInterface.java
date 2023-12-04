package DAO;

import java.util.List;
import java.util.Optional;

public interface DaoInterface<T> {
    // get could be null if no result present
    Optional<T> get(int id);

    // returns a list of all results
    List<T> getAll();

    T create(T t);

    T update(T t);

    // Returns the deleted resource
    Optional<T> delete(int id);
}