package com.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Movie Recommender System!");
        new Main().run(
                "users.txt",
                "movies.txt",
                "recommendations.txt");
    }

    public void run(String usersFilePath, String moviesFilePath, String outputFilePath) {
        try {
            // Clear the output file if it exists
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(outputFilePath));

            // Parse input files
            System.out.println("Parsing movies and users data...");
            Parser parser = createParser(usersFilePath, moviesFilePath);
            ParseResult parseResult = parser.parse();

            // Create DataStore with parsed data
            System.out.println("Creating data store...");
            DataStore dataStore = createDataStore(parseResult);

            // Create Recommender and Writer
            Recommender recommender = createRecommender(dataStore);
            RecommendationWriter writer = createWriter(outputFilePath);

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

    public Parser createParser(String users, String movies) throws java.io.IOException {
        return new Parser(users, movies);
    }

    public DataStore createDataStore(ParseResult result) throws Exception {
        return new DataStore(result);
    }

    public Recommender createRecommender(DataStore dataStore) {
        return new Recommender(dataStore);
    }

    public RecommendationWriter createWriter(String path) {
        return new RecommendationWriter(path);
    }

}
