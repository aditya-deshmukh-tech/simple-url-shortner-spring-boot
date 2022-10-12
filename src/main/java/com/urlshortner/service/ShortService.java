package com.urlshortner.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class ShortService {
	
	private Map<String,String> m = new HashMap<String,String>();
	
	public String getRandomString(String s) {
		   Random r = new Random();
		   String f = "";
			for(int i=0;i<8;i++) {
				f=f+s.charAt(r.nextInt(s.length()))+r.nextInt(9);
			}
			f = f.replaceAll("[^a-zA-Z0-9]", "");
			return f;
	   }
	
	public String addToDatabase(String s) {
		String h = getRandomString(s);
		m.put(h, s);
		return h;
	}
	
	public String getLink(String s) {
		 String f = m.get(s);
		 m.remove(s);
		 return f;
	}

	
}
