package it.sevenbits.taskmanager.web.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthenticatedJwtToken extends AbstractAuthenticationToken {

    private final String subject;

    /**
     * Default constructor
     * @param subject name of authenticated user
     * @param authorities authorities of subject
     */

    AuthenticatedJwtToken(final String subject, final Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.subject = subject;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return subject;
    }
}
