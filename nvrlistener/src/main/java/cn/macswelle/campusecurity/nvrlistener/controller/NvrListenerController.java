package cn.macswelle.campusecurity.nvrlistener.controller;

import cn.macswelle.campusecurity.common.entities.Device;
import cn.macswelle.campusecurity.devicemanage.client.AdapterClient;
import cn.macswelle.campusecurity.nvrlistener.Info;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 这个微服务与设备管理服务之间的通信是不对称的，
 * 设备管理服务要访问本服务，必须加入feignClient接口
 * 本服务访问设备管理服务要通过网关
 */

@RestController
public class NvrListenerController implements AdapterClient {

    @RequestMapping("/getInfo")
    public Info getInfo() {
        return new Info();
    }

    @Override
    public List<Device> getDevice() {
        return null;
    }


}
