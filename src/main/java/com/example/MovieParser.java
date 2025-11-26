package com.example;

import java.util.ArrayList;
import java.util.List;

public class MovieParser {

    private List<Movie> movies_list;

    public MovieParser() {
        movies_list=new ArrayList<>();
        
    }

    public List<Movie> parseMovies(String moviesFileData) throws Exception {
        String[] lines = moviesFileData.split("\n");
        int i = 0;
        
        while (i < lines.length) {
            String line = lines[i];
            
            // Read the first line: title and id
            String[] titleAndId = line.split(",", 2);
            if (titleAndId.length < 2) {
                i++;
                continue;
            }
            
            String title = titleAndId[0].trim();
            String id = titleAndId[1].trim();
            
            i++;
            if (i >= lines.length) {
                throw new MovieException("Genres are missing for movie " + id);
            }
            
            line = lines[i];
            String[] genresArray = line.split(",");
            if (genresArray.length > 1) {
                if (genresArray[1].matches(".*\\d.*")) {
                    throw new MovieException("Genres are invalid for movie " + id);
                    
                }
            }
            List<String> genres = new ArrayList<>();
            for (String genre : genresArray) {
                genres.add(genre.trim());
            }
            
            Movie movie = new Movie(title, id, genres);
            movies_list.add(movie);
            i++;
        }

        return movies_list;
    }
}
