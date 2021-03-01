package cn.macswelle.campusecurity.common.dto.requestDto;

import lombok.Data;

@Data
public class LoginDto {
    String userId;
    String password;
}