package com.example;

import java.util.List;

public class Movie {

    private String title;
    private String id;
    private List<String> genres;
    
    public Movie(String title, String id, List<String> genres) {
        if (!isTitleValid(title)) {
            throw new IllegalArgumentException("Invalid movie title format: " + title);
        }
        if (!isIdValid(id, title)) {
            throw new IllegalArgumentException("Invalid movie ID format: " + id);
        }
        this.title = title;
        this.id = id;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (!isTitleValid(title)) {
            throw new IllegalArgumentException("Invalid movie title format: " + title);
        }
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (!isIdValid(id, this.title)) {
            throw new IllegalArgumentException("Invalid movie ID format: " + id);
        }
        this.id = id;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public static boolean isTitleValid(String title) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        String[] words = title.split("\\s+");
        for (String word : words) {
            if (word.isEmpty() || !Character.isUpperCase(word.charAt(0))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isIdValid(String id, String title) {
        if (id == null || title == null) {
            return false;
        }

        String capitalLettersInTitle = title.replaceAll("[^A-Z]", "");
        
        if (!id.startsWith(capitalLettersInTitle)) {
            return false;
        }

        String numbersPart = id.substring(capitalLettersInTitle.length());
        if (numbersPart.length() != 3) {
            return false;
        }

        if (!numbersPart.matches("\\d{3}")) {
            return false;
        }

        return numbersPart.chars().distinct().count() == 3;
    }
}
