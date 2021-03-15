package cn.macswelle.campusecurity.feignapi.adapter;

import cn.macswelle.campusecurity.common.dto.DeviceDto;

import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 接口监听服务要接入系统，必须要实现以下api
 */
@FeignClient(value = "adapter")
public interface AdapterApi {

    /**
     * 获取接入的硬件
     */
    @RequestLine("POST /getDevice")
    List<DeviceDto> getDevice();

    /**
     * 由于这个方法被原生Feign调用，spring的注解无效，要使用Feign的注解
     */
    @RequestLine("POST /getInfo")
    DeviceDto getInfo();
}
