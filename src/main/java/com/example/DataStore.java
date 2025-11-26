package com.example;

import java.util.List;

/**
 * Represents an in-memory data store for managing users and movies.
 * This class provides functionalities to add users and movies while ensuring
 * data integrity.
 */
public class DataStore {

    private List<User> users;
    private List<Movie> movies;

    public DataStore() {
        users = new java.util.ArrayList<>();
        movies = new java.util.ArrayList<>();
    }

    /**
     * Constructs a new DataStore object.
     * This initializes the user and movie lists as empty ArrayLists.
     * 
     * @throws Exception
     */
    public DataStore(ParseResult data) throws Exception {
        movies = new java.util.ArrayList<>();
        users = new java.util.ArrayList<>(); // Initialize empty, will add users after validation
        
        // Validate and add each movie
        for (Movie movie : data.getMovies()) {
            addMovie(movie);
        }
        // Validate and add each user
        for (User user : data.getUsers()) {
            checkIntegrity(user);
            users.add(user);
        }
    }

    /**
     * Adds a new user to the data store after validating its integrity.
     * The user is only added if it passes the {@code checkIntegrity} validation.
     *
     * @param user The user object to be added to the data store.
     * @throws IllegalArgumentException if the user's data fails the integrity
     *                                  check,
     *                                  preventing the addition of a user with
     *                                  invalid or inconsistent data.
     */
    public void addUser(User user) throws Exception {
        if (checkIntegrity(user)) {
            this.users.add(user);
        } else {
            throw new DataIntegrityException("User data integrity check failed for user ID: " + user.getId());
        }
    }

    /**
     * Adds a movie to the data store.
     *
     * @param movie the movie object to be added.
     */
    public void addMovie(Movie movie) throws DataIntegrityException {
        String id = movie.getId();
        String numericPart = id.replaceAll("\\D+", "");
        for (Movie m : movies) {
            String otherNumeric = m.getId().replaceAll("\\D+", "");
            if (numericPart.equals(otherNumeric)) {
                throw new DataIntegrityException("Movie Id numbers " + movie.getId() + " aren't unique");
            }
        }
        this.movies.add(movie);
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    private boolean checkIntegrity(User user) throws Exception {

        for (String id : user.getLikedMovies()) {
            boolean found = false;
            for (Movie movie : movies) {
                if (movie.getId().equals(id)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new DataIntegrityException("This movie id doesn't exist");
            }
        }
        for (User existingUser : users) {
            if (existingUser.getId().equals(user.getId())) {
                throw new DataIntegrityException("Duplicate user ID found: " + user.getId());
            }
        }

        return true; // Integrity check passed
    }

    public Movie getMovieById(String id) {
        for (Movie movie : movies) {
            if (movie.getId().equals(id)) {
                return movie; // Return the movie if found
            }
        }
        return null; // Return null if no movie with the given ID is found
    }
}
