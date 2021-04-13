package cn.macswelle.campusecurity.nvrlistener.controller;

import cn.macswelle.campusecurity.nvrlistener.service.BaiduAIFace;
import cn.macswelle.campusecurity.nvrlistener.service.Setingmodel;
import lombok.Data;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@Data
@RestController
@ConfigurationProperties(prefix = "campusecurity.rtmp")
public class CameraController {

  private String url;

  private String rate;

  @RequestMapping(value = "/storeFace", method = RequestMethod.POST)
  public Map<String, Object> storeFace(String image, String name, String groupId) throws IOException {
    Setingmodel setingmodel = new Setingmodel();
    setingmodel.setImgpath(image);
    setingmodel.setUserID(name);
    setingmodel.setGroupID(groupId);
    return faceapi.FaceRegistration(setingmodel);
  }

  /**
   * 开启监控
   * 实现ffmpeg的RTMP推流（up streaming/push/publish），也就是将视频数据传送到rtmp流服务器。
   */
  @RequestMapping(value = "/startUpStreaming", method = RequestMethod.POST)
  public String startUpStream(Integer num) {
//    System.out.println("  " + url + " " + rate);
    OpenCVFrameGrabber grabber = CameraController.getCamera(num);
    try {
      OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
      final Frame[] frame = {grabber.grab()};
      final opencv_core.IplImage[] image = {converter.convert(frame[0])};//将一帧转为图像，可以进行人脸识别、加水印等处理
      int width = image[0].width();
      int height = image[0].height();
      FrameRecorder recorder = FrameRecorder.createDefault(url, width, height);
      recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
      recorder.setFormat("flv");
      recorder.setFrameRate(Double.parseDouble(rate));
      recorder.start();
//      CanvasFrame canvasFrame = new CanvasFrame("camera");
//      canvasFrame.setVisible(true);
//      canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      final long[] startTime = {0};
      Runnable camera = () -> {
        int a = 0;
        try {
          while ((frame[0] = grabber.grab()) != null) {
//        System.out.println("推流..." + url);
//            canvasFrame.showImage(frame[0]);
            image[0] = converter.convert(frame[0]);
            if (a % 500 == 0) {
              a = 0;
              //每隔200帧进行一次识别
              String buffers = image[0].imageData().getString();
              Map<String, Object> searchface = searchFace(buffers);
              if (searchface == null) System.out.println("没有人");
              else if (searchface.get("user_id") == null) System.out.println("陌生人");

            }
            //上传帧至recorder，grabber.grab()不能直接上传，要经过两次转换，即转成image再转回来
            Frame rotatedFrame = converter.convert(image[0]);
            if (startTime[0] == 0) startTime[0] = System.currentTimeMillis();
//        recorder.setTimestamp(1000 * (System.currentTimeMillis() - startTime));//时间戳
            if (rotatedFrame != null) recorder.record(rotatedFrame);
            a++;
//            Thread.sleep(40);
          }
        } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
          e.printStackTrace();
        }
      };
      new Thread(camera).start();
    } catch (IOException e) {
      e.printStackTrace();
      return "error";
    }
    return "success";
  }

  public static OpenCVFrameGrabber getCamera(int num) {
    System.out.println(num);
    OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(num);
    try {
      grabber.start();   //开始获取摄像头数据
    } catch (FrameGrabber.Exception e) {
      e.printStackTrace();
    }
    return grabber;
  }

  @Autowired
  private BaiduAIFace faceapi;

  private Map<String, Object> searchFace(String imagebase64) {
//    String substring = imagebase64.substring(imagebase64.indexOf(",") + 1, imagebase64.length());
    Setingmodel setingmodel = new Setingmodel();
    setingmodel.setImgpath(imagebase64);
    setingmodel.setGroupID("StRoot");
//    System.out.println(imagebase64);
    return faceapi.FaceSearch(setingmodel);
  }
}
