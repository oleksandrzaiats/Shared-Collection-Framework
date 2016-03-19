package se.lnu.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.lnu.application.security.TokenAuthenticationFilter;
import se.lnu.application.security.UserRole;

@EnableWebSecurity
@Configuration
@Order(2)
@ComponentScan({"se.lnu"})
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    public SpringSecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // Basic entities
                .antMatchers("/api/v1.0/collection/**").hasAnyAuthority(new String[]{UserRole.ROLE_ADMIN.toString(), UserRole.ROLE_USER.toString()})
                .antMatchers("/api/v1.0/artifact/**").hasAnyAuthority(new String[]{UserRole.ROLE_ADMIN.toString(), UserRole.ROLE_USER.toString()})
                // User management
                .antMatchers(HttpMethod.DELETE, "/api/v1.0/user/**").hasAnyAuthority(UserRole.ROLE_ADMIN.toString())
                .antMatchers(HttpMethod.GET, "/api/v1.0/user/**").hasAnyAuthority(UserRole.ROLE_ADMIN.toString())
                .antMatchers(HttpMethod.PUT, "/api/v1.0/user/**").hasAnyAuthority(new String[]{UserRole.ROLE_ADMIN.toString(), UserRole.ROLE_USER.toString()})
                // Preflight requests support
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll().and()
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().and()
                .headers().cacheControl();

    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

}
