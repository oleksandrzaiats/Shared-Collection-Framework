package se.lnu.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
 
@EnableWebMvc
@Configuration
@ComponentScan({"se.lnu"})
public class SpringWebConfig extends WebMvcConfigurerAdapter {

}