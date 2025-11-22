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
        for (String id : parts) {
            user.addLikedMovie(id);
        }
    }

    public List<User> parseUsers(String usersFileData) throws Exception {
        // Method to parse users
        String[] lines = usersFileData.split("\n");
        int length = lines.length;
        if (lines.length % 2 == 1) {
            throw new Exception("Invalid user data format: Odd number of lines");
        }
        for (int i = 0; i < length; i += 2) {
            String[] parts = lines[i].split(",");
            if (parts.length != 2) {
                throw new Exception("Invalid user data format at line " + (i + 1));
            }
            User user = new User(parts[0], parts[1], new ArrayList<>());
            users_list.add(user);
            addLikedMovies(lines[i + 1], user);
        }
        return users_list;
    }
}
