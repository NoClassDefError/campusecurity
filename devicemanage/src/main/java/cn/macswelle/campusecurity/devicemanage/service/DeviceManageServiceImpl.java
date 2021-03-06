package cn.macswelle.campusecurity.devicemanage.service;

import cn.macswelle.campusecurity.common.DeviceNotRespondException;
import cn.macswelle.campusecurity.common.EntityNotFoundException;
import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.common.dto.LocationDto2;
import cn.macswelle.campusecurity.common.dto.requestDto.RecordDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.common.dto.responseDto.RecordDto2;
import cn.macswelle.campusecurity.database.entities.*;
import cn.macswelle.campusecurity.database.repositories.*;
import cn.macswelle.campusecurity.feignapi.adapter.AdapterApi;
import feign.Feign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
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

  @Autowired
  private PersonnelRepository personnelRepository;

  @Autowired
  private RecordRepository recordRepository;

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
          String infoUrl = getUrl(id);
          adapterApi = Feign.builder().target(AdapterApi.class, infoUrl);
          if (adapterApi != null) {
            DeviceDto deviceDto = adapterApi.getInfo();
            deviceDto.setUrl(infoUrl);
            deviceDtos.add(deviceDto);
            register(deviceDto);
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
  public HttpResult register(DeviceDto deviceDto) {
    Device device = new Device();
    device.setDescription(deviceDto.getDescription());
    device.setId(deviceDto.getId());
    device.setName(deviceDto.getName());
    device.setCategory(deviceDto.getCategory());
    device.setVersion(deviceDto.getVersion());
    device.setUrl(deviceDto.getUrl());
    Logger logger = LoggerFactory.getLogger(this.getClass());
    logger.info(deviceDto.toString());

//    if (deviceDto.getUrl() == null)
//      deviceDto.setUrl(getUrl(deviceDto.getId()));
    Location location;
    try {
      location = locationRepository.findById(deviceDto.getLocation()).orElseThrow(() ->
        new EntityNotFoundException(deviceDto.getLocation(), Location.class));
    } catch (Exception e) {
      logger.info("Can not find location id=" + deviceDto.getLocation());
      return new HttpResult("status:error");
    }
    device.setLocation(location);
    deviceRepository.save(device);
    return new HttpResult("status:success");
  }


  @Override
  public void cancel(String id) {
    deviceRepository.deleteById(id);
  }

  @Override
  public void addRecord(RecordDto dto) {
    Record record = new Record();
    record.setCreateTime(dto.getCreateTime());
    record.setEvent(dto.getEvent());
    record.setFile(dto.getFile());
    if (dto.getPersonnel() != null)
      record.setPersonnel(personnelRepository.findById(dto.getPersonnel()).orElse(null));
    try {
      Location l = locationRepository.findById(dto.getLocation()).orElseThrow(() ->
        new EntityNotFoundException(dto.getLocation(), Location.class));
      record.setLocation(l);
    } catch (EntityNotFoundException e) {
      e.printStackTrace();
    }
    recordRepository.save(record);
  }

  @Override
  public HttpResult refractorOrAddLocation(LocationDto2 locationDto) {
    Location result;
    if (locationDto.getId() == null) {
      Location location = new Location();
      location.setDescription(locationDto.getDescription());
      location.setName(locationDto.getName());
      result = locationRepository.save(location);
      return new HttpResult("status:add", "id:" + result.getId());
    } else {
      try {
        result = locationRepository.findById(locationDto.getId()).orElseThrow(() ->
          new EntityNotFoundException(locationDto.getId(), Location.class));
        result.setName(locationDto.getName());
        result.setDescription(locationDto.getDescription());
        locationRepository.save(result);
        return new HttpResult("status:modify", "id:" + locationDto.getId());
      } catch (EntityNotFoundException e) {
        e.printStackTrace();
        return new HttpResult("status:error");
      }
    }
  }

  @Override
  public List<LocationDto2> getLocations() {
    List<LocationDto2> list = new ArrayList<>();
    locationRepository.findAll().forEach(location -> {
      LocationDto2 locationDto2 = new LocationDto2();
      locationDto2.setId(location.getId());
      locationDto2.setDescription(location.getDescription());
      locationDto2.setName(location.getName());
      locationDto2.setDeviceNum(deviceRepository.findNumByLocation(location.getId()));
      locationDto2.setRecord(recordRepository.countByLocation(location.getId()));
      list.add(locationDto2);
    });
    return list;
  }

  @Override
  public List<DeviceDto> getByLocation(String id) {
    List<DeviceDto> list = new ArrayList<>();
    deviceRepository.findByLocation(id).forEach(device -> {
      DeviceDto deviceDto = new DeviceDto();
      deviceDto.setCategory(device.getCategory());
      deviceDto.setDescription(device.getDescription());
      deviceDto.setId(device.getId());
      deviceDto.setLocation(id);
      deviceDto.setName(device.getName());
      deviceDto.setVersion(device.getVersion());
      deviceDto.setUrl(device.getUrl());
      list.add(deviceDto);
    });
    return list;
  }

  @Override
  public List<RecordDto2> getRecords(String locationId, Long start, Long end) {
    List<RecordDto2> list = new ArrayList<>();
    recordRepository.findByLocation(locationId, start, end).forEach(record -> {
      RecordDto2 recordDto = new RecordDto2();
      recordDto.setCreateTime(record.getCreateTime());
      recordDto.setEvent(record.getEvent());
      recordDto.setId(record.getId());
      recordDto.setFile(record.getFile());
      Location l = record.getLocation();
      recordDto.setLocationId(l.getId());
      recordDto.setLocationName(l.getName());
      Personnel p = record.getPersonnel();
      if (p != null) {
        recordDto.setPersonnelId(p.getId());
        recordDto.setPersonnelName(p.getName());
      }
      list.add(recordDto);
    });
    return list;
  }

  /**
   * id 为服务注册中心给的id
   * 由于数据库里没有url字段，所以使用id从注册中心查询
   */
  private String getUrl(String id) {
    try {
      return discoveryClient.getInstances(id).get(0).getUri().toURL().toString();
    } catch (IndexOutOfBoundsException | MalformedURLException e) {
      e.printStackTrace();
    }
    return "not found";
  }
}
