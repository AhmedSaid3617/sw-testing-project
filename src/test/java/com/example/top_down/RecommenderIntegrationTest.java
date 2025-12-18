package com.example.top_down;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.example.DataStore;
import com.example.Movie;
import com.example.MovieException;
import com.example.Recommender;
import com.example.User;
import com.example.UserException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecommenderIntegrationTest {

    @Test
    void recommendMovies_shouldRecommendMoviesWithLikedGenres() throws UserException, MovieException {

        DataStore dataStore = mock(DataStore.class);

        User user = new User(
                "Mohamed",
                "123456789",
                List.of("TM001")
        );

        Movie likedMovie = new Movie(
                "The Matrix",
                "TM001",
                List.of("Action", "Sci-Fi", "Thriller")
        );

        Movie recommendedMovie = new Movie(
                "Inception",
                "I002",
                List.of("Action", "Sci-Fi", "Mystery")
        );

        Movie unrelatedMovie  = new Movie(
                "The Shawshank Redemption",
                "TSR003",
                List.of("Drama")
        );

        when(dataStore.getMovieById("TM001"))
                .thenReturn(likedMovie);

        when(dataStore.getMovies())
                .thenReturn(List.of(
                        likedMovie,
                        unrelatedMovie,
                        recommendedMovie
                ));

        Recommender recommender = new Recommender(dataStore);

        List<Movie> recommendations =
                recommender.recommendMovies(user);

        assertEquals(1, recommendations.size());
        assertTrue(recommendations.contains(recommendedMovie));
        assertFalse(recommendations.contains(likedMovie));
        assertFalse(recommendations.contains(unrelatedMovie));

        verify(dataStore).getMovieById("TM001");
        verify(dataStore).getMovies();
    }
}