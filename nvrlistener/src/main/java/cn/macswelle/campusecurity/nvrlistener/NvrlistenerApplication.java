package cn.macswelle.campusecurity.nvrlistener;

import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients( "cn.macswelle.campusecurity.feignapi.deviceManager")
public class NvrlistenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NvrlistenerApplication.class, args);
    }

    @Bean
    public OpenCVFrameGrabber getCamera(){
      OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
      try {
        grabber.start();   //开始获取摄像头数据
      } catch (FrameGrabber.Exception e) {
        e.printStackTrace();
      }
      return grabber;
    }
}
