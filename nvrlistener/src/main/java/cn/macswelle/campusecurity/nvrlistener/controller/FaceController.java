package cn.macswelle.campusecurity.nvrlistener.controller;

import cn.macswelle.campusecurity.common.dto.MessageDto;
import cn.macswelle.campusecurity.common.dto.requestDto.FaceDto;
import cn.macswelle.campusecurity.common.dto.requestDto.FaceRegDto;
import cn.macswelle.campusecurity.nvrlistener.DeviceInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FaceController {

  @Autowired
  private AmqpTemplate rabbitAmqoTemplate;

  @Autowired
  private DeviceInfo deviceInfo;

  @Autowired
  private ObjectMapper mapper;

  @PostMapping("/storeFace")
  @ResponseBody
  public void faceRegister(@RequestBody cn.macswelle.campusecurity.common.dto.requestDto.FaceRegDto faceRegDto) throws JsonProcessingException {
    MessageDto<FaceRegDto> message = new MessageDto<>();
    Map<String, String> map = new HashMap<>();
    map.put("location", deviceInfo.getLocation());
    map.put("time", System.currentTimeMillis() + "");
    map.put("action", "register");
    message.setObject(faceRegDto);
    message.setMessage(map);
    rabbitAmqoTemplate.convertAndSend("faceDto", mapper.writeValueAsString(message));
  }

  @PostMapping("/recognize")
  @ResponseBody
  public void faceReco(@RequestBody FaceDto faceDto) {
    MessageDto<FaceDto> message = new MessageDto<>();
    Map<String, String> map = new HashMap<>();
    map.put("location", deviceInfo.getLocation());
    map.put("time", System.currentTimeMillis() + "");
    map.put("action", "recognize");
    message.setObject(faceDto);
    message.setMessage(map);
    try {
      rabbitAmqoTemplate.convertAndSend("faceDto", mapper.writeValueAsString(message));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
