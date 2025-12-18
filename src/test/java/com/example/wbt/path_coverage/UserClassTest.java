package com.example.wbt.path_coverage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.example.User;
import com.example.UserException;

import java.util.List;


// START
//   |
// Check name syntax ?
//   |----NO----> THROW UserException ---> END
//   |
// Check ID matches syntax & length == 9 ?
//   |----NO----> THROW UserException ---> END
//   |
// Assign name, id, likedMovies
//   |
// END


public class UserClassTest {

    // PATH 1: All valid
    @Test
    void validUser() throws Exception {
        User user = new User("Alice", "123456789", List.of("TM001"));
        assertEquals("Alice", user.getName());
    }

    // PATH 2: Name validation fails
    @Test
    void invalidUserName() {
        assertThrows(UserException.class,
            () -> new User("Al1ce", "123456789", List.of("TM001")));
    }

    // PATH 3: ID syntax fails
    @Test
    void invalidUserIdSyntax() {
        assertThrows(UserException.class,
            () -> new User("Alice", "ABCDEF123", List.of("TM001")));
    }

    // PATH 4: ID syntax passes, length fails
    @Test
    void invalidUserIdLength() {
        assertThrows(UserException.class,
            () -> new User("Alice", "12345A", List.of("TM001")));
    }

    // PATH 5: Null name
    @Test
    void nullUserName() {
        assertThrows(NullPointerException.class,
            () -> new User(null, "123456789", List.of("TM001")));
    }

    // PATH 6: Null ID 
    @Test
    void nullUserId() {
        assertThrows(NullPointerException.class,
            () -> new User("Alice", null, List.of("TM001")));
    }
}
