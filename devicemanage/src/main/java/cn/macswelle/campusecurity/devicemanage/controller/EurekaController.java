package cn.macswelle.campusecurity.devicemanage.controller;

import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.devicemanage.service.DeviceManageService;
import cn.macswelle.campusecurity.feignapi.deviceManager.EurekaApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
public class EurekaController implements EurekaApi {
    @Autowired
    private DeviceManageService deviceManageService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Override
    public void instanceRegister(DeviceDto deviceDto) {
        deviceManageService.register(deviceDto);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @Override
    public void instanceCancel(String id) {
        deviceManageService.cancel(id);
    }
}
