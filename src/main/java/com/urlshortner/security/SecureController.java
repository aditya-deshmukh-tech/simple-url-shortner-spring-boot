package com.urlshortner.security;

import com.urlshortner.exceptions.UrlShortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
}
