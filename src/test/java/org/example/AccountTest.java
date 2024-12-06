package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class AccountTest {

    @Test
    public void testNoArgsConstructor() {
        Account account = new Account();
        assertNull(account.getId());
        assertNull(account.getUsername());
        assertNull(account.getHashedPassword());
        assertNull(account.getTempPassword());
        assertNotNull(account.getSurveys());
        assertTrue(account.getSurveys().isEmpty());
    }

    @Test
    public void testConstructorWithUsernameAndPassword() {
        String username = "testUser";
        String password = "Valid1!";
        Account account = new Account(username, password);

        assertEquals(username, account.getUsername());
        assertEquals(password, account.getTempPassword());
        assertNotNull(account.getHashedPassword());
        assertNotEquals(password, account.getHashedPassword());
    }

    @Test
    public void testHashPasswordSuccess() {
        String password = "SomePass1!";
        String hashed = Account.hashPassword(password);
        assertNotNull(hashed);
        assertNotEquals(password, hashed);
        assertEquals(64, hashed.length());
    }

    @Test
    public void testHashPassword_NoSuchAlgorithmException() {
        try (MockedStatic<MessageDigest> mdMock = Mockito.mockStatic(MessageDigest.class)) {
            mdMock.when(() -> MessageDigest.getInstance("SHA-256")).thenThrow(new NoSuchAlgorithmException("Test Exception"));
            RuntimeException ex = assertThrows(RuntimeException.class, () -> {
                Account.hashPassword("password");
            });
            assertTrue(ex.getMessage().contains("Error while hashing the password"));
        }
    }

    @Test
    public void testGetSetId() {
        Account account = new Account();
        account.setId(123L);
        assertEquals(123L, account.getId().longValue());
    }

    @Test
    public void testGetSetUsername() {
        Account account = new Account();
        account.setUsername("newUser");
        assertEquals("newUser", account.getUsername());
    }

    @Test
    public void testPasswordMatch() {
        String password = "GoodPass1!";
        Account account = new Account("user", password);

        assertTrue(account.passwordMatch(password));
        assertFalse(account.passwordMatch("WrongPass"));
    }

    @Test
    public void testGetSetHashedPassword() {
        Account account = new Account("user", "Original1!");
        String oldHash = account.getHashedPassword();

        account.setHashedPassword("NewPass1!");
        String newHash = account.getHashedPassword();

        assertNotEquals(oldHash, newHash);
        assertTrue(account.passwordMatch("NewPass1!"));
        assertFalse(account.passwordMatch("Original1!"));
    }

    @Test
    public void testGetSurveysAddSurveySetSurveys() {
        Account account = new Account();
        assertTrue(account.getSurveys().isEmpty());

        Survey survey1 = new Survey("Survey1");
        account.addSurvey(survey1);
        assertEquals(1, account.getSurveys().size());
        assertEquals(survey1, account.getSurveys().get(0));

        Survey survey2 = new Survey("Survey2");
        List<Survey> newSurveys = new ArrayList<>();
        newSurveys.add(survey2);

        account.setSurveys(newSurveys);
        assertEquals(1, account.getSurveys().size());
        assertEquals(survey2, account.getSurveys().get(0));
    }

    @Test
    public void testGetSetTempPassword() {
        Account account = new Account();
        account.setTempPassword("tempPass");
        assertEquals("tempPass", account.getTempPassword());
    }

    @Test
    public void testIsValidPassword_TooShort() {
        assertFalse(Account.isValidPassword("Ab1!"));
    }

    @Test
    public void testIsValidPassword_NoUppercase() {
        assertFalse(Account.isValidPassword("abcde1!f"));
    }

    @Test
    public void testIsValidPassword_NoLowercase() {
        assertFalse(Account.isValidPassword("ABCDEF1!"));
    }

    @Test
    public void testIsValidPassword_NoDigit() {
        assertFalse(Account.isValidPassword("Abcdef!A"));
    }

    @Test
    public void testIsValidPassword_NoSpecialChar() {
        // No special char
        assertFalse(Account.isValidPassword("Abcdef1A"));
    }

    @Test
    public void testIsValidPassword_Valid() {
        assertTrue(Account.isValidPassword("Abcdef1!"));
    }
}
