package Controller;

import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;

public class AccountController {

    private final AccountService accountService = new AccountService();

    public void attachRoutes(Javalin app) {
        app.post("/register", this::handleRegister);
        app.post("/login", this::handleLogin);
    }

    private void handleRegister(Context ctx) {
        // Parse the request body to create an Account
        // Call accountService.create() and handle responses (success/error)
    }

    private void handleLogin(Context ctx) {
        // Validate login credentials
        // Call accountService.findByUsername() and compare passwords
        // Respond with account details or error
    }
}
