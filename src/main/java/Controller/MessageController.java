package Controller;

import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Message;

public class MessageController {

    private final MessageService messageService = new MessageService();

    public void attachRoutes(Javalin app) {
        app.post("/messages", this::handleCreateMessage);
        app.get("/messages", this::handleGetAllMessages);
        app.get("/messages/:message_id", this::handleGetMessageById);
        app.delete("/messages/:message_id", this::handleDeleteMessage);
        app.patch("/messages/:message_id", this::handleUpdateMessage);
        app.get("/accounts/:account_id/messages", this::handleGetMessagesByUserId);
    }

    private void handleCreateMessage(Context ctx) {
        // Parse request for new message, validate, and create
    }

    private void handleGetAllMessages(Context ctx) {
        // Retrieve and respond with all messages
    }

    private void handleGetMessageById(Context ctx) {
        // Extract message ID from path and retrieve specific message
    }

    private void handleDeleteMessage(Context ctx) {
        // Extract message ID and delete the message
    }

    private void handleUpdateMessage(Context ctx) {
        // Extract message ID and update details
    }

    private void handleGetMessagesByUserId(Context ctx) {
        // Extract user ID from path and retrieve messages by that user
    }
}
