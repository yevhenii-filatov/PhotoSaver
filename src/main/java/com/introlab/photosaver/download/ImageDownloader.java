package com.introlab.photosaver.download;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        HttpResponse<byte[]> response = Unirest.get(imageUrl).asBytes();
        if (!response.isSuccess()) {
            throwRelevantException(response);
        }

        String rawImagePath = generateImagePath(profileUrl);
        
        Path imagePath = Paths.get(rawImagePath);
        imagePath.toFile().mkdirs();
        if (Files.exists(imagePath)) {
            log.warn("Overwriting profile photo for {}", profileUrl);
            Files.delete(imagePath);
        }

        File image = new File(rawImagePath);
        InputStream byteInputStream = new ByteArrayInputStream(response.getBody());
        BufferedImage bufferedImage = ImageIO.read(byteInputStream);
        ImageIO.write(bufferedImage, "png", image);
        return image;
    }

    private void throwRelevantException(HttpResponse<byte[]> response) throws IOException {
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

    private String generateImagePath(String profileUrl) {
        String normalizedProfileUrl = removeEnd(profileUrl, SLASH);
        normalizedProfileUrl = substringAfterLast(normalizedProfileUrl, SLASH);
        return String.format("%s/%s.png", destinationFolder, normalizedProfileUrl);
    }
}
