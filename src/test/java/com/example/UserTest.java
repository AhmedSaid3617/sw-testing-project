package com.example;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    @DisplayName("Valid user creation")
    public void testValidUserCreation() {
        List<String> movies = new ArrayList<>();
        User user = assertDoesNotThrow( ()->new User("John Doe", "12345678A", movies));
        
        assertEquals("John Doe", user.getName());
        assertEquals("12345678A", user.getId());
        assertEquals(movies, user.getLikedMovies());
    }
    
    @Test
    @DisplayName("User name - alphabetic characters and spaces")
    public void testUserNameAlphabeticAndSpaces() {
        List<String> movies = new ArrayList<>();
        assertDoesNotThrow(() -> new User("John Smith", "123456789", movies));
        assertDoesNotThrow(() -> new User("Mary Jane Watson", "12345678B", movies));
        assertDoesNotThrow(() -> new User("Alice", "987654321", movies));}
    @Test
    @DisplayName("User name - shouldnt start with space")
    public void testUserNameCannotStartWithSpace() {
        List<String> movies = new ArrayList<>();
        Exception exception = assertThrows(UserException.class, () -> {
            new User(" John Doe", "123456789", movies);
        });
        assertTrue(exception.getMessage().contains("Error in user name"));
    }
    
    @Test
    @DisplayName("User name- invalid with numbers")
    public void testUserNameInvalidWithNumbers() {
        List<String> movies = new ArrayList<>();
        Exception exception = assertThrows(UserException.class, () -> {
            new User("John123", "123456789", movies);
        });
        assertTrue(exception.getMessage().contains("Error in user name"));
    }
    
    @Test
    @DisplayName("User name - invalid with special characters")
    public void testUserNameInvalidWithSpecialCharacters() {
        List<String> movies = new ArrayList<>();
        
        Exception exception = assertThrows(UserException.class, () -> {
            new User("John@Doe", "123456789", movies);
        });
        assertTrue(exception.getMessage().contains("Error in user name"));
    }
    @Test
    @DisplayName("User ID - must be exactly 9 characters")
    public void testUserIdExactLength() {
        List<String> movies = new ArrayList<>();
        assertDoesNotThrow(() -> new User("John Doe", "123456789", movies));
        assertDoesNotThrow(() -> new User("Jane Smith", "12345678A", movies));
        Exception exception1 = assertThrows(UserException.class, () -> {
            new User("John Doe", "12345678", movies);
            });
        assertTrue(exception1.getMessage().contains("Error in user id length"));
        Exception exception2 = assertThrows(UserException.class, () -> {
            new User("John Doe", "1234567890", movies);
        });
        assertTrue(exception2.getMessage().contains("Error in user id length"));
    }
    @Test
    @DisplayName("User ID - must start with numbers")
    public void testUserIdStartsWithNumbers() {
        List<String> movies = new ArrayList<>();
        assertDoesNotThrow(() -> new User("John Doe", "123456789", movies));
        assertDoesNotThrow(() -> new User("Jane Smith", "12345678A", movies));
        Exception exception = assertThrows(UserException.class, () -> {
            new User("John Doe", "A12345678", movies);});
        assertTrue(exception.getMessage().contains("Error in user id format"));
    }
    @Test
    @DisplayName("User ID -can end with one alphabetic char")
    public void testUserIdEndsWithOneAlphabeticCharacter() {
        List<String> movies = new ArrayList<>();
        assertDoesNotThrow(() -> new User("John Doe", "12345678A", movies));
        assertDoesNotThrow(() -> new User("Jane Smith", "87654321Z", movies));
        assertDoesNotThrow(() -> new User("Bob Jones", "123456789", movies));
        Exception exception = assertThrows(UserException.class, () -> {
            new User("John Doe", "1234567AB", movies);
        });
        assertTrue(exception.getMessage().contains("Error in user id format"));
        }
    
    @Test
    @DisplayName("User ID - alphanumeric only.")
    public void testUserIdAlphanumericOnly() {
        List<String> movies = new ArrayList<>();
        Exception exception = assertThrows(UserException.class, () -> {
            new User("John Doe", "12345678@", movies);
        });
        assertThrows(UserException.class, () -> {
            new User("John Doe", "1234567@8", movies);
        });
        assertTrue(exception.getMessage().contains("Error in user id format"));
    }
    
    @Test
    @DisplayName("User - add liked movies")
    public void testUserAddLikedMovies() {
        List<String> movies = new ArrayList<>();
        User user = assertDoesNotThrow( ()->new User("John Doe", "123456789", movies));
        user.addLikedMovie("I123");
        user.addLikedMovie("A456");
        assertEquals(2, user.getLikedMovies().size());
        assertTrue(user.getLikedMovies().contains("I123"));
        assertTrue(user.getLikedMovies().contains("A456"));
    }
    @Test
    @DisplayName("User - create with initial liked movies")
    public void testUserWithInitialLikedMovies() {
        List<String> movies = new ArrayList<>(Arrays.asList("T789", "FG321"));
        User user = assertDoesNotThrow( ()->new User("Alice Brown", "555555555", movies));    
        assertEquals(2, user.getLikedMovies().size());
        assertEquals("T789", user.getLikedMovies().get(0));
        assertEquals("FG321", user.getLikedMovies().get(1));
    }
}