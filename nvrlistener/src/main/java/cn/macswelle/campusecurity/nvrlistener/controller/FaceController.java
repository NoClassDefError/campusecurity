package cn.macswelle.campusecurity.nvrlistener.controller;

import cn.macswelle.campusecurity.common.dto.requestDto.FaceRegDto;
import cn.macswelle.campusecurity.sdk.service.AmqpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaceController {

  @Autowired
  private AmqpService amqpService;

  @PostMapping("/storeFace")
  @ResponseBody
  public void faceRegister(@RequestBody FaceRegDto faceRegDto) {
    amqpService.regFace(false, faceRegDto);
  }
}
