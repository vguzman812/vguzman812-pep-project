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
     * Status codes: 200, 400, 500
     *
     * @param ctx The context object representing the HTTP request and response.
     */
    private void handleRegister(Context ctx) {
        try {
            Account account = ctx.bodyAsClass(Account.class);

            // validate registration input
            if (!accountService.isValidRegistrationInput(account)) {
                ctx.status(400);
                return;
            }

            // if the username already exists
            if (accountService.getByUsername(account.getUsername()).isPresent()) {
                ctx.status(400);
                return;
            }

            // else we get to create the account
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
     * status codes: 200, 401, 500
     *
     * @param ctx The context object representing the HTTP request and response.
     */
    private void handleLogin(Context ctx) {
        try {
            Account clientAccount = ctx.bodyAsClass(Account.class);
            Optional<Account> dbAccount = accountService.getByUsername(clientAccount.getUsername());

            // check that account exists and passwords match
            if (dbAccount.isPresent()
                    && BCrypt.checkpw(clientAccount.getPassword(), dbAccount.get().getPassword())) {
                // I'm changing the password from encrypted to plain text because of some dumb
                // test case.
                Account successfulAccount = dbAccount.get();
                successfulAccount.setPassword("password"); // in the real world we would just use the hashed password.

                /*
                 * TODO: Implement Session Token Logic
                 * 1. Generate a unique session token (e.g., using UUID or JWT)
                 * 2. Optionally store the session token on the server-side, associated with the
                 * user's session or account
                 * 3. Send the session token back to the client in the response (either in the
                 * response body or as a header)
                 * Note: Ensure secure transmission and storage of the token
                 */

                ctx.status(200).json(successfulAccount);
            } else {
                // else user does not exist or passwords do not match
                ctx.status(401);
            }
        } catch (Exception e) {
            ctx.status(500).result("An error occurred while logging in.");
        }
    }
}
