package com.introlab.photosaver.controller;

import com.introlab.photosaver.download.ImageDownloader;
import com.introlab.photosaver.model.dto.BaseResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Objects;

/**
 * @author Yevhenii Filatov
 * @since 6/15/20
 **/

@Slf4j
@RestController
@RequestMapping("/")
public class DownloadImageController {
    private final ImageDownloader imageDownloader;
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 200;
    private static final int CODE_FAILURE = 500;

    public DownloadImageController(ImageDownloader imageDownloader) {
        this.imageDownloader = imageDownloader;
    }

    @GetMapping("/download-image")
    public BaseResponse saveImage(@RequestParam(value = "profileUrl") String profileUrl,
                          @RequestParam(value = "imageUrl") String imageUrl) {
        File image = imageDownloader.download(profileUrl, imageUrl);
        if (Objects.isNull(image)) {
            return new BaseResponse(ERROR_STATUS, CODE_FAILURE);
        } else {
            return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS);
        }
    }
}
