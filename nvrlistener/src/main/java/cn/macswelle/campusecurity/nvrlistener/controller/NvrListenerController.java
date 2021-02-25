package cn.macswelle.campusecurity.nvrlistener.controller;

import cn.macswelle.campusecurity.common.Adapter;
import cn.macswelle.campusecurity.common.entities.Device;
import cn.macswelle.campusecurity.feignapi.adapter.AdapterClient;
import cn.macswelle.campusecurity.nvrlistener.Info;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 这个微服务与设备管理服务之间的通信是不对称的，
 * 设备管理服务要访问本服务，需要从服务注册中心获取本服务的信息。
 * 本服务访问设备管理服务要通过网关
 */

@RestController
public class NvrListenerController implements AdapterClient {

    @RequestMapping("/getDevices")
    @Override
    public List<Device> getDevice() {
        return null;
    }

    @Override
    public Adapter getInfo() {
        return new Info();
    }

}
