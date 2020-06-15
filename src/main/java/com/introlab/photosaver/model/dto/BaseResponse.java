package com.introlab.photosaver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Yevhenii Filatov
 * @since 6/15/20
 **/

@Getter
@AllArgsConstructor
public class BaseResponse {
    private final String status;
    private final int code;
}
