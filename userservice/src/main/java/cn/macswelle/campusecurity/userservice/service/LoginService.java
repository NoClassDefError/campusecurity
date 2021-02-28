package cn.macswelle.campusecurity.userservice.service;

import cn.macswelle.campusecurity.common.dto.requestDto.LoginDto;
import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.common.dto.responseDto.LogoutDto;
import cn.macswelle.campusecurity.common.dto.responseDto.UserDto;

import java.util.List;

public interface LoginService {
    LoginDto2 login(LoginDto loginDto);
    LogoutDto logout();
    List<UserDto> getUsers();
    UserDto getUser(String token);
}
