package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataStoreTest {
    
    private DataStore dataStore;
    private List<Movie> movies;
    private List<User> users;
    private ParseResult parseResult;

    @BeforeEach
    void setUp() throws UserException, MovieException {
        movies = new ArrayList<>();
        users = new ArrayList<>();
        
        // Create sample movies
        Movie movie1 = new Movie("The Matrix", "TM123", Arrays.asList("Action", "Sci-Fi"));
        Movie movie2 = new Movie("Inception", "I456", Arrays.asList("Action", "Thriller"));
        Movie movie3 = new Movie("Avatar", "A789", Arrays.asList("Adventure", "Sci-Fi"));
        
        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);
        
        parseResult = new ParseResult(movies, users);
        dataStore = assertDoesNotThrow(()->new DataStore(parseResult));
    }

    @Test
    @DisplayName("DataStore initialization with movies")
    void testDataStoreInitialization() {
        assertEquals(3, dataStore.getMovies().size());
        assertEquals(0, dataStore.getUsers().size());
    }

    @Test
    @DisplayName("Add user with valid liked movies")
    void testAddValidUser() throws Exception {
        User user = new User("John Doe", "123456789", 
                           new ArrayList<>(Arrays.asList("TM123", "I456")));
        
        assertDoesNotThrow(() -> dataStore.addUser(user));
        assertEquals(1, dataStore.getUsers().size());
        assertEquals("John Doe", dataStore.getUsers().get(0).getName());
    }

    @Test
    @DisplayName("Add multiple users")
    void testAddMultipleUsers() throws Exception {
        User user1 = new User("John Doe", "123456789", 
                            new ArrayList<>(Arrays.asList("TM123")));
        User user2 = new User("Jane Smith", "12345678A", 
                            new ArrayList<>(Arrays.asList("I456")));
        User user3 = new User("Bob Johnson", "987654321", 
                            new ArrayList<>(Arrays.asList("A789")));
        
        dataStore.addUser(user1);
        dataStore.addUser(user2);
        dataStore.addUser(user3);
        
        assertEquals(3, dataStore.getUsers().size());
    }

    @Test
    @DisplayName("Add user with non-existent movie throws exception")
    void testAddUserWithInvalidMovieThrowsException() throws UserException {
        User user = new User("John Doe", "123456789", 
                           new ArrayList<>(Arrays.asList("INVALID999")));
        
        assertThrows(DataIntegrityException.class, () -> {
            dataStore.addUser(user);
        });
    }

    @Test
    @DisplayName("Add user with empty liked movies list")
    void testAddUserWithEmptyLikedMovies() throws Exception {
        User user = new User("Alice Brown", "555555555", new ArrayList<>());
        
        assertDoesNotThrow(() -> dataStore.addUser(user));
        assertEquals(1, dataStore.getUsers().size());
        assertEquals(0, user.getLikedMovies().size());
    }

    @Test
    @DisplayName("Add user with all available movies")
    void testAddUserWithAllMovies() throws Exception {
        User user = new User("Charlie Davis", "111111111", 
                           new ArrayList<>(Arrays.asList("TM123", "I456", "A789")));
        
        assertDoesNotThrow(() -> dataStore.addUser(user));
        assertEquals(3, user.getLikedMovies().size());
    }

    @Test
    @DisplayName("Add movie to data store")
    void testAddMovie() throws UserException, MovieException {
        int initialSize = dataStore.getMovies().size();
        Movie newMovie = new Movie("Interstellar", "I123", Arrays.asList("Sci-Fi", "Drama"));
        
        dataStore.addMovie(newMovie);
        
        assertEquals(initialSize + 1, dataStore.getMovies().size());
    }

    @Test
    @DisplayName("Add multiple movies to data store")
    void testAddMultipleMovies() throws UserException, MovieException {
        Movie movie4 = new Movie("Titanic", "T999", Arrays.asList("Drama", "Romance"));
        Movie movie5 = new Movie("The Dark Knight", "TDK888", Arrays.asList("Action", "Crime"));
        
        dataStore.addMovie(movie4);
        dataStore.addMovie(movie5);
        
        assertEquals(5, dataStore.getMovies().size());
    }

    @Test
    @DisplayName("Get movie by ID - existing movie")
    void testGetMovieByIdFound() {
        Movie movie = dataStore.getMovieById("TM123");
        
        assertNotNull(movie);
        assertEquals("The Matrix", movie.getTitle());
        assertEquals("TM123", movie.getId());
    }

    @Test
    @DisplayName("Get movie by ID - non-existent movie")
    void testGetMovieByIdNotFound() {
        Movie movie = dataStore.getMovieById("NONEXISTENT");
        
        assertNull(movie);
    }

    @Test
    @DisplayName("Get all movies")
    void testGetAllMovies() {
        List<Movie> allMovies = dataStore.getMovies();
        
        assertEquals(3, allMovies.size());
        assertTrue(allMovies.stream().anyMatch(m -> m.getId().equals("TM123")));
        assertTrue(allMovies.stream().anyMatch(m -> m.getId().equals("I456")));
        assertTrue(allMovies.stream().anyMatch(m -> m.getId().equals("A789")));
    }

    @Test
    @DisplayName("Get all users")
    void testGetAllUsers() throws Exception {
        User user1 = new User("John Doe", "123456789", 
                            new ArrayList<>(Arrays.asList("TM123")));
        User user2 = new User("Jane Smith", "12345678A", 
                            new ArrayList<>(Arrays.asList("I456")));
        
        dataStore.addUser(user1);
        dataStore.addUser(user2);
        
        List<User> allUsers = dataStore.getUsers();
        assertEquals(2, allUsers.size());
    }

    @Test
    @DisplayName("User data integrity check - valid movie IDs")
    void testIntegrityCheckWithValidMovieIds() throws Exception {
        User user = new User("Test User", "999999999", 
                           new ArrayList<>(Arrays.asList("TM123", "I456")));
        
        assertDoesNotThrow(() -> dataStore.addUser(user));
        assertTrue(dataStore.getUsers().contains(user));
    }

    @Test
    @DisplayName("User data integrity check - mixed valid and invalid IDs")
    void testIntegrityCheckWithMixedMovieIds() throws UserException {
        User user = new User("Test User", "888888888", 
                           new ArrayList<>(Arrays.asList("TM123", "INVALID001", "I456")));
        
        assertThrows(DataIntegrityException.class, () -> {
            dataStore.addUser(user);
        });
    }

    @Test
    @DisplayName("Verify movie data is preserved after adding users")
    void testMovieDataPreservedAfterAddingUsers() throws Exception {
        User user = new User("John Doe", "123456789", 
                           new ArrayList<>(Arrays.asList("TM123")));
        dataStore.addUser(user);
        
        assertEquals(3, dataStore.getMovies().size());
        Movie matrix = dataStore.getMovieById("TM123");
        assertEquals("The Matrix", matrix.getTitle());
    }

    @Test
    @DisplayName("Get movie details by ID")
    void testGetMovieDetails() {
        Movie movie = dataStore.getMovieById("I456");
        
        assertNotNull(movie);
        assertEquals("Inception", movie.getTitle());
        assertEquals("I456", movie.getId());
        assertTrue(movie.getGenres().contains("Action"));
        assertTrue(movie.getGenres().contains("Thriller"));
    }
    @Test
    @DisplayName("Test repeated user id")
    void testRepeatedUserIdIntegrity() throws Exception{
       User user1 = new User("John Doe", "123456789", 
                            new ArrayList<>(Arrays.asList("TM123")));
        User user2 = new User("Jane Smith", "123456789", 
                            new ArrayList<>(Arrays.asList("I456")));
        dataStore.addUser(user2);
        assertThrows(DataIntegrityException.class, ()->dataStore.addUser(user1));
    }
    @Test
    @DisplayName("Test user have liked movie doesnot exist")
    void testexistingmoviesIdIntegrity() throws Exception{
       User user1 = new User("John Doe", "123456789", 
                            new ArrayList<>(Arrays.asList("T123")));
        User user2 = new User("Jane Smith", "123456789", 
                            new ArrayList<>(Arrays.asList("I456")));
        assertDoesNotThrow(()->dataStore.addUser(user2));
        assertThrows(DataIntegrityException.class, ()->dataStore.addUser(user1));
    }
}

