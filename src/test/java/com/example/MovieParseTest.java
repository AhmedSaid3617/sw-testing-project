package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.List;

public class MovieParseTest {
    private MovieParser movieParser;

    @BeforeEach
    void setUp() {
        movieParser = new MovieParser();
    }

    @Test
    @DisplayName("Parse single movie with valid format")
    void testParseSingleValidMovie() throws Exception {
        String movieData = "The Matrix,TM123\nAction,Sci-Fi\n";
        
        List<Movie> movies = movieParser.parseMovies(movieData);
        
        assertEquals(1, movies.size());
        assertEquals("The Matrix", movies.get(0).getTitle());
        assertEquals("TM123", movies.get(0).getId());
        assertEquals(2, movies.get(0).getGenres().size());
        assertTrue(movies.get(0).getGenres().contains("Action"));
        assertTrue(movies.get(0).getGenres().contains("Sci-Fi"));
    }

    @Test
    @DisplayName("Parse multiple movies")
    void testParseMultipleMovies() throws Exception {
        String movieData = "The Matrix,TM123\nAction,Sci-Fi\n" +
                          "Inception,I456\nAction,Thriller,Drama\n" +
                          "Avatar,A789\nAction,Adventure\n";
        
        List<Movie> movies = movieParser.parseMovies(movieData);
        
        assertEquals(3, movies.size());
        assertEquals("The Matrix", movies.get(0).getTitle());
        assertEquals("Inception", movies.get(1).getTitle());
        assertEquals("Avatar", movies.get(2).getTitle());
    }

    @Test
    @DisplayName("Parse movie with single genre")
    void testParseMovieWithSingleGenre() throws Exception {
        String movieData = "Titanic,T123\nDrama\n";
        
        List<Movie> movies = movieParser.parseMovies(movieData);
        
        assertEquals(1, movies.size());
        assertEquals(1, movies.get(0).getGenres().size());
        assertEquals("Drama", movies.get(0).getGenres().get(0));
    }

    @Test
    @DisplayName("Parse movie with multiple genres")
    void testParseMovieWithMultipleGenres() throws Exception {
        String movieData = "The Dark Knight,TDK123\nAction,Crime,Drama,Thriller\n";
        
        List<Movie> movies = movieParser.parseMovies(movieData);
        
        assertEquals(1, movies.size());
        assertEquals(4, movies.get(0).getGenres().size());
        assertTrue(movies.get(0).getGenres().contains("Action"));
        assertTrue(movies.get(0).getGenres().contains("Crime"));
        assertTrue(movies.get(0).getGenres().contains("Drama"));
        assertTrue(movies.get(0).getGenres().contains("Thriller"));
    }

    @Test
    @DisplayName("Parse movie with spaces in title")
    void testParseMovieWithSpacesInTitle() throws Exception {
        String movieData = "The Dark Knight Rises,TDKR123\nAction,Adventure\n";
        
        List<Movie> movies = movieParser.parseMovies(movieData);
        
        assertEquals(1, movies.size());
        assertEquals("The Dark Knight Rises", movies.get(0).getTitle());
    }

    @Test
    void testParseMovieWithNoGenres() throws Exception {
        String movieData = "Gladiator,G123\n\n";
        
        assertThrows(MovieException.class, () -> {
            movieParser.parseMovies(movieData);
        });
        
    }

    @Test
    @DisplayName("Parse empty movie data")
    void testParseEmptyMovieData() throws Exception {
        String movieData = "";
        
        List<Movie> movies = movieParser.parseMovies(movieData);
        
        assertEquals(0, movies.size());
    }

    @Test
    @DisplayName("Invalid movie format throws exception")
    void testParseInvalidMovieThrowsException() {
        String movieData = "invalid movie title,TIM123\nAction\n";
        
        assertThrows(MovieException.class, () -> {
            movieParser.parseMovies(movieData);
        });
    }

    @Test
    @DisplayName("Movie with incorrect ID format throws exception")
    void testParseMovieInvalidIdThrowsException() {
        String movieData = "The Matrix,TM12\nAction,Sci-Fi\n";
        
        assertThrows(MovieException.class, () -> {
            movieParser.parseMovies(movieData);
        });
    }

    @Test
    @DisplayName("Parse movie with genres containing spaces")
    void testParseMovieGenresWithSpaces() throws Exception {
        String movieData = "Avatar,A123\nScience Fiction, Adventure\n";
        
        List<Movie> movies = movieParser.parseMovies(movieData);
        
        assertEquals(1, movies.size());
        assertEquals(2, movies.get(0).getGenres().size());
        assertTrue(movies.get(0).getGenres().contains("Science Fiction"));
        assertTrue(movies.get(0).getGenres().contains("Adventure"));
    }
    @Test
    @DisplayName("Parse more than a movie")
    void testParseMoreThanAMovie() throws Exception {
        String movieData = "Inception,I456\nAction,Thriller,Drama\n" +
                           "The Lord Of The Rings,TLOTR789\nFantasy,Adventure\n";
        
        List<Movie> movies = movieParser.parseMovies(movieData);
        
        assertEquals(2, movies.size());
        assertEquals("Inception", movies.get(0).getTitle());
        assertEquals("The Lord Of The Rings", movies.get(1).getTitle());
        assertEquals(3, movies.get(0).getGenres().size());
        assertTrue(movies.get(0).getGenres().contains("Action"));
        assertTrue(movies.get(0).getGenres().contains("Thriller"));
        assertTrue(movies.get(0).getGenres().contains("Drama"));
        assertEquals(2, movies.get(1).getGenres().size());
        assertTrue(movies.get(1).getGenres().contains("Fantasy"));
        assertTrue(movies.get(1).getGenres().contains("Adventure"));
    }
}
    
