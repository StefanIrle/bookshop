package com.bookshop.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger configuration
 *
 */
@Configuration
public class SpringFoxConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis( RequestHandlerSelectors.basePackage("com.bookshop"))              
          .paths(PathSelectors.any())                          
          .build().apiInfo(apiInfo());                                           
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfo(
          "Bookshop REST API", 
          "Api to manage products and place orders", 
          "1.0", 
          null, 
          new Contact("Stefan Irle", null, "stefan@irle.com"), 
          null, null, Collections.emptyList());
    }
}