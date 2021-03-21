package cn.macswelle.campusecurity.common.dto.requestDto;

import lombok.Data;

@Data
public class SignUpDto {
    String name;
    String description;
    Integer auth;//限定为0,1或2
    String id;
}
