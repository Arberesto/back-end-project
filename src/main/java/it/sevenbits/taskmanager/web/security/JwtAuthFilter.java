package it.sevenbits.taskmanager.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Abstract filter to take JwtToken from the http request.
 */
public abstract class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Default constructor
     * @param matcher RequestMatcher object to define at which paths this filter will be working
     */


    JwtAuthFilter(final RequestMatcher matcher) {
        super(matcher);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response) throws AuthenticationException {
        String token;
        try {
            token = takeToken(request);
        } catch (Exception e) {
            logger.warn("Failed to get token: {}", e.getMessage());
            return anonymousToken();
        }
        return new JwtToken(token);
    }

    /**
     * Get token from request
     * @param request Http request object to take token from
     * @return token
     * @throws AuthenticationException throw if can't take token
     */

    protected abstract String takeToken(HttpServletRequest request) throws AuthenticationException;

    private Authentication anonymousToken() {
        return new AnonymousAuthenticationToken("ANONYMOUS", "ANONYMOUS",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final FilterChain chain,
                                            final Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
