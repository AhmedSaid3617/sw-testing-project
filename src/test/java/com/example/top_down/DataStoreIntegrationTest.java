package com.example.top_down;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.example.DataIntegrityException;
import com.example.DataStore;
import com.example.Movie;
import com.example.ParseResult;
import com.example.User;

class DataStoreIntegrationTest {

    @Test
    void dataStore_shouldAcceptValidMoviesAndUsers() throws Exception {

        Movie movie1 = new Movie(
                "The Matrix",
                "TM001",
                List.of("Action", "Sci-Fi")
        );

        Movie movie2 = new Movie(
                "John Wick",
                "JW002",
                List.of("Action")
        );

        User user = new User(
                "Mohamed",
                "123456789",
                List.of("TM001")
        );

        ParseResult parseResult = new ParseResult(
                List.of(movie1, movie2),
                List.of(user)
        );

        DataStore dataStore = new DataStore(parseResult);

        assertEquals(2, dataStore.getMovies().size());
        assertEquals(1, dataStore.getUsers().size());

        assertEquals(movie1, dataStore.getMovieById("TM001"));
    }

    @Test
    void dataStore_shouldFailIfUserReferencesUnknownMovie() throws Exception {

        Movie movie = new Movie(
                "The Matrix",
                "TM001",
                List.of("Action")
        );

        User invalidUser = new User(
                "Mohamed",
                "123456789",
                List.of("INVALID_ID")
        );

        ParseResult parseResult = new ParseResult(
                List.of(movie),
                List.of(invalidUser)
        );

        assertThrows(DataIntegrityException.class, () -> {
            new DataStore(parseResult);
        });
    }

    @Test
    void dataStore_shouldFailOnDuplicateMovieNumericIds() throws Exception  {

        Movie movie1 = new Movie(
                "The Matrix",
                "TM001",
                List.of("Action")
        );

        Movie movie2 = new Movie(
                "John Wick",
                "JW001", // same numeric part
                List.of("Action")
        );

        ParseResult parseResult = new ParseResult(
                List.of(movie1, movie2),
                List.of()
        );

        assertThrows(DataIntegrityException.class, () -> {
            new DataStore(parseResult);
        });
    }


}


