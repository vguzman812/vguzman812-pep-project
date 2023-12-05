package Service;

import DAO.AccountDao;
import Model.Account;
import java.util.List;
import java.util.Optional;

/**
 * Service layer class for handling business logic associated with Account
 * operations.
 * This class uses AccountDao to interact with the database
 */
public class AccountService implements ServiceInterface<Account> {
    private final AccountDao accountDao = new AccountDao();

    /**
     * Creates a new Account in the database.
     *
     * @param account The Account object to be created.
     * @return The newly created Account object with a generated ID.
     */
    @Override
    public Account create(Account account) {
        return accountDao.create(account);
    }

    /**
     * Updates an existing Account in the database.
     *
     * @param account The Account object to be updated.
     * @return The updated Account object.
     */
    @Override
    public Account update(Account account) {
        return accountDao.update(account);
    }

    /**
     * Deletes an Account from the database by its ID.
     *
     * @param id The ID of the Account to be deleted.
     * @return An Optional containing the deleted Account or empty if the Account
     *         was not found.
     */
    @Override
    public Optional<Account> delete(int id) {
        return accountDao.delete(id);
    }

    /**
     * Retrieves an Account from the database by its ID.
     *
     * @param id The ID of the Account to be retrieved.
     * @return An Optional containing the found Account or empty if the Account is
     *         not found.
     */
    @Override
    public Optional<Account> get(int id) {
        return accountDao.get(id);
    }

    /**
     * Retrieves all Accounts from the database.
     *
     * @return A List of all Accounts.
     */
    @Override
    public List<Account> getAll() {
        return accountDao.getAll();
    }

    /**
     * Finds an Account by its username.
     * 
     * @param username The username to search for.
     * @return An Optional containing the Account if found, or empty if not found.
     */
    public Optional<Account> findByUsername(String username) {
        return accountDao.findByUsername(username);
    }
}
