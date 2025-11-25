package com.example;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
public class MovieTest{


    @Test
    @DisplayName("Valid movie creation with proper title and id")
    public void testValidMovieCreation(){
        List<String> genre = Arrays.asList("Action", "Adventure");
        Movie movie = assertDoesNotThrow(() -> new Movie("The Dark Knight", "TDK123", genre));
         
        assertEquals("The Dark Knight", movie.getTitle());
        assertEquals("TDK123", movie.getId());
        assertEquals(genre, movie.getGenres());
        }
        
    @Test
    @DisplayName("Movie title - each word starts with capital letter")
    public void testMovieTitleWithCapitalLetters(){
        List<String> genres = Arrays.asList("Drama");
        assertDoesNotThrow(() -> new Movie("The Matrix", "TM456", genres));
        assertDoesNotThrow(() -> new Movie("Star Wars", "SW789", genres));
        assertDoesNotThrow(() -> new Movie("A Beautiful Mind", "ABM321", genres));
    }

    @Test
    @DisplayName("Movie title - invalid format (lowercase start)")
    public void testMovieTitleInvalidLowercase(){
        List<String> genres = Arrays.asList("Drama");
        Exception exception = assertThrows(MovieException.class, () -> {
            new Movie("the matrix", "TM123", genres);
        });
        assertThrows(MovieException.class, () -> {
            new Movie("the Matrix", "TM456", genres);});

        assertEquals("ERROR: Movie Title the matrix is wrong", exception.getMessage());
    }
    
    @Test
    @DisplayName("Movie title - invalid format (word starts with lowercase)")
    public void testMovieTitleInvalidWordStartsWithLowercase() {
        List<String> genres = Arrays.asList("Drama");
        Exception exception = assertThrows(MovieException.class, () -> {
            new Movie("The matrix", "TM123", genres);
            });
        assertThrows(MovieException.class, () -> {
            new Movie("Star wars", "SW789", genres);});
        assertThrows(MovieException.class, () -> {
            new Movie("A beautiful Mind", "ABM321", genres);});
            
        assertEquals("ERROR: Movie Title The matrix is wrong", exception.getMessage());
    }
    
    @Test
    @DisplayName("Movie ID - contains all capital letters from title")
    public void testMovieIdContainsCapitalLetters() {
        List<String> genres = Arrays.asList("Action");
        assertDoesNotThrow(() -> new Movie("The Dark Knight", "TDK123", genres));
        assertDoesNotThrow(() -> new Movie("Inception", "I456", genres));
        assertDoesNotThrow(() -> new Movie("Avatar", "A789", genres));
    }

    @Test
    @DisplayName("Movie title - capital letters only at the start of words")
    public void testMovieTitleCapitalLettersOnlyAtStart() {
        List<String> genres = Arrays.asList("Drama");
        Exception exception = assertThrows(MovieException.class, () -> {
            new Movie("The DARK Knight", "TDK123", genres);
        });
        
        assertEquals("ERROR: Movie Title The DARK Knight is wrong", exception.getMessage());
    }
    
    @Test
    @DisplayName("Movie ID - missing capital letters from title")
    public void testMovieIdMissingCapitalLetters(){
        List<String> genres = Arrays.asList("Action");
        Exception exception = assertThrows(MovieException.class, () -> {
        new Movie("The Dark Knight", "TDK", genres);
        });
        assertTrue(exception.getMessage().contains("ERROR: Movie Id letters TDK are wrong"));
    }
    
    @Test
    @DisplayName("Movie ID -  wrong capital letters")
    public void testMovieIdWrongCapitalLetters() {
        List<String> genres = Arrays.asList("Action");
        Exception exception = assertThrows(MovieException.class, () -> {
            new Movie("The Dark Knight", "TK123", genres);
        });
        assertEquals("ERROR: Movie Id letters TK123 are wrong", exception.getMessage());
    }
    
    @Test
    @DisplayName("Movie ID - must have exactly 3 numbers")
    public void testMovieIdThreeNumbers() {
        List<String> genres = Arrays.asList("Horror");
        assertDoesNotThrow(() -> new Movie("Scream", "S123", genres));
        Exception exception1 = assertThrows(MovieException.class, () -> {
            new Movie("Scream", "S12", genres);
        });
        assertEquals("ERROR: Movie Id letters S12 are wrong", exception1.getMessage());
        Exception exception2 = assertThrows(MovieException.class, () -> {
            new Movie("Scream", "S1234", genres);
        });
        assertEquals("ERROR: Movie Id letters S1234 are wrong", exception2.getMessage());
    }

    @Test
    @DisplayName("Movie genres - multiple genres allowed")
    public void testMovieGenres() {
    List<String> genres = Arrays.asList("Action", "Thriller", "Drama");
        Movie movie =assertDoesNotThrow(() -> new Movie("The Bourne Identity", "TBI123", genres));
        assertEquals(3, movie.getGenres().size());
        assertTrue(movie.getGenres().contains("Action"));
        assertTrue(movie.getGenres().contains("Thriller"));
        assertTrue(movie.getGenres().contains("Drama"));
    }

    @Test
    @DisplayName("Movie genres - empty genre list")
    public void testMovieEmptyGenres() {
        List<String> genres = Arrays.asList();
        Exception exception = assertThrows(MovieException.class, () -> {
            new Movie("Interstellar", "I123", genres);
        });
        assertTrue(exception.getMessage().contains("ERROR: Movie I123 has empty genres list"));
    }
}
