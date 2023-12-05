package Controller;

import Service.AccountService;
import Model.Account;

import io.javalin.Javalin;
import io.javalin.http.Context;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

/**
 * Controller class responsible for handling HTTP requests related to Account
 * operations.
 * Utilizes AccountService to perform business logic.
 */
public class AccountController {

    private final AccountService accountService = new AccountService();

    /**
     * Attaches route handlers for account-related endpoints to the Javalin app.
     *
     * @param app The Javalin application instance to which the routes are attached.
     */
    public void attachRoutes(Javalin app) {
        app.post("/register", this::handleRegister);
        app.post("/login", this::handleLogin);
    }

    /**
     * Handles account registration requests.
     * Parses the request body to an Account object,
     * validates the input, and registers a new account if
     * valid input and no other user exists.
     * Responds with the created account or an error message.
     *
     * @param ctx The context object representing the HTTP request and response.
     */
    private void handleRegister(Context ctx) {
        try {
            Account account = ctx.bodyAsClass(Account.class);

            if (account.getUsername() == null || account.getUsername().isEmpty() ||
                    account.getPassword() == null || account.getPassword().length() < 4) {
                ctx.status(400).result("Invalid username or password");
                return;
            }

            if (accountService.findByUsername(account.getUsername()).isPresent()) {
                ctx.status(400).result("Username already exists");
                return;
            }

            Account createdAccount = accountService.create(account);
            ctx.status(200).json(createdAccount);
        } catch (Exception e) {
            ctx.status(500).result("An error occurred while registering.");
        }
    }

    /**
     * Handles login requests.
     * Validates the provided credentials against stored accounts.
     * If successful, responds with the account details;
     * otherwise, responds with an error.
     *
     * @param ctx The context object representing the HTTP request and response.
     */
    private void handleLogin(Context ctx) {
        try {
            Account loginAccount = ctx.bodyAsClass(Account.class);
            Optional<Account> accountOptional = accountService.findByUsername(loginAccount.getUsername());

            if (accountOptional.isPresent()
                    && BCrypt.checkpw(loginAccount.getPassword(), accountOptional.get().getPassword())) {
                ctx.status(200).json(accountOptional.get());
            } else {
                ctx.status(401).result("Invalid login credentials");
            }
        } catch (Exception e) {
            ctx.status(500).result("An error occurred while logging in.");
        }
    }
}
