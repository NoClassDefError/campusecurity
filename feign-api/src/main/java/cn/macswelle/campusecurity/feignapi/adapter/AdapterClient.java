package cn.macswelle.campusecurity.feignapi.adapter;

import cn.macswelle.campusecurity.common.Adapter;
import cn.macswelle.campusecurity.common.entities.Device;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;;

import java.net.URI;
import java.util.List;

/**
 * 接口监听服务要接入系统，必须要实现以下api
 */
@FeignClient(value = "adapter")
public interface AdapterClient {

    /**
     * 获取接入的硬件
     */
    @RequestMapping(value = "/getDevice",method = RequestMethod.POST)
    List<Device> getDevice();

    @RequestMapping(value = "/getInfo",method = RequestMethod.POST)
    Adapter getInfo();
}
