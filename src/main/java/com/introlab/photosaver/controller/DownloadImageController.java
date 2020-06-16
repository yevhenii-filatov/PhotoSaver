package com.introlab.photosaver.controller;

import com.introlab.photosaver.download.ImageDownloader;
import com.introlab.photosaver.model.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

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

    public DownloadImageController(ImageDownloader imageDownloader, UrlValidator urlValidator) {
        this.imageDownloader = imageDownloader;
        this.urlValidator = urlValidator;
    }

    @GetMapping("/download-image")
    public BaseResponse saveImage(@RequestParam(name = "profileUrl") String profileUrl,
                                  @RequestParam(name = "imageUrl") String imageUrl) {
        if (!urlValidator.isValid(profileUrl) || !urlValidator.isValid(imageUrl)) {
            return BaseResponse.INCORRECT_QUERY_PARAMETERS;
        }

        try {
            imageDownloader.download(profileUrl, imageUrl);
            return BaseResponse.SUCCESS;
        } catch (IOException e) {
            switch (e.getMessage()) {
                case "bad request" : return BaseResponse.INCORRECT_QUERY_PARAMETERS;
                case "not found" : return BaseResponse.NOT_FOUND;
                case "internal server error" : return BaseResponse.INTERNAL_SERVER_ERROR;
                case "forbidden" : return BaseResponse.FORBIDDEN;
                default: return new BaseResponse(e.getMessage(), 520);
            }
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse handleMissingParamsError() {
        return BaseResponse.INCORRECT_QUERY_PARAMETERS;
    }
}