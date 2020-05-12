package it.sevenbits.taskmanager.web.security;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Token that app used as representation of authenticated User
 */

public class AuthenticatedJwtToken extends AbstractAuthenticationToken {

    private final String subject;
    private final String id;
    private final Boolean enabled;

    /**
     * Default constructor
     * @param subject name of authenticated user
     * @param authorities authorities of subject
     * @param id id of user
     * @param enabled status of user
     */

    AuthenticatedJwtToken(final String subject, final Collection<GrantedAuthority> authorities,
                          final String id, final boolean enabled) {
        super(authorities);
        this.subject = subject;
        this.id = id;
        this.enabled = enabled;
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
        Map<String, String> result = new HashMap<>();
        result.put("subject", subject);
        result.put("id", id);
        result.put("enabled", enabled.toString());
        return result;
    }

}
