package cn.macswelle.campusecurity.userservice.service;

import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.requestDto.SignUpDto;
import cn.macswelle.campusecurity.common.dto.responseDto.UserDto;

public interface SignUpService {
    HttpResult signUp(SignUpDto signUpDto);

    HttpResult send(String email);

    HttpResult changeUser(UserDto userDto);

    HttpResult remove(String id);
    HttpResult changePassword(String original, String newPassword);

  HttpResult changeDescription(String d);
}
