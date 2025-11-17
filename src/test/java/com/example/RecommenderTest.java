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
        Movie movie1 = new Movie( "Action Movie", "1",List.of("Action", "Adventure"));
        Movie movie2 = new Movie( "Romantic Movie","2", List.of("Romance", "Drama"));
        Movie movie3 = new Movie( "Sci-Fi Movie", "3",List.of("Sci-Fi", "Action"));
        mockDataStore.addMovie(movie1);
        mockDataStore.addMovie(movie2);
        mockDataStore.addMovie(movie3);

        testUser = new User("u1", "Test User", new ArrayList<>());
        testUser.addLikedMovie(movie1);
        testUser.addLikedMovie(movie2);

        recommender = new Recommender(mockDataStore);
    }

    @Test
    void shouldRecommendMoviesBasedOnLikedGenres() {
        // Act
        List<Movie> recommendations = recommender.recommendMovies(testUser);
        for (Movie movie : recommendations) {
            System.out.println(movie.getTitle());
        }
        // Assert
        assertEquals(1, recommendations.size());
        assertEquals("3", recommendations.get(0).getId()); // Sci-Fi Movie should be recommended
    }
}

