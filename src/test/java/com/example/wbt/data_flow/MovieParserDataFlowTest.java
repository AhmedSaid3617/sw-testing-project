package com.example.wbt.data_flow;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.Movie;
import com.example.MovieException;
import com.example.MovieParser;

/**
 * All-DU-Paths tests for {@link com.example.MovieParser#parseMovies(String)}.
 *
 * Comprehensive coverage of all definition-use paths in the while-loop parsing:
 * - Def(i) -> multiple redefinitions -> different P-use paths through loop conditions
 * - Def(titleAndId) -> P-use(length check) true/false paths
 * - Def(genresArray) -> P-use(length and regex checks) covering all branches
 * - Variable redefinitions within loop iterations
 */
public class MovieParserDataFlowTest {

    @Test
    void parseMovies_puseTitleAndIdLength_true_skipsLine_thenParsesNext() throws Exception {
        // P-use(titleAndId.length < 2) true -> increments i and continues; then normal parse path.
        MovieParser parser = new MovieParser();

        String data = "BadLineWithoutComma\n" +
                      "The Matrix,TM001\n" +
                      "Action,SciFi\n";

        List<Movie> movies = parser.parseMovies(data);
        assertEquals(1, movies.size());
        assertEquals("TM001", movies.get(0).getId());
    }

    @Test
    void parseMovies_puseMissingGenres_true_throws() {
        // P-use(i >= lines.length) true right after reading header -> throws missing-genres exception.
        MovieParser parser = new MovieParser();

        String data = "The Matrix,TM001\n";
        assertThrows(MovieException.class, () -> parser.parseMovies(data));
    }

    @Test
    void parseMovies_puseInvalidGenres_secondTokenHasDigit_throws() {
        // P-use(genresArray[1].matches(".*\\d.*")) true -> throws invalid-genres exception.
        MovieParser parser = new MovieParser();

        String data = "The Matrix,TM001\n" +
                      "Action,Sc1Fi\n";

        assertThrows(MovieException.class, () -> parser.parseMovies(data));
    }

    @Test
    void parseMovies_singleMovie() throws Exception {
        // DU-path: def(i) -> one complete iteration -> redef(i) -> loop exits.
        MovieParser parser = new MovieParser();

        String data = "Matrix,M001\n" +
                      "Action\n";

        List<Movie> movies = parser.parseMovies(data);
        assertEquals(1, movies.size());
        assertEquals("M001", movies.get(0).getId());
    }

    @Test
    void parseMovies_multipleBadLines_skipsAll() throws Exception {
        // DU-path: def(i) -> multiple iterations with titleAndId.length < 2 -> redef(i) each time -> then valid parse.
        MovieParser parser = new MovieParser();

        String data = "BadLine1\n" +
                      "BadLine2\n" +
                      "BadLine3\n" +
                      "The Matrix,TM001\n" +
                      "Action\n";

        List<Movie> movies = parser.parseMovies(data);
        assertEquals(1, movies.size());
        assertEquals("TM001", movies.get(0).getId());
    }

    @Test
    void parseMovies_genresWithOneToken() throws Exception {
        // DU-path: def(genresArray) -> P-use(genresArray.length > 1) false -> skip digit check -> success.
        MovieParser parser = new MovieParser();

        String data = "Matrix,M001\n" +
                      "Action\n";

        List<Movie> movies = parser.parseMovies(data);
        assertEquals(1, movies.size());
        assertEquals(List.of("Action"), movies.get(0).getGenres());
    }

    @Test
    void parseMovies_genresWithMultipleTokens_noDigit() throws Exception {
        // DU-path: def(genresArray) -> P-use(length > 1) true -> P-use(matches digit) false -> success.
        MovieParser parser = new MovieParser();

        String data = "The Matrix,TM001\n" +
                      "Action,SciFi,Thriller\n";

        List<Movie> movies = parser.parseMovies(data);
        assertEquals(1, movies.size());
        assertEquals(List.of("Action", "SciFi", "Thriller"), movies.get(0).getGenres());
    }

    @Test
    void parseMovies_genresSecondTokenDigitInMiddle() {
        // DU-path: def(genresArray) -> P-use(genresArray[1].matches(".*\\d.*")) true -> throw.
        MovieParser parser = new MovieParser();

        String data = "Matrix,M001\n" +
                      "Action,Sc1iFi\n";

        assertThrows(MovieException.class, () -> parser.parseMovies(data));
    }

    @Test
    void parseMovies_incrementI_reachesLength() {
        // DU-path: def(i) incremented after title -> redef(i) -> P-use(i >= lines.length) true -> throw.
        MovieParser parser = new MovieParser();

        String data = "Matrix,M001\n";
        assertThrows(MovieException.class, () -> parser.parseMovies(data));
    }

    @Test
    void parseMovies_iIncrementedTwicePerIteration() throws Exception {
        // DU-path: def(i) -> i++ after title read -> i++ after genres read -> next iteration.
        MovieParser parser = new MovieParser();

        String data = "Movie One,MO001\n" +
                      "Action\n" +
                      "Movie Two,MT002\n" +
                      "Drama\n";

        List<Movie> movies = parser.parseMovies(data);
        assertEquals(2, movies.size());
    }
}
