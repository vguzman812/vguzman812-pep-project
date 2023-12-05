package Service;

import DAO.MessageDao;
import Model.Message;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for handling business logic associated with Message operations.
 * This class uses the MessageDao to interact with the db
 */
public class MessageService implements ServiceInterface<Message> {
    private final MessageDao messageDao = new MessageDao();

    /**
     * Creates a new Message in the database.
     *
     * @param message The Message object to be created.
     * @return The newly created Message object with a generated ID.
     */
    @Override
    public Message create(Message message) {
        return messageDao.create(message);
    }

    /**
     * Updates an existing Message in the database.
     *
     * @param message The Message object to be updated.
     * @return The updated Message object.
     */
    @Override
    public Message update(Message message) {
        return messageDao.update(message);
    }

    /**
     * Deletes a Message from the database by its ID.
     *
     * @param id The ID of the Message to be deleted.
     * @return An Optional containing the deleted Message or empty if the Message
     *         was not found.
     */
    @Override
    public Optional<Message> delete(int id) {
        return messageDao.delete(id);
    }

    /**
     * Retrieves a Message from the database by its ID.
     *
     * @param id The ID of the Message to be retrieved.
     * @return An Optional containing the found Message or empty if the Message is
     *         not found.
     */
    @Override
    public Optional<Message> get(int id) {
        return messageDao.get(id);
    }

    /**
     * Retrieves all Messages from the database.
     *
     * @return A List of all Messages.
     */
    @Override
    public List<Message> getAll() {
        return messageDao.getAll();
    }
}
