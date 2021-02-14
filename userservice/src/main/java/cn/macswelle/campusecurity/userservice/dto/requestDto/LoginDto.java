package cn.macswelle.campusecurity.userservice.dto.requestDto;

import lombok.Data;

@Data
public class LoginDto {
    String username;
    String password;
}