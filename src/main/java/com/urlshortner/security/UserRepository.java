package com.urlshortner.security;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserRepository {

    public static HashMap<String, UserClient> repo = new HashMap<>();

    public void save(UserClient userClient) {
        repo.put(userClient.getUsername(), userClient);
    }

    public UserClient getUser(String username) {
        return repo.get(username);
    }

    public boolean existsById(String username) {
        return repo.containsKey(username);
    }
}
