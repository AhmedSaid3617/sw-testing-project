package com.example;

import java.util.List;

public class User {

    private String name;
    private String id;
    private List<Movie> likedMovies; // List of movie IDs

    public User(String name, String id, List<Movie> likedMovies) {
        if (!isNameValid(name)) {
            throw new IllegalArgumentException("Invalid user name format: " + name);
        }
        if (!isIdValid(id)) {
            throw new IllegalArgumentException("Invalid user ID format: " + id);
        }
        this.name = name;
        this.id = id;
        this.likedMovies = likedMovies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!isNameValid(name)) {
            throw new IllegalArgumentException("Invalid user name format: " + name);
        }
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (!isIdValid(id)) {
            throw new IllegalArgumentException("Invalid user ID format: " + id);
        }
        this.id = id;
    }

    public List<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(List<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    private boolean isNameValid(String name) {
        if (name == null || name.trim().isEmpty() || name.startsWith(" ")) {
            return false;
        }
        return name.matches("[a-zA-Z ]+");
    }

    private boolean isIdValid(String id) {
        if (id == null) {
            return false;
        }
        // Alphanumeric, 9 characters, starts with numbers, ends with at most one
        // letter.
        return id.matches("\\d{8,9}[a-zA-Z]?") && id.length() == 9;
    }
}
