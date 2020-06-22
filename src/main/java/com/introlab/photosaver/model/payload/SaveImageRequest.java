package com.introlab.photosaver.model.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Andrew Ruban
 * @since 6/22/20
 **/

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaveImageRequest {

    @JsonProperty("profile_url")
    String profileUrl;

    @JsonProperty("image_url")
    String imageUrl;

}
