package cn.macswelle.campusecurity.nvrlistener.controller;

import lombok.Data;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.io.IOException;

@Data
@RestController
@ConfigurationProperties(prefix = "campusecurity.rtmp")
public class CameraController {

  @Autowired
  private OpenCVFrameGrabber grabber;

  private String url;

  private String rate;

  /**
   * 开启监控
   * 实现ffmpeg的RTMP推流（up streaming/push/publish），也就是将视频数据传送到rtmp流服务器。
   */
  @RequestMapping(value = "/startUpStreaming", method = RequestMethod.POST)
  public String startUpStream() {
//    System.out.println("  " + url + " " + rate);
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
      CanvasFrame canvasFrame = new CanvasFrame("camera");
      canvasFrame.setVisible(true);
      canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      final long[] startTime = {0};
      Runnable camera = () -> {
        try {
          while ((frame[0] = grabber.grab()) != null) {
//        System.out.println("推流..." + url);
            canvasFrame.showImage(frame[0]);
            image[0] = converter.convert(frame[0]);
            Frame rotatedFrame = converter.convert(image[0]);
            if (startTime[0] == 0) startTime[0] = System.currentTimeMillis();
//        recorder.setTimestamp(1000 * (System.currentTimeMillis() - startTime));//时间戳
            if (rotatedFrame != null) recorder.record(rotatedFrame);
            Thread.sleep(40);
          }
        } catch (FrameGrabber.Exception | InterruptedException | FrameRecorder.Exception e) {
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
}
