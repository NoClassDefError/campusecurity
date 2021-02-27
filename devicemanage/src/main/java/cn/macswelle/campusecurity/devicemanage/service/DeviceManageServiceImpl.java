package cn.macswelle.campusecurity.devicemanage.service;

import cn.macswelle.campusecurity.common.DeviceNotRespondException;
import cn.macswelle.campusecurity.common.EntityNotFoundException;
import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.common.dto.LocationDto2;
import cn.macswelle.campusecurity.common.entities.Device;
import cn.macswelle.campusecurity.common.entities.Location;
import cn.macswelle.campusecurity.devicemanage.repositories.DeviceRepository;
import cn.macswelle.campusecurity.devicemanage.repositories.LocationRepository;
import cn.macswelle.campusecurity.feignapi.adapter.AdapterApi;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceManageServiceImpl implements DeviceManageService {

    @Autowired
    private DiscoveryClient discoveryClient;

    private AdapterApi adapterApi;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public ArrayList<DeviceDto> refreshServices() {
        ArrayList<DeviceDto> deviceDtos = new ArrayList<>();
        //清空Device数据库
        deviceRepository.deleteAll();
        //遍历所有服务的实例
        discoveryClient.getServices().forEach(id -> {
            if (discoveryClient.getInstances(id).size() > 1)
                //物联网服务只能有一个实例，否则不被设备管理服务管理
                discoveryClient.getInstances(id).forEach(serviceInstance -> {
                    // 获取服务信息，重要！feign接口指定url的方法
                    String infoUrl = serviceInstance.getUri().toString();
                    adapterApi = Feign.builder().target(AdapterApi.class, infoUrl);
                    if (adapterApi != null) {
                        deviceDtos.add(adapterApi.getInfo());
                        register(adapterApi.getInfo());
                    } else {
                        try {
                            throw new DeviceNotRespondException();
                        } catch (DeviceNotRespondException e) {
                            e.printStackTrace();
                        }
                    }
                });
        });
        return deviceDtos;
    }

    @Override
    public void register(DeviceDto deviceDto) {
        Device device = new Device();
        device.setDescription(deviceDto.getDescription());
        device.setId(deviceDto.getId());
        device.setName(deviceDto.getName());
        Location location = null;
        try {
            location = locationRepository.findById(deviceDto.getLocation()).orElseThrow(() ->
                    new EntityNotFoundException(deviceDto.getLocation(), Location.class));
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        device.setLocation(location);
        deviceRepository.save(device);
    }

    @Override
    public List<DeviceDto> getSons(String id) {
        String infoUrl = discoveryClient.getInstances(id).get(0).getUri().toString();
        adapterApi = Feign.builder().target(AdapterApi.class, infoUrl);
        if (adapterApi != null) {
            return adapterApi.getDevice();
        }else {
            try {
                throw new DeviceNotRespondException();
            } catch (DeviceNotRespondException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void cancel(String id) {
        deviceRepository.deleteById(id);
    }

    @Override
    public String refractorOrAddLocation(LocationDto2 locationDto) {
        Location result = null;
        if (locationDto.getId() == null) {
            Location location = new Location();
            location.setDescription(locationDto.getDescription());
            location.setName(locationDto.getName());
            result = locationRepository.save(location);
        } else {
            try {
                result = locationRepository.findById(locationDto.getId()).orElseThrow(() ->
                        new EntityNotFoundException(locationDto.getId(), Location.class));
                result.setName(locationDto.getName());
                result.setDescription(locationDto.getDescription());
                locationRepository.save(result);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result == null ? null : result.getId();
    }


    @Override
    public List<LocationDto2> getLocations() {
        List<LocationDto2> list = new ArrayList<>();
        locationRepository.findAll().forEach(location -> {
            LocationDto2 locationDto2 = new LocationDto2();
            locationDto2.setId(location.getId());
            locationDto2.setDescription(location.getDescription());
            locationDto2.setName(location.getName());
            list.add(locationDto2);
        });
        return list;
    }
}
