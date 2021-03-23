package cn.macswelle.campusecurity.feignapi.deviceManager;

import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * eureka向设备管理中心发送信息的api
 */
@FeignClient(value = "devicemanage", path = "/device")
public interface EurekaApi {
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    HttpResult instanceRegister(@RequestBody DeviceDto deviceDto);

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    void instanceCancel(@RequestParam("id") String id);

}
