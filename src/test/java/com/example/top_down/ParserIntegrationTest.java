package com.example.top_down;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.example.ParseResult;
import com.example.Parser;
import com.example.User;
import com.example.Movie;
import com.example.CreateFiles;

import java.util.List;

/*Parser Integration Testing calls the User Parser & Movie Parser and outputs 
using Parse Result Class

*/

class ParserIntegrationTest {

    @Test
    void parseIntegrationTestWithFiles() throws Exception {
        // Temporary file paths
        String usersFile = "users.txt";
        String moviesFile = "movies.txt";

        // File content
        String moviesData = 
            "Matrix,M001\nAction|Sci-Fi\n" +
            "Inception,I002\nAction|Thriller\n" +
            "Avatar,A003\nAdventure|Sci-Fi\n";

        String usersData = 
            "Alice,123456789\nM001,I002\n" +
            "Bob,987654321\nM001,A003\n";

        // Create files
        CreateFiles.createFile(moviesFile, moviesData);
        CreateFiles.createFile(usersFile, usersData);

        try {
            Parser parser = new Parser(usersFile, moviesFile);
            ParseResult result = parser.parse();

            // Validate users
            List<User> users = result.getUsers();
            assertEquals(2, users.size());
            assertTrue(users.stream().anyMatch(u -> u.getName().equals("Alice")));
            assertTrue(users.stream().anyMatch(u -> u.getName().equals("Bob")));

            // Validate movies
            List<Movie> movies = result.getMovies();
            assertEquals(3, movies.size());
            assertTrue(movies.stream().anyMatch(m -> m.getId().equals("M001")));
            assertTrue(movies.stream().anyMatch(m -> m.getId().equals("I002")));
            assertTrue(movies.stream().anyMatch(m -> m.getId().equals("A003")));

        } finally {
            // Clean up temp files
             CreateFiles.deleteFile(usersFile);
             CreateFiles.deleteFile(moviesFile);
        }
    }
}
