package com.urlshortner.security;

import com.urlshortner.security.jwt.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
public class UrlShortAuthFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil;

    private UrlShortUserDetailService urlShortUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean authPass = true;
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        String base64Token = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.warn("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token has expired");
                String refreshToken = request.getHeader("refreshToken");
                String requestURL = request.getRequestURL().toString();
                // allow for Refresh Token creation if following conditions are true.
                boolean expiredTokenValid = jwtTokenUtil.isExpiredTokenValid(e.getClaims());
                if (refreshToken != null && refreshToken.equals("true") && requestURL.contains("refreshtoken") && expiredTokenValid) {
                    authenticateForRefreshToken(e, request, expiredTokenValid);
                } else {
                    //request.setAttribute("exception", e.getMessage());
                    logger.warn(e.getMessage());
                    authenticateForRefreshToken(e, request, expiredTokenValid);
                }
            }
        } else {
            //to check using needFilter method for urls to authenticate or not
            // this code is required if mapping in security config does not work
           // if (needFilter(request.getServletPath()) && authPass) {

                //check if basicAuth token is present or not
                if (requestTokenHeader != null) {
                    //parsing and authenticating basicAuth token
                    logger.warn("JWT Token does not begin with Bearer String");
                    base64Token = new String(Base64.getDecoder().decode(requestTokenHeader.substring(6)));
                    String user = base64Token.split(":")[0];
                    UserDetails userDetails = urlShortUserDetailService.loadUserByUsername(user);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            // this code is required if mapping in security config does not work
           /* } else {
                //customly setting authentication to true
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(null, null, null);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } */
        }

        // Once we get the token validate it.
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.urlShortUserDetailService.loadUserByUsername(username);

                // if token is valid configure Spring Security to manually set
                // authentication
                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the
                    // Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    /*
    method for avoid authentication for perticular urls
    returns only true or false
    this code is required if mapping in security config does not work
     */
    public boolean needFilter(String path) {
        if (path.contains("/actuator") || path.contains("/swagger-ui.html") || path.contains("/webjars/springfox-swagger-ui") ||
                path.contains("/swagger-resources") || path.contains("/v2") || path.contains("/authenticate")) {
            return false;
        }
        return true;
    }

    /*
    customly setting authentication to true for refreshtoken
     */
    private void authenticateForRefreshToken(ExpiredJwtException ex, HttpServletRequest request, boolean expiredTokenValidity) {

        // create a UsernamePasswordAuthenticationToken with null values.
         UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(null, null, null);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
         SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // Set the claims so that in controller we will be using it to create
        // new JWT
        request.setAttribute("expiredTokenValidity", expiredTokenValidity);
        request.setAttribute("claims", ex.getClaims());

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
