package com.example;

import java.util.List;

public class User {

    private String name;
    private String id;
    private List<Movie> likedMovies; // List of movie IDs

    public User(String name, String id, List<Movie> likedMovies) {
        this.name = name;
        this.id = id;
        this.likedMovies = likedMovies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(List<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }
    public void addLikedMovie(Movie movie) {
        this.likedMovies.add(movie);
    }
}
