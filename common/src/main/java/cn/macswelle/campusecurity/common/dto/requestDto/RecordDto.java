package cn.macswelle.campusecurity.common.dto.requestDto;

import lombok.Data;

@Data
public class RecordDto {
    private Long createTime = System.currentTimeMillis();
    private String event = null;
    private String file = null;
    private String location = null;
    private String personnel = null;
}
