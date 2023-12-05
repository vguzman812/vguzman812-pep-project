package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/* 
 * Implementation of DaoInterface for Message model PLUS:
 *     public List<Message> getAllByUserId(int id)
*/
public class MessageDao implements DaoInterface<Message> {

    /**
     * Creates a new Message in the database.
     *
     * @param message The Message object to be created.
     * @return The Message object with its generated ID.
     */
    @Override
    public Message create(Message message) {
        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = ConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, message.getPosted_by());
            pstmt.setString(2, message.getMessage_text());
            pstmt.setLong(3, message.getTime_posted_epoch());
            pstmt.executeUpdate();

            // Retrieve the auto-generated keys (primary keys) created by the database
            generatedKeys = pstmt.getGeneratedKeys();
            // Check if a generated key is available
            if (generatedKeys.next()) {
                /*
                 * The db automatically generates a primary key. We need to get that and
                 * set the message_id property in our new Message object here
                 * We do that by getting the first column in our new row (i.e. the primary key)
                 * then using that value to setMessage_id on our new Message object
                 */
                message.setMessage_id(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating message failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
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
        return message;
    }

    /**
     * Updates an existing Message in the database.
     *
     * @param message The Message object to be updated.
     * @return The updated Message object.
     */
    @Override
    public Message update(Message message) {
        String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, message.getMessage_text());
            pstmt.setInt(3, message.getMessage_id());
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
        return message;
    }

    /**
     * Deletes a Message from the database by its ID.
     * 
     * @param id The ID of the Message to be deleted.
     * @return An Optional containing the deleted Message or empty if not found.
     */
    @Override
    public Optional<Message> delete(int id) {
        // First, find the message to be deleted
        Optional<Message> messageToDelete = get(id);
        if (messageToDelete == null) {
            return Optional.empty();
        }

        String sql = "DELETE FROM Message WHERE message_id = ?";
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

        return messageToDelete;
    }

    /**
     * Retrieves all Messages from the database.
     * 
     * @return A List of all Messages.
     */
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
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
        return messages;
    }

    /**
     * Retrieves a Message from the database by its ID.
     * 
     * @param id The ID of the Message to be retrieved.
     * @return An Optional containing the found Message or empty if not found.
     */
    @Override
    public Optional<Message> get(int id) {
        String sql = "SELECT * FROM Message WHERE message_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
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
     * Retrieves all messages posted by a specific user.
     *
     * @param id The ID of the user whose messages are to be retrieved.
     * @return A List of Message objects posted by the specified user.
     */
    public List<Message> getAllByUserId(int id) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        return messages;
    }
}
