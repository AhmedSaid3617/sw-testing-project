package com.example;

import java.util.ArrayList;

public class Parser {
    private String usersFile;
    private String moviesFile;
    private DataStore dataStore;
    public Parser(String usersFile, String moviesFile) {
        // Constructor implementation
        this.usersFile = usersFile;
        this.moviesFile = moviesFile;
        this.dataStore = new DataStore();

        
    }
    private void record_books(String l,User user)throws Exception{
         String[] parts=l.split(",");
         for(String id : parts){
            Movie movie = dataStore.getMovieById(id);
            user.addLikedMovie(movie);
            
         }
    }

    public void parseUsers()throws Exception {
        // Method to parse users
        String[] lines = usersFile.split("\n");
        int length=lines.length;
        if(lines.length %2==1){
            throw new Exception("Invalid user data format: Odd number of lines");
        }
        for(int i=0;i<length;i+=2){
            String[] parts=lines[i].split(",");
            if(parts.length !=2){
                throw new Exception("Invalid user data format at line " + (i + 1));
            }
            User user = new User(parts[0], parts[1], new ArrayList<>());
            dataStore.addUser(user);
            record_books(lines[i+1],user);
        }
    }

    public void parseMovies() {
        // Method to parse movies
    }
}
