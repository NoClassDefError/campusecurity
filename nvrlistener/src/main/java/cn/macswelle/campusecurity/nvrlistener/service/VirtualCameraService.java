package cn.macswelle.campusecurity.nvrlistener.service;

import cn.macswelle.campusecurity.common.dto.requestDto.FaceDto;
import cn.macswelle.campusecurity.nvrlistener.controller.FaceController;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 视频的处理与推流服务，作为生命周期独立的线程。
 * 交给spring管理，但启动这个线程之前一定要先init()或者执行其有参构造器
 * 注意是多例模式，即任何一个实例都是新的实例，不能直接注入，要用beanFactory
 *
 * @author Ge Hanchen
 */
@Scope("prototype")
@Service
public class VirtualCameraService implements Runnable {

  private OpenCVFrameGrabber grabber = null;
  private boolean run = true;
  private FrameRecorder recorder;
  private boolean init = false;

  private CanvasFrame canvasFrame;
  private Frame frame;
  private opencv_core.IplImage image;
  private OpenCVFrameConverter.ToIplImage converter = null;
  //  private long startTime = 0;
  private String info = null;
  private final Logger logger = LoggerFactory.getLogger(VirtualCameraService.class);

  /**
   * 调用此方法之后一定要init()
   */
  public VirtualCameraService() {

  }

  public VirtualCameraService(int num, String url, String rate)
    throws FrameGrabber.Exception, FrameRecorder.Exception {
    init(num, url, rate);
  }

  public void init(int num, String url, String rate) throws FrameGrabber.Exception, FrameRecorder.Exception {
    info = "cam" + num + " " + url;
    logger.info(info + " 正在初始化");
    canvasFrame = new CanvasFrame(info);
    canvasFrame.setVisible(true);
    grabber = new OpenCVFrameGrabber(num);
    grabber.start();
    frame = grabber.grab();
    converter = new OpenCVFrameConverter.ToIplImage();
    image = converter.convert(frame);
    recorder = FrameRecorder.createDefault(url, image.width(), image.height());
    recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
    recorder.setFormat("flv");
    recorder.setFrameRate(Double.parseDouble(rate));
    recorder.start();
    init = true;
  }

  @Override
  public void run() {
    if (!init) return;
    int a = 0;
    try {
      while (run && (frame = grabber.grab()) != null) {
//        System.out.println("推流..." + url);
        canvasFrame.showImage(frame);
        if (a % 5000 == 0) {
          a = 0;
          //每隔200帧进行一次识别，此处要加入消息队列
          logger.info(info + " 执行识别");
          Java2DFrameConverter java2dFrameConverter = new Java2DFrameConverter();
          searchFace(java2dFrameConverter.getBufferedImage(frame));
        }
        image = converter.convert(frame);
        //上传帧至recorder，grabber.grab()不能直接上传，要经过两次转换，即转成image再转回来
        Frame rotatedFrame = converter.convert(image);
//        if (startTime == 0) startTime = System.currentTimeMillis();
//        recorder.setTimestamp(1000 * (System.currentTimeMillis() - startTime));//时间戳
        if (run && rotatedFrame != null) recorder.record(rotatedFrame);
        a++;
//            Thread.sleep(40);
      }
      logger.info(info + " 视频处理线程关闭");
    } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
      logger.info(info + " 视频处理中断，视频流提前关闭");
    }
  }

  @Autowired
  private FaceController faceController;

  private void searchFace(BufferedImage image) {
//    System.err.println(image.imageData().getString());
//    String image64 = Base64.encodeBase64String(image.imageData().getString().getBytes());
//    System.out.println(image64);
//    String image64 = MatToBase64(image);
    FaceDto faceDto = new FaceDto();
    faceDto.setImage(bufferedImageToBase64(image));
    faceController.faceReco(faceDto);
  }

  /**
   * https://github.com/bytedeco/javacv/issues/1094
   * OpenCV Mat to JavaCV Mat conversion https://github.com/bytedeco/javacpp/issues/38
   */
  public static String bufferedImageToBase64(BufferedImage image) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      ImageIO.write(image, "jpg", outputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    BASE64Encoder encoder = new BASE64Encoder();
    return encoder.encode(outputStream.toByteArray());
  }

  public void stop() throws FrameGrabber.Exception, FrameRecorder.Exception, InterruptedException {
    logger.info(info + " 试图停止");
    this.run = false;
    this.canvasFrame.setVisible(false);
    Thread.sleep(500);
    this.grabber.stop();
    this.recorder.close();
  }

  public void restart(String url, String rate) throws FrameGrabber.Exception, FrameRecorder.Exception {
    LoggerFactory.getLogger(this.getClass()).info(info + " 试图重新启动");
    if (!run) {
      this.run = true;
      this.grabber.start();
      this.frame = grabber.grab();
      this.image = converter.convert(frame);
      this.canvasFrame.setVisible(true);
      recorder = FrameRecorder.createDefault(url, image.width(), image.height());
      recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
      recorder.setFormat("flv");
      recorder.setFrameRate(Double.parseDouble(rate));
      this.recorder.start();
      new Thread(this).start();
    }
  }

  @Override
  public String toString() {
    return info + " running:" + run;
  }
}
