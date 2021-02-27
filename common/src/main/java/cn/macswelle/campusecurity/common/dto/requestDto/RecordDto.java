package cn.macswelle.campusecurity.common.dto.requestDto;

import lombok.Data;

@Data
public class RecordDto {
    private Long createTime;
    private String event;
    private String file;
    private String location;
    private String personnel;
}
