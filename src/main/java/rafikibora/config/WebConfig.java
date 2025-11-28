/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package rafikibora.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
*
* @author bkariuki
*/
@Configuration
public class WebConfig implements WebMvcConfigurer {

@Override
public void addCorsMappings(CorsRegistry registry) {
registry.addMapping("/**").allowedOrigins("*");
}

// Static resources for Swagger UI are auto-configured by springdoc-openapi in Spring Boot 3+.

@Override
public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
configurer.defaultContentType(MediaType.APPLICATION_JSON);
}
}
