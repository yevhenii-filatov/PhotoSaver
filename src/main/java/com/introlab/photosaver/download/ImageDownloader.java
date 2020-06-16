package com.introlab.photosaver.download;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;

/**
 * @author Yevhenii Filatov
 * @since 6/15/20
 **/

@Slf4j
@Service
public class ImageDownloader {
    @Value("${destination.folder}")
    private String destinationFolder;
    private static final String SLASH = "/";

    public File download(String profileUrl, String imageUrl) throws IOException {
        return download(profileUrl, imageUrl, Format.PNG);
    }

    public File download(String profileUrl, String imageUrl, Format format) throws IOException {
        String rawImagePath = generateImagePath(profileUrl, format);
        Path imagePath = Paths.get(rawImagePath);
        if (Files.exists(imagePath)) {
            log.warn("Overwriting profile photo for {}", profileUrl);
            Files.delete(imagePath);
        }

        File image = new File(rawImagePath);
        HttpResponse<File> response = Unirest
                .get(imageUrl)
                .asFile(image.getPath());

        if (!response.isSuccess()) {
            throwRelevantException(response);
        }
        return response.getBody();
    }

    private void throwRelevantException(HttpResponse<File> response) throws IOException {
        switch (response.getStatus()) {
            case 400 :
                throw new IOException("bad request");

            case 403 :
                throw new IOException("forbidden");

            case 404 :
                throw new IOException("not found");

            case 500 :
                throw new IOException("internal server error");

            default:
                throw new IOException(response.getStatusText());
        }
    }

    private String generateImagePath(String profileUrl, Format format) {
        String normalizedProfileUrl = removeEnd(profileUrl, SLASH);
        normalizedProfileUrl = substringAfterLast(normalizedProfileUrl, SLASH);
        return String.format("%s/%s.%s", destinationFolder, normalizedProfileUrl, format);
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
