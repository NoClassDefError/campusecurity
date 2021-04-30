package cn.macswelle.campusecurity.eureka;

import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.common.dto.responseDto.HttpResult;
import cn.macswelle.campusecurity.feignapi.adapter.AdapterApi;
import cn.macswelle.campusecurity.feignapi.deviceManager.EurekaApi;
import com.netflix.appinfo.InstanceInfo;
import feign.Feign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * 服务监听器
 */
@Component
@ComponentScan
public class EurekaStateListener {
  private final static Logger logger = LoggerFactory.getLogger(EurekaStateListener.class);

  @Autowired
  private EurekaApi eurekaApi;

  @Autowired
  private ServletContext servletContext;

  @Autowired
  private ObjectFactory<HttpMessageConverters> messageConverters;

  @EventListener(condition = "#event.replication==false")
  public void listen(EurekaInstanceCanceledEvent event) {
    String msg = "服务下线 appName: " + event.getAppName() + " serverId: " + event.getServerId();
    logger.info(msg);
    //在此要向设备管理服务发送消息，要先判定是否为硬件
    if (servletContext.getAttribute(event.getServerId()) == null)
      eurekaApi.instanceCancel(event.getServerId());
    else servletContext.removeAttribute(event.getServerId());
  }

  @EventListener(condition = "#event.replication==false")
  public void listen(EurekaInstanceRegisteredEvent event) {
    InstanceInfo instanceInfo = event.getInstanceInfo();
    String msg = "服务注册 appName: " + instanceInfo.getAppName() +
      " url: " + instanceInfo.getHomePageUrl() +
      " ip: " + instanceInfo.getIPAddr() +
      " instanceId: " + instanceInfo.getId();
    logger.info(msg);
    String url = instanceInfo.getHomePageUrl();
    if (instanceInfo.getAppName().equalsIgnoreCase("gateway-server-v2")) {
      logger.info("分离版网关请求注册，考虑到他不能用web通信，于是不发送获取信息的请求。");
      return;
    }
    AdapterApi adapterClient = Feign.builder()
      .decoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)))
      .target(AdapterApi.class, url);//由于没有对象参数，不需要配置encoder
    if (adapterClient != null) {
      DeviceDto info = adapterClient.getInfo();
      if (info != null) {
        info.setId(instanceInfo.getId());
        logger.info("获得硬件信息：" + info + "已发送至设备管理中心");
        HttpResult result = eurekaApi.instanceRegister(info);
        if (!result.result.get("status").equals("success"))
          logger.info("注册失败，可能是硬件信息中的地址id不存在，请检查");
        else logger.info("注册成功");
      } else {
        servletContext.setAttribute(instanceInfo.getId(), false);
        logger.info("获得硬件信息为空，非硬件服务");
      }
    }
  }
}
