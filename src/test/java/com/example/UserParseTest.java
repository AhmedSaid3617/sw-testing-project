package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.List;

public class UserParseTest {

    private UserParser userParser;

    @BeforeEach
    void setUp() {
        userParser = new UserParser();
    }

    @Test
    @DisplayName("Parse single user with valid format")
    void testParseSingleValidUser() throws Exception {
        String userData = "John Doe,123456789\nM001,M002\n";
        
        List<User> users = userParser.parseUsers(userData);
        
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals("123456789", users.get(0).getId());
        assertEquals(2, users.get(0).getLikedMovies().size());
        assertTrue(users.get(0).getLikedMovies().contains("M001"));
        assertTrue(users.get(0).getLikedMovies().contains("M002"));
    }

    @Test
    @DisplayName("Parse multiple users")
    void testParseMultipleUsers() throws Exception {
        String userData = "John Doe,123456789\nM001,M002\n" +
                         "Jane Smith,12345678A\nM003,M004,M005\n" +
                         "Bob Johnson,987654321\nM001\n";
        
        List<User> users = userParser.parseUsers(userData);
        
        assertEquals(3, users.size());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals("Jane Smith", users.get(1).getName());
        assertEquals("Bob Johnson", users.get(2).getName());
    }

    @Test
    @DisplayName("Parse user with single liked movie")
    void testParseUserWithSingleLikedMovie() throws Exception {
        String userData = "Alice Brown,555555555\nM100\n";
        
        List<User> users = userParser.parseUsers(userData);
        
        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getLikedMovies().size());
        assertEquals("M100", users.get(0).getLikedMovies().get(0));
    }

    @Test
    @DisplayName("Parse user with multiple liked movies")
    void testParseUserWithMultipleLikedMovies() throws Exception {
        String userData = "Charlie Davis,111111111\nM001,M002,M003,M004,M005\n";
        
        List<User> users = userParser.parseUsers(userData);
        
        assertEquals(1, users.size());
        assertEquals(5, users.get(0).getLikedMovies().size());
        assertTrue(users.get(0).getLikedMovies().contains("M001"));
        assertTrue(users.get(0).getLikedMovies().contains("M005"));
    }

    @Test
    @DisplayName("Parse user with spaces in name")
    void testParseUserWithSpacesInName() throws Exception {
        String userData = "Mary Jane Watson,222222222\nM001\n";
        
        List<User> users = userParser.parseUsers(userData);
        
        assertEquals(1, users.size());
        assertEquals("Mary Jane Watson", users.get(0).getName());
    }

    @Test
    @DisplayName("Parse user with ID ending with letter")
    void testParseUserWithIdEndingWithLetter() throws Exception {
        String userData = "David Lee,12345678Z\nM050\n";
        
        List<User> users = userParser.parseUsers(userData);
        
        assertEquals(1, users.size());
        assertEquals("12345678Z", users.get(0).getId());
    }

    @Test
    @DisplayName("Parse user with ID ending with number")
    void testParseUserWithIdEndingWithNumber() throws Exception {
        String userData = "Emma Wilson,987654321\nM075\n";
        
        List<User> users = userParser.parseUsers(userData);
        
        assertEquals(1, users.size());
        assertEquals("987654321", users.get(0).getId());
    }

    @Test
    @DisplayName("Odd number of lines throws exception")
    void testOddNumberOfLinesThrowsException() {
        String userData = "Frank Thomas,333333333\n";
        
        Exception exception = assertThrows(Exception.class, () -> {
            userParser.parseUsers(userData);
        });
        assertTrue(exception.getMessage().contains("Odd number of lines"));
    }

    @Test
    @DisplayName("Invalid user format (missing comma) throws exception")
    void testInvalidUserFormatThrowsException() {
        String userData = "Grace Lee 444444444\nM001\n";
        
        Exception exception = assertThrows(Exception.class, () -> {
            userParser.parseUsers(userData);
        });
        assertTrue(exception.getMessage().contains("Invalid user data format"));
    }

    @Test
    @DisplayName("Invalid user name white space throws exception")
    void testInvalidUserNameThrowsException() {
        String userData = " henry Jackson,555555555\nM001\n";
        
        assertThrows(UserException.class, () -> {
            userParser.parseUsers(userData);
        });
    }

    @Test
    @DisplayName("Invalid user ID format throws exception")
    void testInvalidUserIdFormatThrowsException() {
        String userData = "Iris Kim,A12345678\nM001\n";
        
        assertThrows(UserException.class, () -> {
            userParser.parseUsers(userData);
        });
    }

    @Test
    @DisplayName("Invalid user ID length throws exception")
    void testInvalidUserIdLengthThrowsException() {
        String userData = "Jack Brown,12345678\nM001\n";
        
        assertThrows(UserException.class, () -> {
            userParser.parseUsers(userData);
        });
    }

    @Test
    @DisplayName("Parse user with special character in movie ID")
    void testParseUserWithSpecialCharacterInMovieId() throws Exception {
        String userData = "Kate Miller,666666666\nM-001,M-002\n";
        
        List<User> users = userParser.parseUsers(userData);
        
        assertEquals(1, users.size());
        assertEquals(2, users.get(0).getLikedMovies().size());
        assertTrue(users.get(0).getLikedMovies().contains("M-001"));
    }

    @Test
    @DisplayName("Parse empty liked movies list")
    void testParseUserWithEmptyLikedMoviesList() throws Exception {
        String userData = "Leo Wang,777777777\n\n";
        
        assertThrows(UserException.class, () -> {
            userParser.parseUsers(userData);
        });
    }

    @Test
    void testParseMultipleUsersWithNoLikedMovies() {
        String userData = "Mia Garcia,121212121\n"+
                          "Noah Martinez,131313131\n"+
                          "Noah Martinez,131313131\n"+
                          "Noah Martinez,131313131\n";
        
        Exception exception = assertThrows(UserException.class, () -> {
            userParser.parseUsers(userData);
        });
        System.err.println(exception.getMessage());
    }

   @Test
    @DisplayName("Parse multi users")
    void testParseMultiUsers() throws Exception {
        String userData = "Anna Scott,888888888\nM010,M020\n" +
                          "Brian Cox,99999999B\nM030\n";
        
        List<User> users = userParser.parseUsers(userData);
        
        assertEquals(2, users.size());
        assertEquals("Anna Scott", users.get(0).getName());
        assertEquals("Brian Cox", users.get(1).getName());
        assertEquals(2, users.get(0).getLikedMovies().size());
        assertEquals(1, users.get(1).getLikedMovies().size());
    }
}

