package cn.macswelle.campusecurity.devicemanage.controller;

import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.devicemanage.service.DeviceManageService;
import cn.macswelle.campusecurity.feignapi.deviceManager.EurekaApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EurekaController implements EurekaApi {
    @Autowired
    private DeviceManageService deviceManageService;

    @Override
    public void instanceRegister(DeviceDto deviceDto) {

    }

    @Override
    public void instanceCancel(String id) {

    }
}
