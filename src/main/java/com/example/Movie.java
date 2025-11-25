package com.example;

import java.util.List;

public class Movie {
    private String title;
    private String id;
    private List<String> genres;

    public Movie(String title, String id, List<String> genres) throws MovieException {

        // Validate genres list is not empty
        if (genres == null || genres.isEmpty()) {
            throw new MovieException("ERROR: Movie "+ id + " has empty genres list");
        }

        // Validate title format: each word starts with one uppercase followed by only lowercase letters
        if (!title.matches("^([A-Z][a-z]*)(\\s[A-Z][a-z]*)*$")) {
            throw new MovieException("ERROR: Movie Title " + title + " is wrong");
        }
        
        // Extract capital letters from title
        String expectedLetters = title.replaceAll("[^A-Z]", "");
        
        // Extract letters and numbers from id
        String idLetters = id.replaceAll("[^A-Z]", "");
        String idNumbers = id.replaceAll("[^0-9]", "");
        
        // Validate id letters match title capital letters
        if (!idLetters.equals(expectedLetters) || idNumbers.length() != 3) {
            throw new MovieException("ERROR: Movie Id letters " + id + " are wrong");
        }
        
        this.title = title;
        this.id = id;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public List<String> getGenres() {
        return genres;
    }
}