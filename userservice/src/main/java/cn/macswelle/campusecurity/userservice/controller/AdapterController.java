package cn.macswelle.campusecurity.userservice.controller;

import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.feignapi.adapter.AdapterApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdapterController implements AdapterApi {

    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public DeviceDto getInfo() {
        return null;
    }
}
