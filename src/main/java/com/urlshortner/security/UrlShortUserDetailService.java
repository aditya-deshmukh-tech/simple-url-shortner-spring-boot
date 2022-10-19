package com.urlshortner.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UrlShortUserDetailService implements UserDetailsService {

    private UserRepository userRepo;

    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserClient userClient = userRepo.getUser(username);
        if (userClient == null){
            throw new UsernameNotFoundException("no user found with username "+username);
        }
        return new User(userClient.getUsername(), userClient.getPassword(), getAuthorities(userClient.getRoles()));
    }


    public Set<GrantedAuthority> getAuthorities(List<String> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Autowired
    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void addRootUser() {
        UserRepository.repo.put("root", new UserClient("root", passwordEncoder.encode("root"), Arrays.asList("NORMAL","ADMIN")));
        System.out.println("user added");
    }
}
