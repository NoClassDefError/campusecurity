package cn.macswelle.campusecurity.nvrlistener.service;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 视频的处理与推流服务，作为生命周期独立的线程，不直接交给spring管理
 */
public class VirtualCameraService implements Runnable {

  private final OpenCVFrameGrabber grabber;
  private boolean run = true;
  private FrameRecorder recorder;

  private CanvasFrame canvasFrame;
  private final BaiduAIFace faceapi;
  private Frame frame;
  private opencv_core.IplImage image;
  private final OpenCVFrameConverter.ToIplImage converter;
  //  private long startTime = 0;
  private final String info;

  public VirtualCameraService(int num, String url, String rate, BaiduAIFace faceapi) throws FrameGrabber.Exception, FrameRecorder.Exception {
    this.faceapi = faceapi;
    info = "cam" + num + " " + url;
    LoggerFactory.getLogger(this.getClass()).info(info + " 正在初始化");
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
  }

  @Override
  public void run() {
    int a = 0;
    try {
      while (run && (frame = grabber.grab()) != null) {
//        System.out.println("推流..." + url);
        canvasFrame.showImage(frame);
        image = converter.convert(frame);
        if (a % 500 == 0) {
          a = 0;
          //每隔200帧进行一次识别
          LoggerFactory.getLogger(this.getClass()).info(info + " 执行识别");
//          String buffers = image.imageData().getString();
//          Map<String, Object> searchface = searchFace(buffers);
//          if (searchface == null) LoggerFactory.getLogger(this.getClass()).info("没有人 " + info);
//          else if (searchface.get("user_id") == null) LoggerFactory.getLogger(this.getClass()).info("陌生人 " + info);
        }
        //上传帧至recorder，grabber.grab()不能直接上传，要经过两次转换，即转成image再转回来
        Frame rotatedFrame = converter.convert(image);
//        if (startTime == 0) startTime = System.currentTimeMillis();
//        recorder.setTimestamp(1000 * (System.currentTimeMillis() - startTime));//时间戳

        if (run && rotatedFrame != null) recorder.record(rotatedFrame);
        a++;
//            Thread.sleep(40);
      }
      LoggerFactory.getLogger(this.getClass()).info(info + " 视频处理线程关闭");
    } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
      LoggerFactory.getLogger(this.getClass()).info(info + "视频处理中断，视频流提前关闭");
    }
  }

  private Map<String, Object> searchFace(String imagebase64) {
//    String substring = imagebase64.substring(imagebase64.indexOf(",") + 1, imagebase64.length());
    Setingmodel setingmodel = new Setingmodel();
    setingmodel.setImgpath(imagebase64);
    setingmodel.setGroupID("StRoot");
//    System.out.println(imagebase64);
    return faceapi.FaceSearch(setingmodel);
  }

  public void stop() throws FrameGrabber.Exception, FrameRecorder.Exception, InterruptedException {
    LoggerFactory.getLogger(this.getClass()).info(info + " 试图停止");
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
