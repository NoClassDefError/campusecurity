package cn.macswelle.campusecurity.common.dto.requestDto;

import lombok.Data;

@Data
public class SignUpDto {
    String phone;
    String password;
    int code;
}
