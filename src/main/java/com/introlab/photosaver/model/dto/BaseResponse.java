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
    public static final BaseResponse SUCCESS = new BaseResponse("success", 200);
    public static final BaseResponse INCORRECT_QUERY_PARAMETERS = new BaseResponse("bad request - incorrect query params", 400);
    public static final BaseResponse FORBIDDEN = new BaseResponse("forbidden", 403);
    public static final BaseResponse NOT_FOUND = new BaseResponse("not found", 404);
    public static final BaseResponse INTERNAL_SERVER_ERROR = new BaseResponse("internal server error", 500);

    private final String status;
    private final int code;
}
