package com.urlshortner.security.jwt;

import com.urlshortner.exceptions.UrlShortException;
import com.urlshortner.security.UrlShortUserDetailService;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/*
controller class for jwt authentication and generation operations
 */
@RestController
public class JwtAuthenticationController {

    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    private UrlShortUserDetailService urlShortUserDetailService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = urlShortUserDetailService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping(value = "/refreshtoken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception {
        // From the HttpRequest get the claims
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
        boolean expiredTokenValidity = (boolean) request.getAttribute("expiredTokenValidity");
        if (!expiredTokenValidity)
            throw new UrlShortException("expired token error", "token expiration time is greater than valid time please create new token using /authenticate", 403);
        Map<String, Object> expectedMap = getMapFromIoJsonWebTokenClaims(claims);
        String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public Map<String, Object> getMapFromIoJsonWebTokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new UrlShortException("USER_DISABLED", e.getMessage(), 401);
        } catch (BadCredentialsException e) {
            throw new UrlShortException("INVALID_CREDENTIALS", e.getMessage(), 401);
        }
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setUrlShortUserDetailService(UrlShortUserDetailService urlShortUserDetailService) {
        this.urlShortUserDetailService = urlShortUserDetailService;
    }
}
