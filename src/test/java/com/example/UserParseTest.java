package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class UserParseTest {

    private UserParser userParser;
    private DataStore mockDataStore;

    @BeforeEach
    void setUp() {
        mockDataStore = new DataStore();
        mockDataStore.addMovie(new Movie("1", "Movie One", new ArrayList<>()));
        mockDataStore.addMovie(new Movie("2", "Movie Two", new ArrayList<>()));
        mockDataStore.addMovie(new Movie("3", "Movie Three", new ArrayList<>()));

        userParser = new UserParser(mockDataStore);
    }

    @Test
    void shouldParseMultipleUsersFromValidInput() {
        // Arrange
        String usersFileData = "Alice,001\n1,2\nBob,002\n2,3\n";

        // Act
        assertDoesNotThrow(() -> userParser.parseUsers(usersFileData));

        // Assert
        List<User> users = mockDataStore.getUsers();
        assertEquals(2, users.size());
        assertEquals("Alice", users.get(0).getName());
        assertEquals("Bob", users.get(1).getName());
    }
}
