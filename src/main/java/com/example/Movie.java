package com.example;

import java.util.List;

public class Movie {
    private String title;
    private String id;
    private List<String> genres;

    public Movie(String title, String id, List<String> genres) throws MovieException {
        // Validate title format
        if (!title.matches("([A-Z][a-z]*\\s*)+")) {
            throw new MovieException("Error in movie title\nERROR: Movie Title " + title + " is wrong");
        }
        
        // Extract capital letters from title
        String expectedLetters = title.replaceAll("[^A-Z]", "");
        
        // Extract letters and numbers from id
        String idLetters = id.replaceAll("[^A-Z]", "");
        String idNumbers = id.replaceAll("[^0-9]", "");
        
        // Validate id letters match title capital letters
        if (!idLetters.equals(expectedLetters)) {
            throw new MovieException("Error in movie id letters\nERROR: Movie Id letters " + id + " are wrong");
        }
        
        // Validate id numbers format
        if (!idNumbers.matches("[0-9]{3}")) { //"(?!.*(.).*\\1)[0-9]{3}"
            throw new MovieException("Error in movie id unique numbers\nERROR: Movie Id numbers " + id + " aren't unique");
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