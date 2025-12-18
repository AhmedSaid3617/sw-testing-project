package com.example.wbt.data_flow;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.DataIntegrityException;
import com.example.DataStore;
import com.example.Movie;
import com.example.User;

/**
 * All-DU-Paths (All Definition-Use Paths) tests for {@link com.example.DataStore}.
 *
 * These tests achieve comprehensive coverage of all definition-clear paths from
 * each variable definition to all reachable uses (P-uses and C-uses):
 * - addMovie: numericPart def -> loop iterations -> P-use/C-use paths
 * - checkIntegrity: found flag def/redef -> nested loop paths -> multiple uses
 * - getMovieById: movie loop var -> different iteration paths
 *
 * Each test documents the specific DU-path(s) it covers.
 */
public class DataStoreDataFlowTest {

    @Test
    void addMovie_defNumericPart_puseEquals_false_thenAdd() throws Exception {
        // Def(id,numericPart) -> P-use(numericPart.equals(otherNumeric)) false -> C-use(movies.add(movie)).
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));
        store.addMovie(new Movie("Inception", "I002", List.of("SciFi")));
        assertEquals(2, store.getMovies().size());
    }

    @Test
    void addMovie_defOtherNumeric_puseEquals_true_thenThrow() throws Exception {
        // Def(otherNumeric) in loop body -> P-use(numericPart.equals(otherNumeric)) true -> exception.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));
        assertThrows(DataIntegrityException.class,
            () -> store.addMovie(new Movie("Avatar", "A001", List.of("SciFi"))));
    }

    @Test
    void checkIntegrity_defFound_false_puseNotFound_true_thenThrow() throws Exception {
        // Def(found=false) -> P-use(!found) true after inner loop completes without match -> exception.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));

        User user = new User("Hassan", "123456789", List.of("X999"));
        assertThrows(DataIntegrityException.class, () -> store.addUser(user));
    }

    @Test
    void checkIntegrity_duplicateUserLoop_puseEquals_true_thenThrow() throws Exception {
        // Existing users loop: P-use(existingUser.getId().equals(user.getId())) true -> duplicate exception.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));

        User u1 = new User("Hassan", "123456789", List.of("TM001"));
        User u2 = new User("Walid", "123456789", List.of("TM001"));

        store.addUser(u1);
        assertThrows(DataIntegrityException.class, () -> store.addUser(u2));
    }

    @Test
    void addMovie_emptyList_noLoopIteration_directAdd() throws Exception {
        // DU-path: def(numericPart) -> loop condition false (0 iterations) -> C-use(movies.add).
        DataStore store = new DataStore();
        store.addMovie(new Movie("Matrix", "M001", List.of("Action")));
        assertEquals(1, store.getMovies().size());
    }

    @Test
    void addMovie_multipleIterations_allUnique() throws Exception {
        // DU-path: def(numericPart) -> multiple loop iterations with P-use false each time -> C-use(add).
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));
        store.addMovie(new Movie("Inception", "I002", List.of("SciFi")));
        store.addMovie(new Movie("Avatar", "A003", List.of("Adventure")));
        assertEquals(3, store.getMovies().size());
    }

    @Test
    void addMovie_duplicateOnSecondIteration() throws Exception {
        // DU-path: def(numericPart) -> first iteration P-use false -> second iteration P-use true -> throw.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));
        store.addMovie(new Movie("Speed", "S002", List.of("Action")));
        assertThrows(DataIntegrityException.class,
            () -> store.addMovie(new Movie("Avatar", "A001", List.of("SciFi"))));
    }

    @Test
    void checkIntegrity_multipleLikedMovies_allFound() throws Exception {
        // DU-path: multiple outer loop iterations, each with def(found=false) -> inner loop finds -> def(found=true) -> P-use(!found) false.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));
        store.addMovie(new Movie("Inception", "I002", List.of("SciFi")));
        store.addMovie(new Movie("Avatar", "A003", List.of("Adventure")));

        User user = new User("Hassan", "123456789", List.of("TM001", "I002", "A003"));
        store.addUser(user);
        assertEquals(1, store.getUsers().size());
    }

    @Test
    void checkIntegrity_firstMovieNotFound() throws Exception {
        // DU-path: first outer iteration, def(found=false) -> inner loop completes without match -> P-use(!found) true -> throw.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));

        User user = new User("Walid", "123456789", List.of("INVALID", "TM001"));
        assertThrows(DataIntegrityException.class, () -> store.addUser(user));
    }

    @Test
    void checkIntegrity_secondMovieNotFound() throws Exception {
        // DU-path: first outer iteration succeeds, second iteration def(found=false) -> no match -> P-use(!found) true -> throw.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));

        User user = new User("Alice", "123456789", List.of("TM001", "INVALID"));
        assertThrows(DataIntegrityException.class, () -> store.addUser(user));
    }

    @Test
    void checkIntegrity_movieFoundAfterMultipleIterations() throws Exception {
        // DU-path: inner loop iterates multiple times before def(found=true) via break.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));
        store.addMovie(new Movie("Inception", "I002", List.of("SciFi")));
        store.addMovie(new Movie("Avatar", "A003", List.of("Adventure")));

        User user = new User("Hassan", "123456789", List.of("A003"));
        store.addUser(user);
        assertEquals(1, store.getUsers().size());
    }

    @Test
    void checkIntegrity_noExistingUsers_skipsDuplicateCheck() throws Exception {
        // DU-path: outer movie loop completes -> duplicate user loop has 0 iterations -> return true.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));

        User user = new User("Ahmed", "123456789", List.of("TM001"));
        store.addUser(user);
        assertEquals(1, store.getUsers().size());
    }

    @Test
    void checkIntegrity_multipleExistingUsers_noDuplicate() throws Exception {
        // DU-path: duplicate check loop iterates multiple times with P-use(equals) false each time.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));

        store.addUser(new User("Hossam", "123456789", List.of("TM001")));
        store.addUser(new User("Ashraf", "987654321", List.of("TM001")));
        store.addUser(new User("Mohsen", "111111111", List.of("TM001")));

        assertEquals(3, store.getUsers().size());
    }

    @Test
    void getMovieById_foundAtFirstIteration() throws Exception {
        // DU-path: def(movie) in loop -> first iteration P-use(equals) true -> C-use(return movie).
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));
        store.addMovie(new Movie("Inception", "I002", List.of("SciFi")));

        Movie result = store.getMovieById("TM001");
        assertNotNull(result);
        assertEquals("TM001", result.getId());
    }

    @Test
    void getMovieById_foundAtLastIteration() throws Exception {
        // DU-path: multiple iterations with P-use(equals) false -> final iteration P-use true -> C-use(return).
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));
        store.addMovie(new Movie("Inception", "I002", List.of("SciFi")));
        store.addMovie(new Movie("Avatar", "A003", List.of("Adventure")));

        Movie result = store.getMovieById("A003");
        assertNotNull(result);
        assertEquals("A003", result.getId());
    }

    @Test
    void getMovieById_emptyMovieList() {
        // DU-path: loop condition false (0 iterations) -> C-use(return null).
        DataStore store = new DataStore();
        assertNull(store.getMovieById("ANY"));
    }
}
