package cn.macswelle.campusecurity.data_process.service;

import cn.macswelle.campusecurity.common.dto.responseDto.FaceDto;
import cn.macswelle.campusecurity.database.entities.Record;
import com.baidu.aip.face.AipFace;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "faceDto", autoDelete = "true")
  , exchange = @Exchange(value = "topic", type = ExchangeTypes.TOPIC), key = "*.face"))
public class FaceReceiver extends AmqpReceiver{

  @Autowired
  private String groupIdList;

  @Autowired
  private AipFace client;

//  public void process(String message) {
//    JSONObject mess = new JSONObject(message);
//    MessageDto messageDto = mapper.readValue(message, MessageDto.class);
//    logger.info(mess.get("message") + "");
  //对象序列化与反序列化的过程中，泛型信息是否丢失？
//    Type type = ((ParameterizedType) messageDto.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//    logger.info(type.getTypeName());
//    if (mess.getJSONObject("message").get("action").equals("register")) register(mess);
//    recognize(mess);

//    BaiduApi baiduApi = Feign.builder()
//      .encoder(new SpringFormEncoder(new SpringEncoder(messageConverters)))
//      .decoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)))
//      .target(BaiduApi.class, "https://aip.baidubce.com/rest/2.0/face/v3/search?access_token=" + token);
//    logger.info("调用识别接口 token=" + token);
//    cn.macswelle.campusecurity.common.dto.responseDto.FaceDto result =
//      baiduApi.faceReco(messageDto.getObject());
//    logger.info(result.toString());
//    if (result.getFace_token() != null && result.getUser_list() != null)
//      toDb(result, messageDto.getMessage(), toFile(messageDto.getObject().getImage()));
//  }

  @RabbitHandler
  private void recognize(String message) throws IOException {
//    JSONObject faceDto = messageDto.getJSONObject("object");
    cn.macswelle.campusecurity.common.dto.requestDto.FaceDto faceDto =
      objectMapper.readValue(message, cn.macswelle.campusecurity.common.dto.requestDto.FaceDto.class);

    HashMap<String, String> options = new HashMap<>();
    options.put("max_face_num", "1");
    options.put("match_threshold", "70");
    options.put("quality_control", "NORMAL");
    options.put("liveness_control", "LOW");
    //logger.info(faceDto.getString("image"));
    //logger.info(faceDto.getString("image_type"));
    JSONObject jsonObject = client.search(faceDto.getImage(), faceDto.getImage_type(), groupIdList, options);
    logger.info(jsonObject.toString());
    FaceDto response = new FaceDto();
    JSONObject result;
    try {
      result = jsonObject.getJSONObject("result");
      response.setFace_token(result.getString("face_token"));
      JSONArray array = result.getJSONArray("user_list");
      if (array.length() != 0) {
        response.setGroup_id((String) array.getJSONObject(0).get("group_id"));
        response.setUser_id((String) array.getJSONObject(0).get("user_id"));
        response.setUser_info((String) array.getJSONObject(0).get("user_info"));
        response.setScore((Double) array.getJSONObject(0).get("score"));
        logger.info("recognize: " + response);
        record(response, faceDto.getTime(), faceDto.getLocation(), toFile(faceDto.getImage()));
      } else {
        logger.info("recognize:没有人");
      }
    } catch (JSONException e) {
      logger.info("recognize:" + jsonObject.get("error_msg"));
    }

    //{"result":null,"log_id":3579101992018,"error_msg":"image check fail","cached":0,"error_code":222203,"timestamp":1619592752}
    //{"result":{"face_token":"40a833f88cec208b4d48884f1f29f6da","user_list":[
    // {"score":98.68994140625,"group_id":"ghc","user_id":"gehanchen","user_info":""}]},
    // "log_id":2018435254535,"error_msg":"SUCCESS","cached":0,"error_code":0,"timestamp":1619775608}
  }


  /**
   * 将FaceDto转换为record，存入数据库，图片存入文件系统
   */
  public void record(FaceDto faceDto, Long time, String location, String fileUrl) {
    Record record = new Record();
    record.setCreateTime(time);
    record.setLocation(locationRepository.findById(location).orElse(null));
    record.setEvent("人脸识别 " + faceDto);
    record.setFile(fileUrl);
    record.setPersonnel(personnelRepository.findById(faceDto.getUser_id()).orElse(null));
    String id = recordRepository.save(record).getId();
    logger.info("recognize: saved id:" + id);
//    faceDto.getUser_list().forEach(user -> {
//      record.setPersonnel(personnelRepository.findById(user.getUser_id()).orElse(null));
//      recordRepository.save(record);
//    });
  }

  /**
   * 将image64格式的图片存入文件系统
   */
  public String toFile(String imageBase64) {
    return null;
  }
}
