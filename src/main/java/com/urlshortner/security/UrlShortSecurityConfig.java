package com.urlshortner.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UrlShortSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] PUBLIC_URLS = {
            "/authenticate",
            "/actuator/**"
    };

    private UrlShortUserDetailService urlShortUserDetailService;

    private UrlShortAuthFilter urlShortAuthFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public PasswordEncoder getPassWordEncoder(){
        return  new MessageDigestPasswordEncoder("SHA-512");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests().antMatchers("/secure/register").hasAuthority("ADMIN")
                .and().authorizeRequests().antMatchers("/url.short/*").hasAuthority("NORMAL")
                .and().authorizeRequests().antMatchers(PUBLIC_URLS).permitAll()
                .anyRequest().authenticated().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(urlShortAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Autowired
    public void setUrlShortUserDetailService(UrlShortUserDetailService urlShortUserDetailService) {
        this.urlShortUserDetailService = urlShortUserDetailService;
    }

    @Autowired
    public void setUrlShortAuthFilter(UrlShortAuthFilter urlShortAuthFilter) {
        this.urlShortAuthFilter = urlShortAuthFilter;
    }

    /*
    new way to configure security without using depricated WebSecurityConfigurerAdapter

            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                    http
                        .cors().and()
                        .csrf().disable()
                        .authorizeRequests().antMatchers("/secure/register").hasAuthority("ADMIN")
                        .and().authorizeRequests().antMatchers("/url.short/*").hasAuthority("NORMAL")
                        .and().authorizeRequests().antMatchers(PUBLIC_URLS).permitAll()
                        .anyRequest().authenticated().and()
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                http.addFilterBefore(urlShortAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
            }

            @Bean
            public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
            }

        @Bean
        public AuthenticationManager authenticationManager(
                AuthenticationConfiguration authConfig) throws Exception {
            return authConfig.getAuthenticationManager();
        }

     */
}
