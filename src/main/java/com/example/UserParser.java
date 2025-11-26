package com.example;

import java.util.ArrayList;
import java.util.List;

public class UserParser {

    private List<User> users_list;

    public UserParser() {
        users_list=new ArrayList<>();
    }

    private void addLikedMovies(String l, User user) throws Exception {
        String[] parts = l.split(",");
        
        if (parts[0].matches(".*\\d.*") == false) {
            throw new UserException("Liked movies are invalid for user " + user.getId());
            
        }

        for (String id : parts) {
            user.addLikedMovie(id);
        }
    }

    public List<User> parseUsers(String usersFileData) throws Exception {
        // Method to parse users
        String[] lines = usersFileData.split("\n");
        int length = lines.length;
        for (int i = 0; i < length; i += 2) {
            String[] parts = lines[i].split(",");
            if (parts.length != 2) {
                throw new UserException("Invalid user data format at line " + (i + 1));
            }
            User user = new User(parts[0], parts[1], new ArrayList<>());
            users_list.add(user);
            if (i+1 < length) {
                addLikedMovies(lines[i + 1], user);   
            }
            else {
                throw new UserException("Liked movies are invalid for user " + user.getId());
            }
        }
        return users_list;
    }
}
