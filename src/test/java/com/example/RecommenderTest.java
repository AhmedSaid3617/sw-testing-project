package com.example;

import static org.junit.jupiter.api.Assertions.*;

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
    void shouldRecommendMoviesBasedOnLikedGenres() throws Exception {

        Movie movie1 = new Movie("Action Movie", "AM123", List.of("Action", "Adventure"));
        Movie movie2 = new Movie("Romantic Movie", "RM456", List.of("Romance", "Drama"));
        Movie movie3 = new Movie("Scifi Movie", "SM789", List.of("Sci-Fi", "Action"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);
        mockDataStore.addMovie(movie3);

        testUser = new User("Test User", "123456789", new ArrayList<>());
        testUser.addLikedMovie("AM123");
        testUser.addLikedMovie("RM456");

        recommender = new Recommender(mockDataStore);
        // Act
        List<Movie> recommendations = recommender.recommendMovies(testUser);
        for (Movie movie : recommendations) {
            System.out.println(movie.getTitle());
        }
        // Assert
        assertEquals(1, recommendations.size());
        assertEquals("SM789", recommendations.get(0).getId()); // Scifi Movie should be recommended
    }

    @Test
    void manyMovies() throws Exception {

        List<Movie> movies = List.of(
            new Movie("Interstellar", "I123", List.of("Sci-Fi", "Drama", "Action")),
            new Movie("The Dark Knight", "TDK456", List.of("Action", "Drama")),
            new Movie("One Battle After Another", "OBAA789", List.of("Action", "War")),
            new Movie("Juror Two", "JT012", List.of("Mystery", "Thriller"))
        );

        for (Movie movie : movies) {
            mockDataStore.addMovie(movie);
        }

        recommender = new Recommender(mockDataStore);

        testUser = new User("Another User", "987654321", new ArrayList<>());
        testUser.addLikedMovie("I123"); // Likes Interstellar

        List<Movie> recommendations = recommender.recommendMovies(testUser);

        assertEquals(recommendations, movies.subList(1, 3)); // Should recommend The Dark Knight and One Battle After Another

        for (Movie movie : recommendations) {
            System.out.println(movie.getTitle());
        }
    }

    @Test
    void noRecommendationsForUniqueTaste() throws Exception {
        Movie movie1 = new Movie("Horror Movie", "HM123", List.of("Horror"));
        Movie movie2 = new Movie("Documentary", "D456", List.of("Documentary"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);

        testUser = new User("Unique Taste User", "111222333", new ArrayList<>());
        Movie indieFilm = new Movie("Indie Film", "IF789", List.of("Indie"));
        mockDataStore.addMovie(indieFilm);
        testUser.addLikedMovie("IF789");

        recommender = new Recommender(mockDataStore);
        // Act
        List<Movie> recommendations = recommender.recommendMovies(testUser);
        // Assert
        assertTrue(recommendations.isEmpty());
    }

    @Test
    void noRecommendationsWhenNoLikedMovies() throws Exception {
        Movie movie1 = new Movie("Comedy Movie", "CM123", List.of("Comedy"));
        Movie movie2 = new Movie("Drama Movie", "DM456", List.of("Drama"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);

        testUser = new User("No Likes User", "444555666", new ArrayList<>());

        recommender = new Recommender(mockDataStore);
        // Act
        List<Movie> recommendations = recommender.recommendMovies(testUser);
        // Assert
        assertTrue(recommendations.isEmpty());
    }

    @Test
    void noRecommendationsWhenAllMoviesLiked() throws Exception {
        Movie movie1 = new Movie("Thriller Movie", "TM123", List.of("Thriller"));
        Movie movie2 = new Movie("Action Movie", "AM456", List.of("Action"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);

        testUser = new User("All Likes User", "777888999", new ArrayList<>());
        testUser.addLikedMovie("TM123");
        testUser.addLikedMovie("AM456");

        recommender = new Recommender(mockDataStore);
        // Act
        List<Movie> recommendations = recommender.recommendMovies(testUser);
        // Assert
        assertTrue(recommendations.isEmpty());
    }

    @Test
    void noRecommendationsEmptyDataStore() throws Exception {
        testUser = new User("Empty DataStore User", "123123123", new ArrayList<>());
        Movie someMovie = new Movie("Some Movie", "SM123", List.of("Genre"));
        mockDataStore.addMovie(someMovie);
        testUser.addLikedMovie("SM123");

        recommender = new Recommender(mockDataStore);
        // Act
        List<Movie> recommendations = recommender.recommendMovies(testUser);
        // Assert
        assertTrue(recommendations.isEmpty());
    }

    @Test
    void multipleUsers() throws Exception {
        Movie movie1 = new Movie("Fantasy Movie", "FM123", List.of("Fantasy", "Classic"));
        Movie movie2 = new Movie("Adventure Movie", "AM456", List.of("Adventure"));
        Movie movie3 = new Movie("Scifi Movie", "SM789", List.of("Sci-Fi", "Adventure"));
        Movie movie4 = new Movie("Horror Movie", "HM012", List.of("Horror", "Classic"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);
        mockDataStore.addMovie(movie3);
        mockDataStore.addMovie(movie4);

        User userA = new User("User A", "111111111", new ArrayList<>());
        userA.addLikedMovie("FM123");

        User userB = new User("User B", "222222222", new ArrayList<>());
        userB.addLikedMovie("SM789");

        recommender = new Recommender(mockDataStore);

        List<Movie> recommendationsA = recommender.recommendMovies(userA);
        List<Movie> recommendationsB = recommender.recommendMovies(userB);

        assertEquals(recommendationsA, List.of(movie4)); // Recommendations for User A

        assertEquals(recommendationsB, List.of(movie2)); // No recommendations for User B
    }

    @Test
    void multipleUsersSharedMovies() throws Exception {
        Movie movie1 = new Movie("Fantasy Movie", "FM123", List.of("Fantasy", "Classic"));
        Movie movie2 = new Movie("Adventure Movie", "AM456", List.of("Adventure"));
        Movie movie3 = new Movie("Scifi Movie", "SM789", List.of("Sci-Fi", "Adventure"));
        Movie movie4 = new Movie("Horror Movie", "HM012", List.of("Horror", "Classic"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);
        mockDataStore.addMovie(movie3);
        mockDataStore.addMovie(movie4);

        User userA = new User("User A", "333333333", new ArrayList<>());
        userA.addLikedMovie("AM456");

        User userB = new User("User B", "444444444", new ArrayList<>());
        userB.addLikedMovie("AM456");
        userB.addLikedMovie("HM012");

        recommender = new Recommender(mockDataStore);

        List<Movie> recommendationsA = recommender.recommendMovies(userA);
        List<Movie> recommendationsB = recommender.recommendMovies(userB);

        assertEquals(recommendationsA, List.of(movie3)); // Recommendations for User A

        assertEquals(recommendationsB, List.of(movie1, movie3)); // No recommendations for User B
    }
}
