package com.example;

import java.util.ArrayList;

public class Parser {
    private String usersFileData;
    private String moviesFileData;
    private DataStore dataStore;
    Movie Movies[];

    public Parser(String usersFilePath, String moviesFilePath) throws java.io.IOException {
        this.usersFileData = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(usersFilePath)));
        this.moviesFileData = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(moviesFilePath)));
        this.dataStore = new DataStore();
    }

    public void parse() throws Exception {
        UserParser userParser = new UserParser(dataStore);
        userParser.parseUsers(usersFileData);
        // parse movies
    }

}
