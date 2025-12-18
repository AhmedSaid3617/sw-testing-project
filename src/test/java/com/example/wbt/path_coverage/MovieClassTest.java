package com.example.wbt.path_coverage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.example.Movie;
import com.example.MovieException;

import java.util.List;

// START
//   |
//   v
// Check genres == null OR empty ?
//   |----YES----> THROW MovieException ---> END
//   |
//   NO
//   |
// Strip numbers from title
//   |
// Check title format (capitalization) ?
//   |----NO----> THROW MovieException ---> END
//   |
//   YES
//   |
// Extract capital letters from title
// Extract letters & numbers from ID
// Check ID letters == title letters AND number length == 3 ?
//   |----NO----> THROW MovieException ---> END
//   |
//   YES
//   |
// Assign title, id, genres
//   |
// END


public class MovieClassTest {

    // PATH 1: genres == null : exception
    @Test
    void nullGenres() {
        assertThrows(MovieException.class,
            () -> new Movie("The Matrix", "TM001", null));
    }

    // PATH 2: genres empty : exception
    @Test
    void emptyGenreList() {
        assertThrows(MovieException.class,
            () -> new Movie("The Matrix", "TM001", List.of()));
    }

    // PATH 3: invalid title capitalization : exception
    @Test
    void invalidTitleLowercase() {
        assertThrows(MovieException.class,
            () -> new Movie("the Matrix", "TM001", List.of("Action")));
    }

    // PATH 4: invalid ID letters mismatch
    @Test
    void invalidIdLetters() {
        assertThrows(MovieException.class,
            () -> new Movie("The Matrix", "MX001", List.of("Action")));
    }

    // PATH 5: invalid ID number length < 3
    @Test
    void invalidIdTooShort() {
        assertThrows(MovieException.class,
            () -> new Movie("The Matrix", "TM01", List.of("Action")));
    }

    // PATH 6: invalid ID number length > 3
    @Test
    void invalidIdTooLong() {
        assertThrows(MovieException.class,
            () -> new Movie("The Matrix", "TM0011", List.of("Action")));
    }

    // PATH 7: valid simple movie
    @Test
    void validMovie() throws Exception {
        Movie m = new Movie("The Matrix", "TM001", List.of("Action"));
        assertNotNull(m);
    }

    // PATH 8: valid complex title
    @Test
    void validComplexTitle() throws Exception {
        Movie m = new Movie("Spider Man Homecoming", "SMH001", List.of("Action"));
        assertNotNull(m);
    }

    // PATH 9: valid title containing digits
    @Test
    void validTitleWithNumbers() throws Exception {
        Movie m = new Movie("Se7en", "S001", List.of("Thriller"));
        assertNotNull(m);
    }

    // PATH 10: valid movie with multiple genres
    @Test
    void validMultipleGenres() throws Exception {
        Movie m = new Movie("Inception", "I002", List.of("SciFi", "Thriller"));
        assertNotNull(m);
    }
}

