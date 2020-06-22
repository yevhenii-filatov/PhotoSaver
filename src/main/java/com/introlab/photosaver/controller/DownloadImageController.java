package com.introlab.photosaver.controller;

import com.introlab.photosaver.download.ImageDownloader;
import com.introlab.photosaver.model.dto.BaseResponse;
import com.introlab.photosaver.model.payload.SaveImageRequest;
import com.introlab.photosaver.repository.LinkRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
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
    public BaseResponse saveImage(@RequestBody SaveImageRequest saveImageRequest) {
        String profileUrl = saveImageRequest.getProfileUrl();
        String imageUrl = saveImageRequest.getImageUrl();
        log.info("{}", profileUrl);
        log.info("{}", imageUrl);
        if (!urlValidator.isValid(profileUrl) || !urlValidator.isValid(imageUrl)) {
            return BaseResponse.INCORRECT_QUERY_PARAMETERS;
        }
        BaseResponse result = process(profileUrl, imageUrl);
        log.info("{}\n", result.getStatus());
        return result;
    }

    private BaseResponse process(String profileUrl, String imageUrl) {
        try {
            File image = imageDownloader.download(profileUrl, imageUrl);
            saveImageNameToStateDb(image, profileUrl);
            return BaseResponse.SUCCESS;
        } catch (IOException e) {
            switch (e.getMessage()) {
                case "bad request":
                    return BaseResponse.INCORRECT_QUERY_PARAMETERS;
                case "not found":
                    return BaseResponse.NOT_FOUND;
                case "internal server error":
                    return BaseResponse.INTERNAL_SERVER_ERROR;
                case "forbidden":
                    return BaseResponse.FORBIDDEN;
                default:
                    return new BaseResponse(e.getMessage(), 520);
            }
        }
    }

    @Transactional
    protected void saveImageNameToStateDb(File image, String profileUrl) {
        //linkRepository.setImageNameForLink(profileUrl, image.getName());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse handleMissingParamsError() {
        return BaseResponse.INCORRECT_QUERY_PARAMETERS;
    }
}