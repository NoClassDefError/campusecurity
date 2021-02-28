package cn.macswelle.campusecurity.feignapi.deviceManager;

import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.common.dto.LocationDto2;
import cn.macswelle.campusecurity.common.dto.requestDto.RecordDto;
import cn.macswelle.campusecurity.common.dto.responseDto.RecordDto2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户向设备管理中心发送信息的api
 * 最后一个特殊，是adapter向设备管理中心发送信息的api
 */
@FeignClient(value = "deviceManager", path = "/device")
public interface ManagerApi {

    @RequestMapping(value = "/getSons", method = RequestMethod.POST)
    List<DeviceDto> getSons(String id);

    @RequestMapping(value = "/getDevices", method = RequestMethod.POST)
    ArrayList<DeviceDto> refreshServices();

    @RequestMapping(value = "/changeLocation", method = RequestMethod.POST)
    String refractorOrAddLocation(LocationDto2 locationDto);

    @RequestMapping(value = "/getLocations", method = RequestMethod.POST)
    List<LocationDto2> getLocations();

    @RequestMapping(value = "/getDeviceByLocation", method = RequestMethod.POST)
    List<DeviceDto> getByLocation(String id);

    @RequestMapping(value = "/getRecords", method = RequestMethod.POST)
    List<RecordDto2> getRecords(String locationId, Long start, Long end);

    @RequestMapping(value = "/addRecord", method = RequestMethod.POST)
    void addRecord(RecordDto dto);
}
