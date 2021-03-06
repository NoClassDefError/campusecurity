package cn.macswelle.campusecurity.feignapi.deviceManager;

import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.common.dto.LocationDto2;
import cn.macswelle.campusecurity.common.dto.requestDto.RecordDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.responseDto.RecordDto2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 前端向设备管理中心发送信息的api
 * 最后一个特殊，是adapter向设备管理中心发送信息的api
 */
@FeignClient(value = "devicemanage", path = "/device")
public interface ManagerApi {

    @RequestMapping(value = "/getDevices", method = RequestMethod.POST)
    ArrayList<DeviceDto> refreshServices();

    @RequestMapping(value = "/admin/changeLocation", method = RequestMethod.POST)
    HttpResult refractorOrAddLocation(LocationDto2 locationDto);

    @RequestMapping(value = "/getLocations", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<LocationDto2> getLocations();

    @RequestMapping(value = "/getDeviceByLocation", method = RequestMethod.POST)
    List<DeviceDto> getByLocation(String id);

    @RequestMapping(value = "/getRecords", method = RequestMethod.POST)
    List<RecordDto2> getRecords(@RequestParam("locationId") String locationId,
                                @RequestParam("start") Long start,
                                @RequestParam("end") Long end);
}
