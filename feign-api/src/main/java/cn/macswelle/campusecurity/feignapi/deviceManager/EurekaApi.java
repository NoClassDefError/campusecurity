package cn.macswelle.campusecurity.feignapi.deviceManager;

import cn.macswelle.campusecurity.common.dto.DeviceDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * eureka向设备管理中心发送信息的api
 */
@FeignClient(value = "devicemanage", path = "/device")
public interface EurekaApi {
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    void instanceRegister(DeviceDto deviceDto);

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    void instanceCancel(String id);

}
