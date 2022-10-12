package com.urlshortner;

import com.urlshortner.security.UserClient;
import com.urlshortner.security.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class UrlshortnerorcrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlshortnerorcrmApplication.class, args);
		UserRepository.repo.put("root", new UserClient("root", "root", Arrays.asList("NORMAL","ADMIN")));
	}

}
