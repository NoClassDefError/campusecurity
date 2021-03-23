package cn.macswelle.campusecurity.devicemanage.controller;

import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.devicemanage.service.DeviceManageService;
import cn.macswelle.campusecurity.feignapi.deviceManager.EurekaApi;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
public class EurekaController implements EurekaApi {
  @Autowired
  private DeviceManageService deviceManageService;

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  @ResponseBody
  @Override
  public HttpResult instanceRegister(@RequestBody DeviceDto deviceDto) {
    LoggerFactory.getLogger(this.getClass()).info(deviceDto.toString());
    return deviceManageService.register(deviceDto);
  }

  @RequestMapping(value = "/cancel", method = RequestMethod.POST)
  @Override
  public void instanceCancel(@RequestParam("id") String id) {
    deviceManageService.cancel(id);
  }
}
