package cn.macswelle.campusecurity.devicemanage.controller;

import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.common.dto.LocationDto2;
import cn.macswelle.campusecurity.common.dto.requestDto.RecordDto;
import cn.macswelle.campusecurity.common.dto.responseDto.RecordDto2;
import cn.macswelle.campusecurity.devicemanage.service.DeviceManageService;
import cn.macswelle.campusecurity.feignapi.deviceManager.ManagerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("/device")
public class DeviceManageController implements ManagerApi {
    @Autowired
    private DeviceManageService deviceManageService;

    @RequestMapping(value = "/getSons", method = RequestMethod.POST)
    @Override
    public List<DeviceDto> getSons(String id) {
        return deviceManageService.getSons(id);
    }

    @RequestMapping(value = "/getDevices", method = RequestMethod.POST)
    @Override
    public ArrayList<DeviceDto> refreshServices() {
        return deviceManageService.refreshServices();
    }

    @RequestMapping(value = "/changeLocation", method = RequestMethod.POST)
    @Override
    public String refractorOrAddLocation(LocationDto2 locationDto) {
        return deviceManageService.refractorOrAddLocation(locationDto);
    }

    @RequestMapping(value = "/getLocations", method = RequestMethod.POST)
    @Override
    public List<LocationDto2> getLocations() {
        return deviceManageService.getLocations();
    }

    @RequestMapping(value = "/getDeviceByLocation", method = RequestMethod.POST)
    @Override
    public List<DeviceDto> getByLocation(String id) {
        return deviceManageService.getByLocation(id);
    }

    @RequestMapping(value = "/getRecords", method = RequestMethod.POST)
    @Override
    public List<RecordDto2> getRecords(String locationId, Long start, Long end) {
        return deviceManageService.getRecords(locationId, start, end);
    }

    @RequestMapping(value = "/addRecord", method = RequestMethod.POST)
    @Override
    public void addRecord(RecordDto dto) {
        deviceManageService.addRecord(dto);
    }
}
