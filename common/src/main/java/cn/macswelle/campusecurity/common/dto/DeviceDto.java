package cn.macswelle.campusecurity.common.dto;

import lombok.Data;

/**
 * eureka发给设备管理服务时用到的
 * 设备管理服务的页面上用到的
 */
@Data
public class DeviceDto {
    private String id;
    private String name;
    private String location;
    private String description;
    private String version;
    private Integer category;
}
