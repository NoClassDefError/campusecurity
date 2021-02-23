package cn.macswelle.campusecurity.devicemanage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

@Service
public class DeviceManageServiceImpl implements DeviceManageService{

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public void getServices() {
        discoveryClient.getServices();

    }
}
