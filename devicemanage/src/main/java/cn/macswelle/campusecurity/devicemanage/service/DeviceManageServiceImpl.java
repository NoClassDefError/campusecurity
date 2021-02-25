package cn.macswelle.campusecurity.devicemanage.service;

import cn.macswelle.campusecurity.feignapi.adapter.AdapterClient;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;


@Service
public class DeviceManageServiceImpl implements DeviceManageService {

    @Autowired
    private DiscoveryClient discoveryClient;

    private AdapterClient adapterClient;

    @Override
    public void getServices() {
        discoveryClient.getServices().forEach(id -> discoveryClient.getInstances(id).forEach(serviceInstance -> {
            // 获取服务信息，重要！feign接口指定url的方法
            String infoUrl = serviceInstance.getUri().toString();
            adapterClient = Feign.builder().target(AdapterClient.class, infoUrl);
            adapterClient.getInfo();
        }));
    }


}
