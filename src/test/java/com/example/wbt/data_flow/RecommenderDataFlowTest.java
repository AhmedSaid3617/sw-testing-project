package com.example.wbt.data_flow;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.DataStore;
import com.example.Movie;
import com.example.Recommender;
import com.example.User;

/**
 * All-DU-Paths tests for {@link com.example.Recommender#recommendMovies(com.example.User)}.
 *
 * Comprehensive coverage of all definition-use paths through the complex nested loops:
 * - Def(likedMoviesIDs) -> loop iterations with different lengths
 * - Def(likedGenres) -> multiple paths via contains() predicate (add unique vs skip duplicate)
 * - Def(recommendations) -> different paths (skip liked, match genre, no match)
 * - Loop variables with varying iteration counts to cover all DU-paths
 */
public class RecommenderDataFlowTest {

    @Test
    void recommendMovies_exercises_skipLiked_continue_and_recommend_break() throws Exception {
        // Exercises both: skip liked (continue) and recommend on genre match (add + break).
        DataStore store = new DataStore();

        Movie liked = new Movie("The Matrix", "TM001", List.of("Action", "SciFi"));
        Movie candidateMatch = new Movie("John Wick", "JW002", List.of("Action"));
        Movie candidateNoMatch = new Movie("Finding Nemo", "FN003", List.of("Animation"));

        store.addMovie(liked);
        store.addMovie(candidateMatch);
        store.addMovie(candidateNoMatch);

        User user = new User("Alice", "123456789", new ArrayList<>(List.of("TM001")));

        Recommender recommender = new Recommender(store);
        List<Movie> recs = recommender.recommendMovies(user);

        assertTrue(recs.contains(candidateMatch));
        assertFalse(recs.contains(liked));
        assertFalse(recs.contains(candidateNoMatch));
    }

    @Test
    void recommendMovies_buildsLikedGenres_uniqueAddPath() throws Exception {
        // Exercises the "unique add" branch: !likedGenres.contains(genre) == true at least once.
        DataStore store = new DataStore();

        Movie liked = new Movie("The Matrix", "TM001", List.of("Action", "Action", "SciFi"));
        Movie candidate = new Movie("Speed", "S002", List.of("Action"));

        store.addMovie(liked);
        store.addMovie(candidate);

        User user = new User("Alice", "123456789", new ArrayList<>(List.of("TM001")));

        Recommender recommender = new Recommender(store);
        List<Movie> recs = recommender.recommendMovies(user);

        assertEquals(1, recs.size());
        assertEquals("S002", recs.get(0).getId());
    }

    @Test
    void recommendMovies_singleLikedMovie_singleGenre() throws Exception {
        // DU-path: def(likedMoviesIDs) size 1 -> loop 1 iteration -> def(genres) size 1 -> likedGenres gets 1 entry.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));
        store.addMovie(new Movie("Speed", "S002", List.of("Action")));

        User user = new User("Alice", "123456789", new ArrayList<>(List.of("TM001")));

        Recommender recommender = new Recommender(store);
        List<Movie> recs = recommender.recommendMovies(user);

        assertEquals(1, recs.size());
        assertEquals("S002", recs.get(0).getId());
    }

    @Test
    void recommendMovies_multipleLikedMovies_multipleGenres() throws Exception {
        // DU-path: multiple iterations building likedGenres with unique adds across different movies.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action", "SciFi")));
        store.addMovie(new Movie("Inception", "I002", List.of("Thriller", "SciFi")));
        store.addMovie(new Movie("Speed", "S003", List.of("Action")));
        store.addMovie(new Movie("Interstellar", "I004", List.of("SciFi")));

        User user = new User("Alice", "123456789", new ArrayList<>(List.of("TM001", "I002")));

        Recommender recommender = new Recommender(store);
        List<Movie> recs = recommender.recommendMovies(user);

        assertTrue(recs.contains(store.getMovieById("S003")));
        assertTrue(recs.contains(store.getMovieById("I004")));
        assertEquals(2, recs.size());
    }

    @Test
    void recommendMovies_duplicateGenres_skipsDuplicateAdd() throws Exception {
        // DU-path: likedGenres.contains(genre) == true -> skip add branch (false path of !contains).
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action", "SciFi")));
        store.addMovie(new Movie("Speed", "S002", List.of("Action", "Thriller")));
        store.addMovie(new Movie("John Wick", "JW003", List.of("Action")));

        User user = new User("Alice", "123456789", new ArrayList<>(List.of("TM001", "S002")));

        Recommender recommender = new Recommender(store);
        List<Movie> recs = recommender.recommendMovies(user);

        // Action appears 3 times total but should only be in likedGenres once
        assertTrue(recs.contains(store.getMovieById("JW003")));
    }

    @Test
    void recommendMovies_noGenreMatches() throws Exception {
        // DU-path: recommendation loop -> likedMovies.contains false -> inner genre loop -> likedGenres.contains always false -> no add.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));
        store.addMovie(new Movie("Finding Nemo", "FN002", List.of("Animation")));
        store.addMovie(new Movie("Frozen", "F003", List.of("Musical")));

        User user = new User("Alice", "123456789", new ArrayList<>(List.of("TM001")));

        Recommender recommender = new Recommender(store);
        List<Movie> recs = recommender.recommendMovies(user);

        assertEquals(0, recs.size());
    }

    @Test
    void recommendMovies_multipleGenres_firstMatches() throws Exception {
        // DU-path: movie has multiple genres -> first genre matches -> add + break (short-circuit).
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("Action")));
        store.addMovie(new Movie("Speed", "S002", List.of("Action", "Thriller", "Drama")));

        User user = new User("Alice", "123456789", new ArrayList<>(List.of("TM001")));

        Recommender recommender = new Recommender(store);
        List<Movie> recs = recommender.recommendMovies(user);

        assertEquals(1, recs.size());
        assertEquals("S002", recs.get(0).getId());
    }

    @Test
    void recommendMovies_multipleGenres_laterMatches() throws Exception {
        // DU-path: movie has multiple genres -> first doesn't match -> later genre matches -> add + break.
        DataStore store = new DataStore();
        store.addMovie(new Movie("The Matrix", "TM001", List.of("SciFi")));
        store.addMovie(new Movie("Speed", "S002", List.of("Thriller", "Action", "SciFi")));

        User user = new User("Alice", "123456789", new ArrayList<>(List.of("TM001")));

        Recommender recommender = new Recommender(store);
        List<Movie> recs = recommender.recommendMovies(user);

        assertEquals(1, recs.size());
        assertEquals("S002", recs.get(0).getId());
    }
}
