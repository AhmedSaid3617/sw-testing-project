package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an in-memory data store for managing users and movies.
 * This class provides functionalities to add users and movies while ensuring
 * data integrity.
 */
public class DataStore {

    private List<User> users;
    private List<Movie> movies;

    /**
     * Constructs a new DataStore object.
     * This initializes the user and movie lists as empty ArrayLists.
     */
    public DataStore() {
        this.users = new ArrayList<>();
        this.movies = new ArrayList<>();
    }

    /**
     * Adds a new user to the data store after validating its integrity.
     * The user is only added if it passes the {@code checkIntegrity} validation.
     *
     * @param user The user object to be added to the data store.
     * @throws IllegalArgumentException if the user's data fails the integrity check,
     *                                  preventing the addition of a user with invalid or inconsistent data.
     */
    public void addUser(User user) {
        if (checkIntegrity(user)) {
            this.users.add(user);
        } else {
            throw new IllegalArgumentException("User data integrity check failed for user ID: " + user.getId());
        }
    }

    /**
     * Adds a movie to the data store.
     *
     * @param movie the movie object to be added.
     */
    public void addMovie(Movie movie) {
        this.movies.add(movie);
    }

    private boolean checkIntegrity(User user) {

        for (Movie likedMovie : user.getLikedMovies()) {
            if (!movies.contains(likedMovie)) {
                return false; // Integrity check failed
            }
        }

        return true; // Integrity check passed
    }
}
