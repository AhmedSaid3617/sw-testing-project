package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class RecommenderTest {
    private Recommender recommender;
    private DataStore mockDataStore;
    private User testUser;

    @BeforeEach
    void setUp() {
        mockDataStore = new DataStore();

    }

    @Test
    void shouldRecommendMoviesBasedOnLikedGenres() {

        Movie movie1 = new Movie("Action Movie", "1", List.of("Action", "Adventure"));
        Movie movie2 = new Movie("Romantic Movie", "2", List.of("Romance", "Drama"));
        Movie movie3 = new Movie("Sci-Fi Movie", "3", List.of("Sci-Fi", "Action"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);
        mockDataStore.addMovie(movie3);

        testUser = new User("u1", "Test User", new ArrayList<>());
        testUser.addLikedMovie(movie1);
        testUser.addLikedMovie(movie2);

        recommender = new Recommender(mockDataStore);
        // Act
        List<Movie> recommendations = recommender.recommendMovies(testUser);
        for (Movie movie : recommendations) {
            System.out.println(movie.getTitle());
        }
        // Assert
        assertEquals(1, recommendations.size());
        assertEquals("3", recommendations.get(0).getId()); // Sci-Fi Movie should be recommended
    }

    @Test
    void manyMovies() {

        List<Movie> movies = List.of(
            new Movie("Interstellar", "1", List.of("Sci-Fi", "Drama", "Action")),
            new Movie("The Dark Knight", "2", List.of("Action", "Drama")),
            new Movie("One Battle After Another", "3", List.of("Action", "War")),
            new Movie("Juror #2", "4", List.of("Mystery", "Thriller"))
        );

        for (Movie movie : movies) {
            mockDataStore.addMovie(movie);
        }

        recommender = new Recommender(mockDataStore);

        testUser = new User("u2", "Another User", new ArrayList<>());
        testUser.addLikedMovie(movies.get(0)); // Likes Interstellar

        List<Movie> recommendations = recommender.recommendMovies(testUser);

        assertEquals(recommendations, movies.subList(1, 3)); // Should recommend The Dark Knight and One Battle After Another

        for (Movie movie : recommendations) {
            System.out.println(movie.getTitle());
        }
    }

    @Test

    void noRecommendationsForUniqueTaste() {
        Movie movie1 = new Movie("Horror Movie", "1", List.of("Horror"));
        Movie movie2 = new Movie("Documentary", "2", List.of("Documentary"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);

        testUser = new User("u3", "Unique Taste User", new ArrayList<>());
        testUser.addLikedMovie(new Movie("Indie Film", "3", List.of("Indie")));

        recommender = new Recommender(mockDataStore);
        // Act
        List<Movie> recommendations = recommender.recommendMovies(testUser);
        // Assert
        assertTrue(recommendations.isEmpty());
    }

    @Test
    void noRecommendationsWhenNoLikedMovies() {
        Movie movie1 = new Movie("Comedy Movie", "1", List.of("Comedy"));
        Movie movie2 = new Movie("Drama Movie", "2", List.of("Drama"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);

        testUser = new User("u4", "No Likes User", new ArrayList<>());

        recommender = new Recommender(mockDataStore);
        // Act
        List<Movie> recommendations = recommender.recommendMovies(testUser);
        // Assert
        assertTrue(recommendations.isEmpty());
    }

    @Test
    void noRecommendationsWhenAllMoviesLiked() {
        Movie movie1 = new Movie("Thriller Movie", "1", List.of("Thriller"));
        Movie movie2 = new Movie("Action Movie", "2", List.of("Action"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);

        testUser = new User("u5", "All Likes User", new ArrayList<>());
        testUser.addLikedMovie(movie1);
        testUser.addLikedMovie(movie2);

        recommender = new Recommender(mockDataStore);
        // Act
        List<Movie> recommendations = recommender.recommendMovies(testUser);
        // Assert
        assertTrue(recommendations.isEmpty());
    }

    @Test
    void noRecommendationsEmptyDataStore() {
        testUser = new User("u6", "Empty DataStore User", new ArrayList<>());
        testUser.addLikedMovie(new Movie("Some Movie", "1", List.of("Genre")));

        recommender = new Recommender(mockDataStore);
        // Act
        List<Movie> recommendations = recommender.recommendMovies(testUser);
        // Assert
        assertTrue(recommendations.isEmpty());
    }

    @Test
    void multipleUsers() {
        Movie movie1 = new Movie("Fantasy Movie", "1", List.of("Fantasy", "Classic"));
        Movie movie2 = new Movie("Adventure Movie", "2", List.of("Adventure"));
        Movie movie3 = new Movie("Sci-Fi Movie", "3", List.of("Sci-Fi", "Adventure"));
        Movie movie4 = new Movie("Horror Movie", "4", List.of("Horror", "Classic"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);
        mockDataStore.addMovie(movie3);
        mockDataStore.addMovie(movie4);

        User userA = new User("u7", "User A", new ArrayList<>());
        userA.addLikedMovie(movie1);

        User userB = new User("u8", "User B", new ArrayList<>());
        userB.addLikedMovie(movie3);

        recommender = new Recommender(mockDataStore);

        List<Movie> recommendationsA = recommender.recommendMovies(userA);
        List<Movie> recommendationsB = recommender.recommendMovies(userB);

        assertEquals(recommendationsA, List.of(movie4)); // Recommendations for User A

        assertEquals(recommendationsB, List.of(movie2)); // No recommendations for User B
    }

    @Test
    void multipleUsersSharedMovies() {
        Movie movie1 = new Movie("Fantasy Movie", "1", List.of("Fantasy", "Classic"));
        Movie movie2 = new Movie("Adventure Movie", "2", List.of("Adventure"));
        Movie movie3 = new Movie("Sci-Fi Movie", "3", List.of("Sci-Fi", "Adventure"));
        Movie movie4 = new Movie("Horror Movie", "4", List.of("Horror", "Classic"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);
        mockDataStore.addMovie(movie3);
        mockDataStore.addMovie(movie4);

        User userA = new User("u7", "User A", new ArrayList<>());
        userA.addLikedMovie(movie2);

        User userB = new User("u8", "User B", new ArrayList<>());
        userB.addLikedMovie(movie2);
        userB.addLikedMovie(movie4);

        recommender = new Recommender(mockDataStore);

        List<Movie> recommendationsA = recommender.recommendMovies(userA);
        List<Movie> recommendationsB = recommender.recommendMovies(userB);

        assertEquals(recommendationsA, List.of(movie3)); // Recommendations for User A

        assertEquals(recommendationsB, List.of(movie1, movie3)); // No recommendations for User B
    }
}
