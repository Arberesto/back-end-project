package it.sevenbits.taskmanager.config;

import it.sevenbits.taskmanager.web.security.JwtAuthFilter;
import it.sevenbits.taskmanager.web.security.JwtTokenService;
import it.sevenbits.taskmanager.web.security.JwtAuthenticationProvider;
import it.sevenbits.taskmanager.web.security.JwtSettings;
import it.sevenbits.taskmanager.web.security.JsonWebTokenService;
import it.sevenbits.taskmanager.web.security.HeaderJwtAuthFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 *Config for web security of application
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtTokenService jwtTokenService;

    /**
     * Default constructor
     * @param jwtTokenService service to use in JwtAuthenticationProvider
     */

    public WebSecurityConfig(final JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.formLogin().disable();
        http.logout().disable();
        http.sessionManagement().disable();
        http.requestCache().disable();
        http.anonymous();

        RequestMatcher loginPageMatcher = new AntPathRequestMatcher("/signin");
        RequestMatcher notLoginPageMatcher = new NegatedRequestMatcher(loginPageMatcher);

        JwtAuthFilter authFilter = new HeaderJwtAuthFilter(notLoginPageMatcher);
        //JwtAuthFilter authFilter = new CookieJwtAuthFilter(notLoginPageMatcher);
        http.addFilterBefore(authFilter, FilterSecurityInterceptor.class);

        http
                .authorizeRequests()
                .antMatchers("/signup", "/signin").permitAll()
                //.antMatchers("/signin/**", "/signup/**").hasAuthority("ANONYMOUS")
                .antMatchers("/users/**").hasAuthority("ADMIN")
                .antMatchers("/whoami/**", "/tasks/**").hasAuthority("USER")
                .anyRequest().authenticated();
    }

    @Override
    public void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new JwtAuthenticationProvider(jwtTokenService));
    }

    /**
     * Singleton PasswordEncoder
     * @return PasswordEncoder object
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Singleton JwtTokenService
     * @param settings JwtSettings object thar contains settings for jwt tokens
     * @return JwtTokenService object
     */

    @Bean
    @Qualifier("jwtTokenService")
    public JwtTokenService jwtTokenService(final JwtSettings settings) {
        return new JsonWebTokenService(settings);
    }
}
