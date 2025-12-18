package com.example.wbt.path_coverage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.example.User;
import com.example.UserException;
import com.example.UserParser;

import java.util.List;

// START
// Split input lines
// i = 0
// WHILE i < lines.length
//   |
// Split user line by comma
// parts.length != 2 ?
//   |----YES----> THROW UserException ---> END
//   |
// Create User -> add to list
// i+1 < length ?
//   |----NO----> THROW UserException ---> END
//   |
// addLikedMovies(line i+1)
//   |
// Check first liked movie contains digit ?
//   |----NO----> THROW UserException ---> END
//   |
// Add liked movies to user
// i += 2
// LOOP
//   |
// END (return users_list)

public class UserParserClassTest {

    // PATH 1: valid user parsing
    @Test
    void validUserParsing() throws Exception {
        String input = "Alice,123456789\nTM001,TM002\n";
        UserParser parser = new UserParser();
        List<User> users = parser.parseUsers(input);
        assertEquals(1, users.size());
    }

    // PATH 2: invalid user format (no comma)
    @Test
    void invalidUserFormat() {
        String input = "Alice123456789\nTM001\n";
        UserParser parser = new UserParser();
        assertThrows(UserException.class,
            () -> parser.parseUsers(input));
    }

    // PATH 3: invalid liked movies (no digits)
    @Test
    void invalidLikedMovies() {
        String input = "Alice,123456789\nABC,DEF\n";
        UserParser parser = new UserParser();
        assertThrows(UserException.class,
            () -> parser.parseUsers(input));
    }

    // PATH 4: missing liked movies line
    @Test
    void missingLikedMoviesLine() {
        String input = "Alice,123456789\n";
        UserParser parser = new UserParser();
        assertThrows(UserException.class,
            () -> parser.parseUsers(input));
    }
        
    // PATH 5: first liked movie is valid, other isn't 
    @Test
    void firstLikedMovieHasDigitOthersValid() throws Exception {
        String input =
            "Alice,123456789\nTM001,XYZ\n";
        UserParser parser = new UserParser();
        List<User> users = parser.parseUsers(input);
        assertEquals(1, users.size());
    }

    // PATH 6: multiple users parsing
    @Test
    void multipleUsersParsing() throws Exception {
        String input =
            "Alice,123456789\nTM001\n" +
            "Bob,987654321\nTM002\n";
        UserParser parser = new UserParser();
        List<User> users = parser.parseUsers(input);
        assertEquals(2, users.size());
    }

    // PATH 7: empty liked movies line
    @Test
    void emptyLikedMoviesLine() {
        String input =
            "Alice,123456789\n\n";
        UserParser parser = new UserParser();
        assertThrows(UserException.class,
            () -> parser.parseUsers(input));
    }
}