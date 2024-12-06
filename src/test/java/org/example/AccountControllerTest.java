package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AccountControllerTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private Model model;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testRegisterMenu() {
//        String viewName = accountController.registerMenu(model);
//        assertEquals("createaccount", viewName);
//        verify(model).addAttribute(eq("user"), any(Account.class));
//    }

    @Test
    public void testCreateUser_UsernameExists() {
        Account newAccount = new Account();
        newAccount.setUsername("existingUser");
        newAccount.setTempPassword("Valid1!");

        when(accountRepository.findByUsername("existingUser")).thenReturn(Optional.of(new Account()));

        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class, () -> {
            accountController.createUser(newAccount, model);
        });
        assertTrue(ex.getMessage().contains("Username already exists"));
    }

    @Test
    public void testCreateUser_InvalidPassword() {
        Account newAccount = new Account();
        newAccount.setUsername("newUser");
        newAccount.setTempPassword("short"); // invalid password (too short)

        when(accountRepository.findByUsername("newUser")).thenReturn(Optional.empty());

        InvalidInputException ex = assertThrows(InvalidInputException.class, () -> {
            accountController.createUser(newAccount, model);
        });
        assertTrue(ex.getMessage().contains("Invalid password"));
    }

    @Test
    public void testLoginMenu() {
        String message = "SomeMessage";
        String viewName = accountController.loginMenu(model, message);
        assertEquals("loginaccount", viewName);
        verify(model).addAttribute("message", message);
        verify(model).addAttribute(eq("user"), any(Account.class));
    }

    @Test
    public void testLoginMenu_DefaultMessage() {
        String viewName = accountController.loginMenu(model, "");
        assertEquals("loginaccount", viewName);
        verify(model).addAttribute("message", "");
        verify(model).addAttribute(eq("user"), any(Account.class));
    }

    @Test
    public void testLoginUser_AccountNotFound() {
        Account loginAttempt = new Account();
        loginAttempt.setUsername("noSuchUser");
        loginAttempt.setHashedPassword(Account.hashPassword("Valid1!"));

        when(accountRepository.findByUsername("noSuchUser")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            accountController.loginUser(loginAttempt);
        });
        assertTrue(ex.getMessage().contains("Account not found with username: noSuchUser"));
    }

    @Test
    public void testLoginUser_InvalidPassword() {
        Account loginAttempt = new Account();
        loginAttempt.setUsername("existingUser");
        loginAttempt.setHashedPassword(Account.hashPassword("WrongPass1!"));

        Account accountInDb = new Account("existingUser", "Correct1!");
        accountInDb.setId(101L);

        when(accountRepository.findByUsername("existingUser")).thenReturn(Optional.of(accountInDb));

        InvalidInputException ex = assertThrows(InvalidInputException.class, () -> {
            accountController.loginUser(loginAttempt);
        });
        assertTrue(ex.getMessage().contains("Invalid password. Please try again."));
    }

    @Test
    public void testDisplayUser_AccountNotFound() {
        Long userId = 999L;
        when(accountRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            accountController.displayUser(userId, model);
        });
        assertTrue(ex.getMessage().contains("User not found with ID: 999"));
    }

    @Test
    public void testDisplayUser_Success() {
        Long userId = 300L;
        Account foundAccount = new Account("displayUser", "Valid1!");
        foundAccount.setId(userId);

        when(accountRepository.findById(userId)).thenReturn(Optional.of(foundAccount));

        String viewName = accountController.displayUser(userId, model);
        assertEquals("displayaccount", viewName);
        verify(model).addAttribute("user", foundAccount);
    }
}
