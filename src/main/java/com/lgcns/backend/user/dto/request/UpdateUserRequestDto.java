package com.lgcns.backend.user.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateUserRequestDto {
    @JsonProperty("current_password")
    String currentPassword;
    @JsonProperty("new_password")
    String newPassword;
    String nickname;
    @JsonProperty("profile_image")
    String profileImage;
}
