package com.lgcns.backend.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SignUpRequestDto {
    String email;
    String password;
    String name;
    String nickname;
    @JsonProperty("profile_image")
    String profileImage;
    String role;
}
