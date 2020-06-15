package com.introlab.photosaver.download;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;

/**
 * @author Yevhenii Filatov
 * @since 6/15/20
 **/

@Service
public class ImageDownloader {
    @Value("${destination.folder}")
    private String destinationFolder;
    private static final String SLASH = "/";

    public File download(String profileUrl, String imageUrl) {
        return download(profileUrl, imageUrl, Format.PNG);
    }

    public File download(String profileUrl, String imageUrl, Format format) {
        String imagePath = generateImagePath(profileUrl, format);
        File image = new File(imagePath);
        HttpResponse<File> response = Unirest
                .get(imageUrl)
                .asFile(image.getPath());
        return response.isSuccess() ? response.getBody() : null;
    }

    private String generateImagePath(String profileUrl, Format format) {
        String normalizedProfileUrl = removeEnd(profileUrl, SLASH);
        normalizedProfileUrl = substringAfterLast(normalizedProfileUrl, SLASH);
        String uuid = UUID.randomUUID().toString();
        return String.format("%s/%s-%s.%s", destinationFolder, normalizedProfileUrl, uuid, format);
    }

    public enum Format {
        JPG,
        PNG,
        BMP;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
