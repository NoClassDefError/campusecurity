package cn.macswelle.campusecurity.nvrlistener.service;

import cn.macswelle.campusecurity.common.dto.requestDto.FaceDto;
import cn.macswelle.campusecurity.nvrlistener.controller.FaceController;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        image = converter.convert(frame);
        if (a % 500 == 0) {
          a = 0;
          //每隔200帧进行一次识别，此处要加入消息队列
          logger.info(info + " 执行识别");
          searchFace(image);
        }
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
      logger.info(info + "视频处理中断，视频流提前关闭");
    }
  }

  @Autowired
  private FaceController faceController;

  private void searchFace(opencv_core.IplImage image) {
    String image64 = image.imageData().getString();
    FaceDto faceDto = new FaceDto();
    faceDto.setImage(image64);
    List<String> groupId = new ArrayList<>();
    groupId.add("ghc");
    faceDto.setGroup_id_list(groupId);
    faceController.faceReco(faceDto);
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
