package cn.macswelle.campusecurity.common.dto.responseDto;

import lombok.Data;

@Data
public class UserDto {
    String id;
    String name;
    String description;
    Integer auth;
    String phone;
}
