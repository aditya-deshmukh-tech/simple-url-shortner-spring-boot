package com.urlshortner.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urlshortner.models.LinkModel;
import com.urlshortner.service.ShortService;

@RestController
@RequestMapping("/url.short")
public class UrlController {
	
	@Autowired
	private ShortService shorter;

	@PostMapping("/create")
	public ResponseEntity createUrl(@RequestBody LinkModel m) {
		String j = shorter.addToDatabase(m.getFulllink());
		return ResponseEntity.ok("http://localhost:8080/url.short/"+j);
	}
	
	@GetMapping("/{url}")
	public ResponseEntity getFullLink(@PathVariable String url,HttpServletResponse res) throws IOException {
		String uri = shorter.getLink(url);
		res.sendRedirect(uri);
		return ResponseEntity.ok("moved..");
	}
}
