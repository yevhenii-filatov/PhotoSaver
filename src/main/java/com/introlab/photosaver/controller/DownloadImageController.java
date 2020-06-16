package com.introlab.photosaver.controller;

import com.introlab.photosaver.download.ImageDownloader;
import com.introlab.photosaver.model.dto.BaseResponse;
import com.introlab.photosaver.repository.LinkStateRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

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
    private final LinkStateRepository linkStateRepository;

    public DownloadImageController(ImageDownloader imageDownloader, UrlValidator urlValidator, LinkStateRepository linkStateRepository) {
        this.imageDownloader = imageDownloader;
        this.urlValidator = urlValidator;
        this.linkStateRepository = linkStateRepository;
    }

    @GetMapping("/download-image")
    public BaseResponse saveImage(@RequestParam(name = "profileUrl") String profileUrl,
                                  @RequestParam(name = "imageUrl") String imageUrl) {
        if (!urlValidator.isValid(profileUrl) || !urlValidator.isValid(imageUrl)) {
            return BaseResponse.INCORRECT_QUERY_PARAMETERS;
        }

        try {
            File image = imageDownloader.download(profileUrl, imageUrl);
            saveImageNameToStateDb(image, profileUrl);
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

    @Transactional
    protected void saveImageNameToStateDb(File image, String profileUrl) {
        linkStateRepository.setImageNameForLink(profileUrl, image.getName());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse handleMissingParamsError() {
        return BaseResponse.INCORRECT_QUERY_PARAMETERS;
    }
}