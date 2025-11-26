package com.example;

import java.util.List;

public class ParseResult {
    private final List<Movie> movies;
    private final List<User> users;

    public ParseResult(List<Movie> movies, List<User> users) {
        this.movies = movies;
        this.users = users;
    }

    public List<Movie> getMovies() { return movies; }
    public List<User> getUsers() { return users; }
}
