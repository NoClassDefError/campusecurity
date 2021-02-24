package cn.macswelle.campusecurity.devicemanage.client;

import cn.macswelle.campusecurity.common.Adapter;
import cn.macswelle.campusecurity.common.entities.Device;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

/**
 * 接口监听服务要接入系统，必须要实现以下api
 */
@FeignClient(value = "")
public interface AdapterClient {
    /**
     * 获取基本服务信息
     */
    Adapter getInfo();

    /**
     * 获取接入的硬件
     */
    List<Device> getDevice();


}
