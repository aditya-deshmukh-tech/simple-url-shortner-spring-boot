package com.urlshortner.security;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserRepository {

    public static HashMap<String, UserClient> repo = new HashMap<>();

    public void save(UserClient userClient) {
        repo.put(userClient.getUsername(), userClient);
    }

    public UserClient update(UserClient userClient) {
        repo.put(userClient.getUsername(), userClient);
        return repo.get(userClient.getUsername());
    }

    public UserClient getUser(String username) {
        return repo.get(username);
    }

    public boolean existsById(String username) {
        return repo.containsKey(username);
    }

    public void deleteUser(String username) {
        repo.remove(username);
    }
}
