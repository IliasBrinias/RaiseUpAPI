package com.unipi.msc.riseupapi.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class EditUserRequest {
    private MultipartFile multipartFile;
    private String password;
    private String firstName;
    private String lastName;
}
