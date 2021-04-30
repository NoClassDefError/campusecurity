package cn.macswelle.campusecurity.facial_recognision.service;

import cn.macswelle.campusecurity.common.dto.responseDto.FaceDto;
import cn.macswelle.campusecurity.database.entities.Personnel;
import cn.macswelle.campusecurity.database.entities.Record;
import cn.macswelle.campusecurity.database.repositories.LocationRepository;
import cn.macswelle.campusecurity.database.repositories.PersonnelRepository;
import cn.macswelle.campusecurity.database.repositories.RecordRepository;
import com.baidu.aip.face.AipFace;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RabbitListener(queues = "faceDto")
public class Receiver {

  @Autowired
  private String groupIdList;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private PersonnelRepository personnelRepository;

  @Autowired
  private RecordRepository recordRepository;

  @Autowired
  private AipFace client;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @RabbitHandler
  public void process(String message) {
    JSONObject mess = new JSONObject(message);
//    MessageDto messageDto = mapper.readValue(message, MessageDto.class);
    logger.info(mess.get("message") + "");
    //对象序列化与反序列化的过程中，泛型信息是否丢失？
//    Type type = ((ParameterizedType) messageDto.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//    logger.info(type.getTypeName());
    if (mess.getJSONObject("message").get("action").equals("register")) register(mess);
    else recognize(mess);

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
  }

  /**
   * 放弃messageDto的反序列化，直接使用json操作
   */
  private void register(JSONObject messageDto) {
//    FaceRegDto faceRegDto = messageDto.getObject();
    JSONObject faceRegDto = messageDto.getJSONObject("object");
    Personnel personnel = new Personnel();
    personnel.setCategory(1);
    personnel.setDescription(faceRegDto.getString("user_info"));
    personnel.setName(faceRegDto.getString("name"));
    String id = personnelRepository.save(personnel).getId();
    logger.info("register saved:" + id);
    HashMap<String, String> options = new HashMap<>();
    options.put("user_info", faceRegDto.getString("user_info"));
    options.put("quality_control", faceRegDto.getString("quality_control"));
    options.put("liveness_control", faceRegDto.getString("liveness_control"));
    options.put("action_type", faceRegDto.getString("action_type"));
    logger.info("registered:" + faceRegDto);
    JSONObject object = client.addUser(faceRegDto.getString("image"), faceRegDto.getString("image_type"),
      faceRegDto.getString("group_id"), id, options);
    logger.info("registered:" + object.toString());
  }

  private void recognize(JSONObject messageDto) {
    JSONObject faceDto = messageDto.getJSONObject("object");
    HashMap<String, String> options = new HashMap<>();
    options.put("max_face_num", "1");
    options.put("match_threshold", "70");
    options.put("quality_control", "NORMAL");
    options.put("liveness_control", "LOW");
    logger.info(faceDto.getString("image"));
    logger.info(faceDto.getString("image_type"));
    JSONObject jsonObject = client.search(faceDto.getString("image"), faceDto.getString("image_type"), groupIdList, options);
    logger.info(jsonObject.toString());
    FaceDto response = new FaceDto();
    JSONObject result = jsonObject.getJSONObject("result");
    if (result == null) {
      //{"result":null,"log_id":3579101992018,"error_msg":"image check fail","cached":0,"error_code":222203,"timestamp":1619592752}
      //{"result":{"face_token":"40a833f88cec208b4d48884f1f29f6da","user_list":[
      // {"score":98.68994140625,"group_id":"ghc","user_id":"gehanchen","user_info":""}]},
      // "log_id":2018435254535,"error_msg":"SUCCESS","cached":0,"error_code":0,"timestamp":1619775608}
      logger.info("recognize:" + jsonObject.get("error_msg"));
    } else {
      response.setFace_token(result.getString("face_token"));
      JSONArray array = result.getJSONArray("user_list");
      if (array.length() != 0) {
        response.setGroup_id((String) array.getJSONObject(0).get("group_id"));
        response.setUser_id((String) array.getJSONObject(0).get("user_id"));
        response.setUser_info((String) array.getJSONObject(0).get("user_info"));
        response.setScore((String) array.getJSONObject(0).get("score"));
        logger.info("recognize: " + response);
        record(response, messageDto.getJSONObject("message"), toFile(faceDto.getString("image")));
      } else {
        logger.info("recognize:没有人");
      }
    }
  }

  /**
   * 将FaceDto转换为record，存入数据库，图片存入文件系统
   */
  public void record(FaceDto faceDto, JSONObject message, String fileUrl) {
    Record record = new Record();
    record.setCreateTime(Long.parseLong(message.getString("time")));
    record.setLocation(locationRepository.findById(message.getString("location")).orElse(null));
    record.setEvent("人脸识别 " + faceDto + message);
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
