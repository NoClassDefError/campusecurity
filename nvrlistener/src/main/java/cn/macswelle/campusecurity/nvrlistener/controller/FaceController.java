package cn.macswelle.campusecurity.nvrlistener.controller;

import cn.macswelle.campusecurity.common.dto.MessageDto;
import cn.macswelle.campusecurity.common.dto.requestDto.FaceDto;
import cn.macswelle.campusecurity.common.dto.responseDto.FaceRegDto;
import cn.macswelle.campusecurity.feignapi.deviceManager.BaiduApi;
import cn.macswelle.campusecurity.nvrlistener.DeviceInfo;
import feign.Feign;
import feign.form.spring.SpringFormEncoder;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FaceController {

  @Autowired
  private ObjectFactory<HttpMessageConverters> messageConverters;

  @Autowired
  private String token;

  @Autowired
  private AmqpTemplate rabbitAmqoTemplate;

  @PostMapping("/storeFace")
  @ResponseBody
  public FaceRegDto faceRegister(@RequestBody cn.macswelle.campusecurity.common.dto.requestDto.FaceRegDto faceRegDto) {
    BaiduApi baiduApi = Feign.builder()
      .encoder(new SpringFormEncoder(new SpringEncoder(messageConverters)))
      .decoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)))
      .target(BaiduApi.class, "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add?access_token=" + token);
    if (baiduApi != null) return baiduApi.faceRegister(faceRegDto);
    else return null;
  }

  @PostMapping("/recognize")
  @ResponseBody
  public void faceReco(@RequestBody FaceDto faceDto) {
//    BaiduApi baiduApi = Feign.builder()
//      .encoder(new SpringFormEncoder(new SpringEncoder(messageConverters)))
//      .decoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)))
//      .target(BaiduApi.class, "https://aip.baidubce.com/rest/2.0/face/v3/search?access_token=" + token);
//    LoggerFactory.getLogger(this.getClass()).info("调用识别接口 token=" + token);
//    return baiduApi.faceReco(faceDto);
    MessageDto<FaceDto> message = new MessageDto<>();
    Map<String, String> map = new HashMap<>();
    map.put("token", token);
    map.put("location", DeviceInfo.location);
    map.put("time", System.currentTimeMillis() + "");

    message.setObject(faceDto);
    message.setMessage(map);
    rabbitAmqoTemplate.convertAndSend("log.direct", "log.info.routing.key", message);
  }

}
