package cn.macswelle.campusecurity.common.dto.responseDto;

import lombok.Data;

@Data
public class LoginDto2 {
    String id;
    String status;
    String name;
    String description;
    String auth;
    String token;
}

