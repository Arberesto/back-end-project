package it.sevenbits.taskmanager.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import it.sevenbits.taskmanager.core.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to generate and parse JWT tokens.
 */
@Service
public class JsonWebTokenService implements JwtTokenService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtSettings settings;
    private static final String AUTHORITIES = "authorities";
    private static final String ENABLED = "enabled";

    /**
     * Default constructor
     *
     * @param settings JwtSettings about Jwt tokens
     */

    public JsonWebTokenService(final JwtSettings settings) {
        this.settings = settings;
    }

    @Override
    public String createToken(final User user) {
        logger.debug("Generating token for {}", user.getUsername());
        String result = "";
        Instant now = Instant.now();
        Claims claims = Jwts.claims()
                .setId(user.getId())
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(now))
                .setSubject(user.getUsername())
                .setExpiration(Date.from(now.plus(Duration.ofMinutes(getTokenExpiredInMinutes()))));
        claims.put(AUTHORITIES, user.getAuthorities());
        claims.put(ENABLED, user.getEnabled());
        logger.debug("Claims authorities is also fine");
        JwtBuilder builder = Jwts.builder();
        builder = builder.setClaims(claims);
        builder = builder.signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey());
        logger.debug("Nothing wrong with signing keys");
        try {
            result = builder.compact();
        } catch (Exception e) {
            logger.error("Something wrong with builder.compact()");
        }
        logger.debug("Returning token with no problem");
        return result;
    }

    @Override
    public int getTokenExpiredInSeconds() {
        return settings.getTokenExpiredIn() * 60; // store in minutes, but need seconds
    }

    @Override
    public int getTokenExpiredInMinutes() {
        return settings.getTokenExpiredIn();
    }

    @Override
    public Authentication parseToken(final String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(settings.getTokenSigningKey()).parseClaimsJws(token);
        logger.debug(String.format("id from claims when parse token - %s", claims.getBody().getId()));
        String id = claims.getBody().getId();
        String subject = claims.getBody().getSubject();
        boolean enabled = Boolean.parseBoolean(claims.getBody().get("enabled").toString());
        List<String> tokenAuthorities = claims.getBody().get(AUTHORITIES, List.class);
        List<GrantedAuthority> authorities = tokenAuthorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new AuthenticatedJwtToken(subject, authorities, id, enabled);
    }

}
