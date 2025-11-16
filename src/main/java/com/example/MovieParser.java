package com.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Movie {
    private String title;
    private String id;
    private List<String> genres;

    public Movie(String title, String id, List<String> genres) {
        this.title = title;
        this.id = id;
        this.genres = genres;
    }

    public static List<Movie> readMoviesFromFile(String filename) throws IOException {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Read the first line: title and id
                String[] titleAndId = line.split(",", 2);
                if (titleAndId.length < 2) {
                    continue;
                }
                String title = titleAndId[0].trim();
                String id = titleAndId[1].trim();
                if (!title.matches("([A-Z][a-z]*\\s*)+")) {
                throw new IllegalArgumentException("Error in movie title\nERROR: Movie Title " + title + " is wrong");
                }
                
                // Extract capital letters from title
                String expectedLetters = title.replaceAll("[^A-Z]", "");
                
                // Extract letters and numbers from id
                String idLetters = id.replaceAll("[^A-Z]", "");
                String idNumbers = id.replaceAll("[^0-9]", "");
                
                if (!idLetters.equals(expectedLetters)) {
                    throw new IllegalArgumentException("Error in movie id letters\nERROR: Movie Id letters " + id + " are wrong");
                }
                
                if (!idNumbers.matches("[0-9]{3}")) { //"(?!.*(.).*\\1)[0-9]{3}"
                    throw new IllegalArgumentException("Error in movie id unique numbers\nERROR: Movie Id numbers " + id + " aren't unique");
                }
                
                line = br.readLine();
                if (line == null) {break;}
                
                String[] genresArray = line.split(",");
                List<String> genres = new ArrayList<>();
                for (String genre : genresArray) {
                    genres.add(genre.trim());
                }
                movies.add(new Movie(title, id, genres));
            }
        }
        
        return movies;
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
    public static void main(String[] args) {
        try {
            List<Movie> movies = readMoviesFromFile("movies.txt");
            for (Movie movie : movies) {
                System.out.println("Title: " + movie.getTitle());
                System.out.println("ID: " + movie.getId());
                System.out.println("Genres: " + movie.getGenres());
                System.out.println("---");
            }
        } catch (IOException e) {
            System.err.println("Error opening The file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
