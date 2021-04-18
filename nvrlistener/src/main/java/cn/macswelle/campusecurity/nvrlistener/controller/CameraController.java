package cn.macswelle.campusecurity.nvrlistener.controller;

import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.nvrlistener.service.BaiduAIFace;
import cn.macswelle.campusecurity.nvrlistener.service.Setingmodel;
import cn.macswelle.campusecurity.nvrlistener.service.VirtualCameraService;
import lombok.Data;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RestController
@ConfigurationProperties(prefix = "campusecurity.rtmp")
public class CameraController {

  private List<String> url;//推流地址

//  private List<String> http;//拉流地址

  private String rate;

  private static Map<Integer, VirtualCameraService> cameras = new HashMap<>();

  @Autowired
  private BaiduAIFace faceapi;

  @RequestMapping(value = "/storeFace", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> storeFace(String image, String name, String groupId) throws IOException {
    Setingmodel setingmodel = new Setingmodel();
    setingmodel.setImgpath(image);
    setingmodel.setUserID(name);
    setingmodel.setGroupID(groupId);
    return faceapi.FaceRegistration(setingmodel);
  }

  @RequestMapping(value = "/stopStreaming", method = RequestMethod.POST)
  @ResponseBody
  public HttpResult stopStream() {
    LoggerFactory.getLogger(this.getClass()).info("当前cameras状态：" + cameras);
    try {
      for (VirtualCameraService service : cameras.values()) service.stop();
    } catch (IndexOutOfBoundsException e) {
      return new HttpResult("status:error", "info:没有该摄像头或其已经关闭");
    } catch (FrameGrabber.Exception | FrameRecorder.Exception | InterruptedException e) {
      return new HttpResult("status:error", "info:没有关成");
    }
    return new HttpResult("status:success");
  }

  /**
   * 开启监控，所有通道
   * 实现ffmpeg的RTMP推流（up streaming/push/publish），也就是将视频数据传送到rtmp流服务器。
   */
  @RequestMapping(value = "/startUpStreaming", method = RequestMethod.POST)
  @ResponseBody
  public HttpResult startUpStream(int i) {
//    int num = videoInputLib.videoInput.listDevices();
//    System.out.println("  " + url.get(0) + " " + http.get(0));
    LoggerFactory.getLogger(this.getClass()).info("正在开启:cam" + i + " 当前cameras状态：" + cameras);
//    if (num > url.size()) num = url.size();
    try {
//      for (int i = 0; i < num; i++) {

      if (cameras.size() <= i) {
        VirtualCameraService service = new VirtualCameraService(i, url.get(i), rate, faceapi);
        new Thread(service).start();
        cameras.put(i, service);
      } else cameras.get(i).restart(url.get(i), rate);
//      }
    } catch (IOException e) {
      e.printStackTrace();
      return new HttpResult("status:error", "info:视频流服务器没有启动");
    }
    return new HttpResult("status:success", "info:" + cameras);
  }
}
