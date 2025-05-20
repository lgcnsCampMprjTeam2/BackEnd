package com.lgcns.backend.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {

    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "PETIMAGE404", "반려견 이미지를 찾지 못했습니다."),
    ;

    private HttpStatus httpStatus;
    private String message;
    private String code;

}
