package cn.macswelle.campusecurity.data_process.service;

import cn.macswelle.campusecurity.common.dto.requestDto.FaceRegDto;
import cn.macswelle.campusecurity.database.entities.Personnel;
import com.baidu.aip.face.AipFace;
import org.json.JSONObject;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "faceDto", autoDelete = "true")
  , exchange = @Exchange(value = "topic", type = ExchangeTypes.TOPIC), key = "*.faceReg"))
public class FaceRegReceiver extends AmqpReceiver{

  @Autowired
  private AipFace client;
  /**
   * 放弃messageDto的反序列化，直接使用json操作
   */
  @RabbitHandler
  private void register(String message) throws IOException {
    FaceRegDto faceRegDto = objectMapper.readValue(message,FaceRegDto.class);
//    JSONObject faceRegDto = messageDto.getJSONObject("object");
    Personnel personnel = new Personnel();
    personnel.setCategory(1);
    personnel.setDescription(faceRegDto.getUser_info());
    personnel.setName(faceRegDto.getName());
    String id = personnelRepository.save(personnel).getId();
    logger.info("register saved:" + id);
    HashMap<String, String> options = new HashMap<>();
    options.put("user_info", faceRegDto.getUser_info());
    options.put("quality_control", faceRegDto.getQuality_control());
    options.put("liveness_control", faceRegDto.getLiveness_control());
    options.put("action_type", faceRegDto.getAction_type());
    logger.info("registered:" + faceRegDto);
    JSONObject object = client.addUser(faceRegDto.getImage(), faceRegDto.getImage_type(),
      faceRegDto.getGroup_id(), id, options);
    logger.info("registered:" + object.toString());
  }
}
