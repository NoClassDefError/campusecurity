package cn.macswelle.campusecurity.userservice.service;

import cn.macswelle.campusecurity.userservice.dto.requestDto.LoginDto;
import cn.macswelle.campusecurity.userservice.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.userservice.dto.responseDto.LogoutDto;

public interface LoginService {
    LoginDto2 login(LoginDto loginDto);
    LogoutDto logout();
}
