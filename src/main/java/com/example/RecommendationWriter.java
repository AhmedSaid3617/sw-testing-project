package com.example;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RecommendationWriter {

    private String outputFilePath;

    public RecommendationWriter(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public void writeRecommendations(User user, List<Movie> recommendations) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(outputFilePath),
                StandardCharsets.UTF_8)) {

            writer.write(user.getName() + "," + String.valueOf(user.getId()));
            writer.newLine();
            for(int i = 0; i < recommendations.size(); i++) {
                Movie movie = recommendations.get(i);
                writer.write(movie.getTitle()); 
                if (i == recommendations.size() - 1) {
                    writer.newLine();
                }
                else{
                    writer.write(",");
                }
            }

        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to write recommendations to " + outputFilePath, e);
        }
    }
}
