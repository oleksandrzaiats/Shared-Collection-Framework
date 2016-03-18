package se.lnu.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.lnu.application.processor.UserProcessor;
import se.lnu.application.security.TokenAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Order(2)
@ComponentScan({"se.lnu"})
public class TokenAuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserProcessor userProcessor;

    public TokenAuthenticationSecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //.antMatchers("/").permitAll()
                //.antMatchers("/favicon.ico").permitAll()
                //.antMatchers("/resources/**").permitAll()
                .antMatchers("/api/v1.0/collection/**").hasRole("USER")
                .antMatchers("/api/v1.0/artifact/**").hasRole("USER")
                .antMatchers("/api/v1.0/login").permitAll().and()
                //.antMatchers("/collection/**").hasRole("USER")
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().and()
                .anonymous().and()
                .servletApi().and()
                .headers().cacheControl();

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userProcessor).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    /*@Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    } */
}
