package com.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Movie Recommender System!");
        
        String usersFilePath = "users.txt";
        String moviesFilePath = "movies.txt";
        String outputFilePath = "recommendations.txt";
        
        try {
            // Clear the output file if it exists
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(outputFilePath));
            
            // Parse input files
            System.out.println("Parsing movies and users data...");
            Parser parser = new Parser(usersFilePath, moviesFilePath);
            ParseResult parseResult = parser.parse();
            
            // Create DataStore with parsed data
            System.out.println("Creating data store...");
            DataStore dataStore = new DataStore(parseResult);
            
            // Create Recommender and Writer
            Recommender recommender = new Recommender(dataStore);
            RecommendationWriter writer = new RecommendationWriter(outputFilePath);
            
            // Generate and write recommendations for each user
            System.out.println("Generating recommendations...");
            List<User> users = dataStore.getUsers();
            
            for (User user : users) {
                List<Movie> recommendations = recommender.recommendMovies(user);
                writer.writeRecommendations(user, recommendations);
                System.out.println("Generated recommendations for user: " + user.getName());
            }
            
            System.out.println("Recommendations successfully written to " + outputFilePath);
            
        } catch (java.io.IOException e) {
            System.err.println("Error reading input files: " + e.getMessage());
            System.exit(1);
        } catch (MovieException | UserException e) {
            System.err.println("Error parsing data: " + e.getMessage());
            System.exit(1);
        } catch (DataIntegrityException e) {
            System.err.println("Data integrity error: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}