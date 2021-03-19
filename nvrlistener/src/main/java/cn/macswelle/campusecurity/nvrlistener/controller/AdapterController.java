package cn.macswelle.campusecurity.nvrlistener.controller;

import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.feignapi.adapter.AdapterApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

/**
 * 这个微服务与设备管理服务之间的通信是不对称的，
 * 设备管理服务要访问本服务，需要从服务注册中心获取本服务的信息。
 * 本服务访问设备管理服务要通过网关
 */
@RestController
public class AdapterController implements AdapterApi {

    @RequestMapping(value = "/getDevice", method = RequestMethod.POST)
    @Override
    public List<DeviceDto> getDevice() {
        return null;
    }

    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    @Override
    public DeviceDto getInfo() {
        ObjectMapper mapper = new ObjectMapper();
        DeviceDto deviceDto = null;
        try {
            //info.json中储存持久化的DeviceDto类对象，此模块不连接数据库，使用文件持久化对象
            deviceDto = mapper.readValue(
                    new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("info.json")).toURI()),
                    DeviceDto.class);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("错误，信息文件缺失！");
        }
        return deviceDto;
    }
}