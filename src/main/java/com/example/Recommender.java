package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides movie recommendation functionalities.
 */
public class Recommender {
    /**
     * The data source containing user and movie information.
     */
    private DataStore data;

    /**
     * Constructs a new Recommender instance.
     *
     * @param data The data store to use for generating recommendations.
     */
    public Recommender(DataStore data) {
        this.data = data;
    }

    /**
     * Generates movie recommendations for a specific user.
     *
     * @param user The user for whom to generate recommendations.
     */
    public List<Movie> recommendMovies(User user) {
        // Get the list of movies the user likes
        List<Movie> likedMovies = user.getLikedMovies();
        // Get List of liked genres
        List<String> likedGenres = new ArrayList<>();
        for (Movie movie : likedMovies) {
            List<String> genres = movie.getGenres();
            for (String genre : genres) {
                if (!likedGenres.contains(genre)) {
                    likedGenres.add(genre);
                }
            }
        }
        // Recommend movies based on liked genres and exclude already liked movies
        List<Movie> recommendations = new ArrayList<>();
        for (Movie movie : data.getMovies()) {
            if (likedMovies.contains(movie)) continue;
            for (String genre : movie.getGenres()) {
                if (likedGenres.contains(genre)) {
                    recommendations.add(movie);
                    break;
                }
            }
        }
        return recommendations;
    }
}