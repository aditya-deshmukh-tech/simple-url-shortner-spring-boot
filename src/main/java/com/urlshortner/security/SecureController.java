package com.urlshortner.security;

import com.urlshortner.exceptions.UrlShortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
/*
contoller class for user registration
 */

@RestController
@RequestMapping("/secure")
public class SecureController {

    private UserRepository userRepo;

    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerClient(@RequestBody UserClient userClient) {
        if (userRepo.existsById(userClient.getUsername())) {
            throw new UrlShortException("user already exist..", "User alraedy exist..", 409);
        }
        userClient.setPassword(passwordEncoder.encode(userClient.getPassword()));
        userRepo.save(userClient);
        return ResponseEntity.ok("user created..");
    }

    @PutMapping("/update")
    public ResponseEntity<UserClient> updateClient(@RequestBody UserClient userClient) {
        if (!userRepo.existsById(userClient.getUsername())) {
            throw new UrlShortException("user not exist..", "User does not exist or deleted..", 400);
        }
        userClient.setPassword(passwordEncoder.encode(userClient.getPassword()));
        userRepo.update(userClient);
        return ResponseEntity.ok(userClient);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteClient(@PathVariable String username) {
        if (!userRepo.existsById(username)) {
            throw new UrlShortException("user not exist..", "User does not exist or deleted..", 400);
        }
        userRepo.deleteUser(username);
        return ResponseEntity.ok("user deleted..");
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
}
