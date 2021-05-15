package cn.macswelle.campusecurity.sdk.controller;

import cn.macswelle.campusecurity.sdk.DeviceInfo;
import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.feignapi.adapter.AdapterApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class AdapterController implements AdapterApi {

  @Autowired
  private DeviceInfo deviceInfo;

  @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
  @ResponseBody
  @Override
  public DeviceDto getInfo() {
    DeviceDto deviceDto = new DeviceDto();
    deviceDto.setUrl(deviceInfo.getUrl());
    deviceDto.setName(deviceInfo.getName());
    deviceDto.setVersion(deviceInfo.getVersion());
    deviceDto.setLocation(deviceInfo.getLocation());
    deviceDto.setDescription(deviceInfo.getDescription());
    deviceDto.setCategory(deviceInfo.getCategory());
    return deviceDto;
  }
}
