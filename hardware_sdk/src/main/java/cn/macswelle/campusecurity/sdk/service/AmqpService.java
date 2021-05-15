package cn.macswelle.campusecurity.sdk.service;

import cn.macswelle.campusecurity.common.dto.requestDto.FaceDto;
import cn.macswelle.campusecurity.common.dto.requestDto.FaceRegDto;
import cn.macswelle.campusecurity.common.dto.requestDto.RecordDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 上传数据专用，查询信息或其他操作请使用FeignApi
 */
@Service
public class AmqpService {
  @Autowired
  private AmqpTemplate rabbitAmqpTemplate;
  @Autowired
  private ObjectMapper mapper;

  /**
   * 人脸识别请求
   *
   * @param isAlarm 是否需要报警
   * @param faceDto 人脸识别对象
   */
  public void sendFace(Boolean isAlarm, FaceDto faceDto) {
    try {
      if (isAlarm) rabbitAmqpTemplate.convertAndSend("topic", "alarm.faceDto", mapper.writeValueAsString(faceDto));
      else rabbitAmqpTemplate.convertAndSend("topic", "log.faceDto", mapper.writeValueAsString(faceDto));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  /**
   * 人脸注册
   *
   * @param isAlarm 是否需要报警
   * @param faceRegDto 人脸注册对象
   */
  public void regFace(Boolean isAlarm, FaceRegDto faceRegDto) {
    try {
      if (isAlarm) rabbitAmqpTemplate.convertAndSend("topic", "alarm.faceReg", mapper.writeValueAsString(faceRegDto));
      else rabbitAmqpTemplate.convertAndSend("topic", "log.faceReg", mapper.writeValueAsString(faceRegDto));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  /**
   * 上传日志记录，和报警
   *
   * @param isAlarm   是否需要报警
   * @param recordDto 记录
   */
  public void sendRecord(Boolean isAlarm, RecordDto recordDto) {
    try {
      if (isAlarm) rabbitAmqpTemplate.convertAndSend("topic", "alarm.record", mapper.writeValueAsString(recordDto));
      else rabbitAmqpTemplate.convertAndSend("topic", "log.record", mapper.writeValueAsString(recordDto));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
