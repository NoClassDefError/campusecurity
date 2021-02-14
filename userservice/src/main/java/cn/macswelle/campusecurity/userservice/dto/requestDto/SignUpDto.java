package cn.macswelle.campusecurity.userservice.dto.requestDto;

import lombok.Data;

@Data
public class SignUpDto {
    String name;
    String description;
    Integer auth;//限定为1或2

}
