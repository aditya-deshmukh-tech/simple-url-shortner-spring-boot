package com.urlshortner.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/*
This class is used to customly authenticate request for unauthorized token for refreshing token
but not needed if <UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(null, null, null);>
is working for you
 */
public class CustomUrlShortAuthentication extends AbstractAuthenticationToken {

    public CustomUrlShortAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

}
