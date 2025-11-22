package com.example;

import java.util.List;

public class User {

    private String name;
    private String id;
    private List<String> likedMoviesIDs; // List of movie IDs

    public User(String name, String id, List<String> likedMoviesIDs) {
        // Validate user name: must be alphabetic characters and spaces, cannot start with space
        if (!name.matches("[A-Za-z][A-Za-z\\s]*")) {
            throw new IllegalArgumentException("Error in user name\nERROR: User Name " + name + " is wrong");
        }
        
        // Validate user ID: must be alphanumeric, exactly 9 characters, 
        // starts with numbers, might end with only one alphabetic character
        if (!id.matches("[0-9]+[A-Za-z]?")) {
            throw new IllegalArgumentException("Error in user id format\nERROR: User Id " + id + " is wrong");
        }
        
        if (id.length() != 9) {
            throw new IllegalArgumentException("Error in user id length\nERROR: User Id " + id + " must be exactly 9 characters");
        }
        
        this.name = name;
        this.id = id;
        this.likedMoviesIDs = likedMoviesIDs;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<String> getLikedMovies() {
        return this.likedMoviesIDs;
    }

    public void addLikedMovie(String movie_id) {
        this.likedMoviesIDs.add(movie_id);
    }
}
