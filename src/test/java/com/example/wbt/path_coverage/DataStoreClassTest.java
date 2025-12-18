package com.example.wbt.path_coverage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.example.DataIntegrityException;
import com.example.DataStore;
import com.example.Movie;
import com.example.User;

import java.util.List;

// addMovie path coverage test:

// START
// Extract numeric part of movie ID
// FOR each movie in movies
//   |
// numericPart equals other movie numericPart ?
//   |----YES----> THROW DataIntegrityException ---> END
//   |
// END FOR
// Add movie to movies list
// END

//addUser path coverage test: 


// START
// checkIntegrity(user)
//   |----RETURNS true----> Add user to users list ---> END
//   |----THROWS exception----> DataIntegrityException ---> END


//checkIntegrity path coverage test:

// START
// FOR each liked movie ID
//   |
// Movie exists in movies list ?
//   |----NO----> THROW DataIntegrityException ---> END
//   |
// END FOR
// FOR each existing user
//   |
// User ID duplicate ?
//   |----YES----> THROW DataIntegrityException ---> END
//   |
// END FOR
// RETURN true
// END

//getMovieById path coverage test:

// START
// FOR each movie in movies
//   |
// Movie ID equals checked ID ?
//   |----YES----> RETURN movie ---> END
//   |
// END FOR
// RETURN null (not found)
// END

public class DataStoreClassTest {

    // ===================== addMovie =====================

    // PATH 1: add first movie (loop 0 times)
    @Test
    void addMovieFirst() throws Exception {
        DataStore store = new DataStore();
        Movie m1 = new Movie("Matrix", "M001", List.of("Action"));
        store.addMovie(m1);
        assertEquals(1, store.getMovies().size());
    }

    // PATH 2: add multiple unique movies
    @Test
    void addMovieUnique() throws Exception {
        DataStore store = new DataStore();
        store.addMovie(new Movie("Matrix", "M001", List.of("Action")));
        store.addMovie(new Movie("Inception", "I002", List.of("SciFi")));
        assertEquals(2, store.getMovies().size());
    }

    // PATH 3: duplicate numeric part -> exception
    @Test
    void addMovieDuplicateNumeric() throws Exception {
        DataStore store = new DataStore();
        store.addMovie(new Movie("Matrix", "M001", List.of("Action")));
        Movie duplicate = new Movie("Avatar", "A001", List.of("SciFi"));
        assertThrows(DataIntegrityException.class,
            () -> store.addMovie(duplicate));
    }

    // ===================== addUser =====================

    // PATH 4: add valid user
    @Test
    void addUserValid() throws Exception {
        DataStore store = new DataStore();
        store.addMovie(new Movie("Matrix", "M001", List.of("Action")));

        User u = new User("Alice", "123456789", List.of("M001"));
        store.addUser(u);
        assertEquals(1, store.getUsers().size());
    }

    // PATH 5: addUser fails integrity
    @Test
    void addUserInvalid() throws Exception {
        DataStore store = new DataStore();
        User u = new User("Alice", "123456789", List.of("M001"));
        assertThrows(DataIntegrityException.class,
            () -> store.addUser(u));
    }

    // Path 6 : duplicate user id 
    @Test
    void addUserDuplicateId() throws Exception {
        DataStore store = new DataStore();
        store.addMovie(new Movie("Matrix", "M001", List.of("Action")));

        User u1 = new User("Alice", "123456789", List.of("M001"));
        User u2 = new User("Bob", "123456789", List.of("M001"));

        store.addUser(u1);
        assertThrows(DataIntegrityException.class, () -> store.addUser(u2));
    }

    // ===================== getMovieById =====================

    // PATH 7: movie exists
    @Test
    void getMovieByIdExists() throws Exception {
        DataStore store = new DataStore();
        Movie m = new Movie("Matrix", "M001", List.of("Action"));
        store.addMovie(m);
        assertNotNull(store.getMovieById("M001"));
    }

    // PATH 8: movie does not exist
    @Test
    void getMovieByIdNotFound() {
        DataStore store = new DataStore();
        assertNull(store.getMovieById("X999"));
    }


}
            