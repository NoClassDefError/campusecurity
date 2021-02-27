package cn.macswelle.campusecurity.common.dto.responseDto;

import lombok.Data;

@Data
public class RecordDto2 {
    private String id;
    private Long createTime;
    private String event;
    private String  file;
    private String locationName;
    private String personnelId;
    private String personnelName;
    private String locationId;
}
