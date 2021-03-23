package cn.macswelle.campusecurity.devicemanage.service;

import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.common.dto.LocationDto2;
import cn.macswelle.campusecurity.common.dto.requestDto.RecordDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.responseDto.RecordDto2;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备管理服务需要实现的功能：
 * 从注册中心读取硬件服务内容与性质
 * 在页面展示各硬件的管理页面
 * 接受接口监听服务与设备通信服务通过消息总线传来的数据，写入数据库，包括何人何时出现在何地
 */
public interface DeviceManageService {
    //来自用户的请求
    List<DeviceDto> getSons(String id);

    ArrayList<DeviceDto> refreshServices();

    /**
     * 由于存在级联问题，location不能删除，只能修改基本信息
     */
    HttpResult refractorOrAddLocation(LocationDto2 locationDto);

    List<LocationDto2> getLocations();

    List<DeviceDto> getByLocation(String id);

    List<RecordDto2> getRecords(String locationId, Long start, Long end);

    //来自eureka的请求
    HttpResult register(DeviceDto deviceDto);

    void cancel(String id);

    //来自硬件的请求
    void addRecord(RecordDto dto);

}
