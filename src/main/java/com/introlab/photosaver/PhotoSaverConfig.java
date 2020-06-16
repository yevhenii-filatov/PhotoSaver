package com.introlab.photosaver;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yevhenii Filatov
 * @since 6/16/20
 **/

@Configuration
public class PhotoSaverConfig {
    @Bean
    public UrlValidator urlValidator () {
        return new UrlValidator();
    }
}
