package Controller;

import Service.AccountService;
import Service.MessageService;
import Model.Message;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Optional;

/**
 * This class handles the web endpoints related to message operations.
 * It utilizes the MessageService for business logic and data access.
 */
public class MessageController {

    private final MessageService messageService = new MessageService();
    private final AccountService accountService = new AccountService();

    /**
     * Attaches the routes to the provided Javalin app.
     *
     * @param app The Javalin application to attach routes to.
     */
    public void attachRoutes(Javalin app) {
        app.post("/messages", this::handleCreateMessage);
        app.get("/messages", this::handleGetAllMessages);
        app.get("/messages/{message_id}", this::handleGetMessageById);
        app.delete("/messages/{message_id}", this::handleDeleteMessage);
        app.patch("/messages/{message_id}", this::handleUpdateMessage);
        app.get("/accounts/{account_id}/messages", this::handleGetMessagesByUserId);
    }

    /**
     * Handles creating a new message.
     * Parses request body to create a Message object,
     * validates it, and if valid, creates a new message in the database.
     * Responds with the newly created message or an error message.
     * Error codes: 400, 200, 500
     *
     * @param ctx The context object representing the HTTP request and response.
     */
    private void handleCreateMessage(Context ctx) {
        try {
            Message message = ctx.bodyAsClass(Message.class);
            // check if message_text is not valid then stop creation return 400
            if (!messageService.isValidText(message)) {
                ctx.status(400);
                return;
            }
            // check if user exists in db before creating message
            if (accountService.get(message.getPosted_by()).isEmpty()) {
                ctx.status(400);
                return;
            }
            Message createdMessage = messageService.create(message);
            ctx.status(200).json(createdMessage);
        } catch (Exception e) {
            ctx.status(500).result("Server error while processing message creation");
        }
    }

    /**
     * Handles requests for retrieving all messages.
     * Responds with a list of all messages from the database or an error message
     *
     * @param ctx The context object representing the HTTP request and response.
     */
    private void handleGetAllMessages(Context ctx) {
        try {
            List<Message> messages = messageService.getAll();
            ctx.json(messages);
        } catch (Exception e) {
            ctx.status(500).result("Server error while fetching all messages");
        }
    }

    /**
     * Handles retrieving a specific message by its ID.
     * Responds with the requested message if found,
     * or a not found/error message if the message does not exist or an issue
     * occurs.
     *
     * @param ctx The context object representing the HTTP request and response.
     */
    private void handleGetMessageById(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Optional<Message> message = messageService.get(messageId);
            if (message.isPresent()) {
                ctx.status(200).json(message.get());
            } else {
                ctx.status(200);
            }
        } catch (Exception e) {
            ctx.status(500).result("Server error while fetching message by ID");
        }
    }

    /**
     * Handles deleting a message by its ID.
     * Responds with the deleted message if successful,
     * no content if the message does not exist,
     * or an error message if an issue occurs.
     * status: 200, 500
     *
     * @param ctx The context object representing the HTTP request and response.
     */
    private void handleDeleteMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Optional<Message> deletedMessage = messageService.delete(messageId);
            if (deletedMessage.isPresent()) {
                ctx.status(200).json(deletedMessage.get());
            } else {
                ctx.status(200); // message doesnt exist in db so we just return status
            }
        } catch (Exception e) {
            ctx.status(500).result("Server error while deleting message");
        }
    }

    /**
     * Handles updating a specific message's details by its ID.
     * Responds with the updated message if successful,
     * a not found message if the message does not exist,
     * or an error message if an issue occurs.
     *
     * @param ctx The context object representing the HTTP request and response.
     */
    private void handleUpdateMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message updatedInfo = ctx.bodyAsClass(Message.class);

            // if the text is not valid then return 400 and stop update
            if (!messageService.isValidText(updatedInfo)) {
                ctx.status(400);
                return;
            }

            // if the message exists then let's update it, else stop update
            Optional<Message> existingMessage = messageService.get(messageId);
            if (existingMessage.isPresent()) {
                Message messageToUpdate = existingMessage.get();
                messageToUpdate.setMessage_text(updatedInfo.getMessage_text());
                Message updatedMessage = messageService.update(messageToUpdate);
                ctx.json(updatedMessage);
            } else {
                ctx.status(400);
            }
        } catch (Exception e) {
            ctx.status(500).result("Server error while updating message");
        }
    }

    /**
     * Handles requests for retrieving all messages posted by a specific user.
     * Responds with a list of messages or an error message if an issue occurs.
     *
     * @param ctx The context object representing the HTTP request and response.
     */
    private void handleGetMessagesByUserId(Context ctx) {
        try {
            int userId = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getAllByUserId(userId);
            ctx.json(messages);
        } catch (Exception e) {
            ctx.status(500).result("Server error while fetching messages by user ID");
        }
    }
}
