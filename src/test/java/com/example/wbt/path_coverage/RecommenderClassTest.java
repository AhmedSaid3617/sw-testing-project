package com.example.wbt.path_coverage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.example.DataStore;
import com.example.Movie;
import com.example.Recommender;
import com.example.User;

import java.util.List;
import java.util.ArrayList;


// START
// Get liked movies -> likedMovies list
// FOR each liked movie
//   |
// Collect genres -> likedGenres list
// END FOR
// FOR each movie in dataStore
//   |
// IF movie in likedMovies -> skip
// ELSE
//   |
// IF any genre in likedGenres -> add to recommendations
// END FOR
// RETURN recommendations
// END


public class RecommenderClassTest {

    // PATH 1: user has liked movies, one recommendation matches genre
    @Test
    void recommendSingleMovie() throws Exception {
        DataStore store = new DataStore();
        Movie m1 = new Movie("Matrix", "M001", List.of("Action"));
        Movie m2 = new Movie("Inception", "I002", List.of("Action", "SciFi"));
        store.addMovie(m1);
        store.addMovie(m2);
        User user = new User("Alice", "123456789", List.of("M001"));
        store.addUser(user);

        Recommender rec = new Recommender(store);
        List<Movie> recommendations = rec.recommendMovies(user);

        assertEquals(1, recommendations.size());
        assertEquals("Inception", recommendations.get(0).getTitle());
    }

    // PATH 2: user has liked movies, but no movies match genres → empty recommendations
    @Test
    void recommendNoMatchingGenres() throws Exception {
        DataStore store = new DataStore();
        Movie m1 = new Movie("Matrix", "M001", List.of("Action"));
        Movie m2 = new Movie("Inception", "I002", List.of("SciFi"));
        store.addMovie(m1);
        store.addMovie(m2);
        User user = new User("Alice", "123456789", List.of("M001"));
        store.addUser(user);

        Recommender rec = new Recommender(store);
        List<Movie> recommendations = rec.recommendMovies(user);

        // Only movie not liked is Inception, genres: SciFi, liked genres: Action → skip
        assertEquals(0, recommendations.size());
    }

    // PATH 3: user has no liked movies → no recommendations
    @Test
    void recommendNoLikedMovies() throws Exception {
        DataStore store = new DataStore();
        Movie m1 = new Movie("Matrix", "M001", List.of("Action"));
        store.addMovie(m1);
        User user = new User("Alice", "123456789", new ArrayList<>());
        store.addUser(user);

        Recommender rec = new Recommender(store);
        List<Movie> recommendations = rec.recommendMovies(user);

        assertEquals(0, recommendations.size());
    }

    // PATH 4: multiple liked movies, multiple recommendations
    @Test
    void recommendMultipleMovies() throws Exception {
        DataStore store = new DataStore();
        Movie m1 = new Movie("Matrix", "M001", List.of("Action", "SciFi"));
        Movie m2 = new Movie("Inception", "I002", List.of("Action", "SciFi"));
        Movie m3 = new Movie("Avatar", "A003", List.of("SciFi", "Adventure"));
        store.addMovie(m1);
        store.addMovie(m2);
        store.addMovie(m3);

        User user = new User("Alice", "123456789", List.of("M001"));
        store.addUser(user);

        Recommender rec = new Recommender(store);
        List<Movie> recommendations = rec.recommendMovies(user);

        // Recommended: Inception (Action,SciFi), Avatar (SciFi,Adventure)
        assertEquals(2, recommendations.size());
        assertTrue(recommendations.stream().anyMatch(m -> m.getTitle().equals("Inception")));
        assertTrue(recommendations.stream().anyMatch(m -> m.getTitle().equals("Avatar")));
    }

    // PATH 5: all movies already liked -> empty recommendations
    @Test
    void recommendAllMoviesLiked() throws Exception {
        DataStore store = new DataStore();
        Movie m1 = new Movie("Matrix", "M001", List.of("Action"));
        store.addMovie(m1);
        User user = new User("Alice", "123456789", List.of("M001"));
        store.addUser(user);

        Recommender rec = new Recommender(store);
        List<Movie> recommendations = rec.recommendMovies(user);

        assertEquals(0, recommendations.size());
    }
}
