package cn.macswelle.campusecurity.userservice.service;

import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.requestDto.SignUpDto;

public interface SignUpService {
    HttpResult signUp(SignUpDto signUpDto);
    HttpResult remove(String id);
    HttpResult changePassword(String original, String newPassword);

  HttpResult changeDescription(String d);
}
