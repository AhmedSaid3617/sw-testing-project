package com.example.wbt.path_coverage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.example.Movie;
import com.example.MovieException;
import com.example.MovieParser;

import java.util.List;

// START
//   |
// Read input -> split into lines
// i = 0
// WHILE i < lines.length
//   |
// Read line -> split by comma
// titleAndId.length < 2 ?
//   |----YES----> i++ -> CONTINUE LOOP
//   |
//   NO
//   |
// i++ ; i >= lines.length ? (no genres line)
//   |----YES----> THROW MovieException ---> END
//   |
//   NO
//   |
// Read genres line -> split genres
// genresArray.length > 1 AND genresArray[1] contains digit ?
//   |----YES----> THROW MovieException ---> END
//   |
//   NO
//   |
// Create Movie -> add to list
// i++
// LOOP
//   |
// END (return movies_list)


public class MovieParserClassTest {

    // PATH 1: Empty input -> while condition false -> return empty list
    @Test
    void emptyInput() throws Exception {
        String input = "";

        MovieParser parser = new MovieParser();
        List<Movie> movies = parser.parseMovies(input);

        assertEquals(0, movies.size());
    }

    // PATH 2: Valid single movie -> normal execution
    @Test
    void validSingleMovieParsing() throws Exception {
        String input =
            "The Matrix,TM001\n" +
            "Action,Drama\n";

        MovieParser parser = new MovieParser();
        List<Movie> movies = parser.parseMovies(input);

        assertEquals(1, movies.size());
    }

    // PATH 3: Multiple movies -> loop executes multiple times
    @Test
    void multipleMoviesParsing() throws Exception {
        String input =
            "The Matrix,TM001\nAction\n" +
            "Inception,I002\nSciFi\n";

        MovieParser parser = new MovieParser();
        List<Movie> movies = parser.parseMovies(input);

        assertEquals(2, movies.size());
    }

    // PATH 4: Missing genres line -> exception exit
    @Test
    void missingGenresLine() {
        String input = "The Matrix,TM001\n";

        MovieParser parser = new MovieParser();

        assertThrows(MovieException.class,
            () -> parser.parseMovies(input));
    }

    // PATH 5: Invalid genres containing digits -> exception exit
    @Test
    void invalidGenresWithDigits() {
        String input =
            "The Matrix,TM001\n" +
            "Drama,Action1\n";

        MovieParser parser = new MovieParser();

        assertThrows(MovieException.class,
            () -> parser.parseMovies(input));
    }

}