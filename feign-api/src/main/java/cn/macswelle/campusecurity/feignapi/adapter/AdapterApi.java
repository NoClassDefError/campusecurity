package cn.macswelle.campusecurity.feignapi.adapter;

import cn.macswelle.campusecurity.common.dto.DeviceDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

;

/**
 * 接口监听服务要接入系统，必须要实现以下api
 */
@FeignClient(value = "adapter")
public interface AdapterApi {

    /**
     * 获取接入的硬件
     */
    @RequestMapping(value = "/getDevice", method = RequestMethod.POST)
    List<DeviceDto> getDevice();

    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    DeviceDto getInfo();
}
