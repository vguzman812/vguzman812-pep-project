package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

/* 
 * Implementation of DaoInterface for Account model.
 * Follows DaoInterface method implementation PLUS:
 * public Optional<Account> findByUsername(String username)
 * 
*/
public class AccountDao implements DaoInterface<Account> {

    /**
     * Creates a new Account in the database.
     * 
     * @param account Account object to be created.
     * @return The created Account object with its generated ID.
     */
    @Override
    public Account create(Account account) {
        String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = ConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            String hashedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());

            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();

            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                account.setAccount_id(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating account failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        } finally {
            if (generatedKeys != null)
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return account;
    }

    /**
     * Updates an existing Account in the database.
     * 
     * @param account Account object to be updated.
     * @return The updated Account object.
     */
    @Override
    public Account update(Account account) {
        String sql = "UPDATE Account SET username = ?, password = ? WHERE account_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            String hashedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());

            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setInt(3, account.getAccount_id());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        } finally {
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return account;
    }

    /**
     * Deletes an Account from the database by its ID.
     * 
     * @param id The ID of the Account to be deleted.
     * @return An Optional containing the deleted Account or empty if not found.
     */
    @Override
    public Optional<Account> delete(int id) {
        Optional<Account> accountToDelete = get(id);
        if (accountToDelete == null) {
            return Optional.empty();
        }

        String sql = "DELETE FROM Account WHERE account_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return Optional.empty(); // No rows affected, message was not deleted
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
            return Optional.empty();
        } finally {
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return accountToDelete;
    }

    /**
     * Retrieves an Account from the database by its ID.
     * 
     * @param id The ID of the Account to be retrieved.
     * @return An Optional containing the found Account or empty if not found.
     */
    @Override
    public Optional<Account> get(int id) {
        String sql = "SELECT * FROM Account WHERE account_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return Optional.empty();
    }

    /**
     * Retrieves all Accounts from the database.
     * 
     * @return A List of all Accounts.
     */
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM Account";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                accounts.add(new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return accounts;
    }

    /**
     * Finds an Account by its username.
     * 
     * @param username The username of the Account to find.
     * @return An Optional containing the found Account or empty if not found.
     */
    public Optional<Account> getByUsername(String username) {
        String sql = "SELECT * FROM Account WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return Optional.empty();
    }

}
