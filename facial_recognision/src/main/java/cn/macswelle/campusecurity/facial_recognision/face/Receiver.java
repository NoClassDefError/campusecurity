package cn.macswelle.campusecurity.facial_recognision.face;

import cn.macswelle.campusecurity.common.dto.MessageDto;
import cn.macswelle.campusecurity.common.dto.requestDto.FaceDto;
import cn.macswelle.campusecurity.database.entities.Record;
import cn.macswelle.campusecurity.database.repositories.LocationRepository;
import cn.macswelle.campusecurity.database.repositories.PersonnelRepository;
import cn.macswelle.campusecurity.database.repositories.RecordRepository;
import cn.macswelle.campusecurity.feignapi.deviceManager.BaiduApi;
import feign.Feign;
import feign.form.spring.SpringFormEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(
  bindings = @QueueBinding(
    value = @Queue(value = "log.info", autoDelete = "false"),
    exchange = @Exchange(value = "log.direct"),
    key = "log.info.routing.key"
  )
)
public class Receiver {
  @Autowired
  private ObjectFactory<HttpMessageConverters> messageConverters;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private PersonnelRepository personnelRepository;

  @Autowired
  private RecordRepository recordRepository;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @RabbitHandler
  public void process(MessageDto<FaceDto> messageDto) {
    logger.info(messageDto.toString());
    String token = messageDto.getMessage().get("token");
    BaiduApi baiduApi = Feign.builder()
      .encoder(new SpringFormEncoder(new SpringEncoder(messageConverters)))
      .decoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)))
      .target(BaiduApi.class, "https://aip.baidubce.com/rest/2.0/face/v3/search?access_token=" + token);
    logger.info("调用识别接口 token=" + token);
    cn.macswelle.campusecurity.common.dto.responseDto.FaceDto result =
      baiduApi.faceReco(messageDto.getObject());
    logger.info(result.toString());
    if (result.getFace_token() != null && result.getUser_list() != null)
      toDb(result, messageDto.getMessage(), toFile(messageDto.getObject().getImage()));
  }

  /**
   * 将FaceDto转换为record，存入数据库，图片存入文件系统
   */
  public void toDb(cn.macswelle.campusecurity.common.dto.responseDto.FaceDto faceDto,
                   Map<String, String> message, String fileUrl) {
    Record record = new Record();
    record.setCreateTime(Long.parseLong(message.get("time")));
    record.setLocation(locationRepository.findById(message.get("location")).orElse(null));
    record.setEvent("人脸识别 " + faceDto + message);
    record.setFile(fileUrl);
    faceDto.getUser_list().forEach(user -> {
      record.setPersonnel(personnelRepository.findById(user.getUser_id()).orElse(null));
      recordRepository.save(record);
    });
  }

  /**
   * 将image64格式的图片存入文件系统
   */
  public String toFile(String imageBase64) {
    return null;
  }
}
