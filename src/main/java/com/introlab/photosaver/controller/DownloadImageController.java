package com.introlab.photosaver.controller;

import com.introlab.photosaver.download.ImageDownloader;
import com.introlab.photosaver.model.payload.SaveImageRequest;
import com.introlab.photosaver.repository.LinkRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;

/**
 * @author Yevhenii Filatov
 * @since 6/15/20
 **/

@Slf4j
@RestController
@RequestMapping("/api")
public class DownloadImageController {
    private final ImageDownloader imageDownloader;
    private final UrlValidator urlValidator;
    private final LinkRepository linkRepository;

    public DownloadImageController(ImageDownloader imageDownloader,
                                   UrlValidator urlValidator,
                                   LinkRepository linkRepository) {
        this.imageDownloader = imageDownloader;
        this.urlValidator = urlValidator;
        this.linkRepository = linkRepository;
    }

    @PostMapping("/download-image")
    public ResponseEntity saveImage(@RequestBody SaveImageRequest saveImageRequest) {
        String profileUrl = saveImageRequest.getProfileUrl();
        String imageUrl = saveImageRequest.getImageUrl();
        log.info("{}", profileUrl);
        log.info("{}", imageUrl);
        if (!urlValidator.isValid(profileUrl) || !urlValidator.isValid(imageUrl)) {
            return new ResponseEntity("not valid input params", HttpStatus.BAD_REQUEST);
        }
        ResponseEntity result = process(profileUrl, imageUrl);
        log.info("{} for {}\n", result.getStatusCode().getReasonPhrase(), profileUrl);
        return result;
    }

    private ResponseEntity process(String profileUrl, String imageUrl) {
        try {
            File image = imageDownloader.download(profileUrl, imageUrl);
            saveImageNameToStateDb(image, profileUrl);
            return new ResponseEntity("not valid input params", HttpStatus.OK);
        } catch (IOException e) {
            switch (e.getMessage()) {
                case "bad request":
                    return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
                case "not found":
                    return new ResponseEntity(HttpStatus.NOT_FOUND);
                case "internal server error":
                    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                case "forbidden":
                    return new ResponseEntity(HttpStatus.FORBIDDEN);
                default:
                    return new ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
            }
        }
    }

    @Transactional
    protected void saveImageNameToStateDb(File image, String profileUrl) {
        //linkRepository.setImageNameForLink(profileUrl, image.getName());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity handleMissingParamsError() {
        return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }
    
}