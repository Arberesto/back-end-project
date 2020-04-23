package it.sevenbits.taskmanager.web.security;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class AuthenticatedJwtToken extends AbstractAuthenticationToken {

    private final String subject;
    private final String id;

    /**
     * Default constructor
     * @param subject name of authenticated user
     * @param authorities authorities of subject
     */

    AuthenticatedJwtToken(final String subject, final Collection<GrantedAuthority> authorities, final String id) {
        super(authorities);
        this.subject = subject;
        this.id = id;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }
    /*
    @Override
    public Object getPrincipal() {return subject;}
    */


     @Override
    public Object getPrincipal() {
        Map<String,String> result = new HashMap<>();
        result.put("subject", subject);
        result.put("id", id);
        return result;
    }

}
