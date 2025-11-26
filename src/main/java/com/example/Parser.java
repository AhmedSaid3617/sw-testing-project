package com.example;

import java.util.List;

public class Parser {
    private String usersFileData;
    private String moviesFileData;
    Movie Movies[];

    public Parser(String usersFilePath, String moviesFilePath) throws java.io.IOException {
        this.usersFileData = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(usersFilePath)));
        this.moviesFileData = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(moviesFilePath)));
    }

    public ParseResult parse() throws Exception {
        List<Movie> movies = new MovieParser().parseMovies(moviesFileData);
        List<User> users = new UserParser().parseUsers(usersFileData);       
        ParseResult pr = new ParseResult(movies, users);
        return pr;
    }

}
