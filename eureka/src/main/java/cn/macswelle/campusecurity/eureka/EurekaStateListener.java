package cn.macswelle.campusecurity.eureka;

import cn.macswelle.campusecurity.common.dto.DeviceDto;
import cn.macswelle.campusecurity.feignapi.adapter.AdapterApi;
import cn.macswelle.campusecurity.feignapi.deviceManager.EurekaApi;
import com.netflix.appinfo.InstanceInfo;
import feign.Feign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 服务监听器
 */
@Component
@ComponentScan
public class EurekaStateListener {
    private final static Logger logger = LoggerFactory.getLogger(EurekaStateListener.class);

    @Autowired
    private EurekaApi eurekaApi;

    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceCanceledEvent event) {
        String msg = "服务" + event.getAppName() + "\n" + event.getServerId() + "已下线";
        //在此要向设备管理服务发送消息
        logger.info(msg);
        eurekaApi.instanceCancel(event.getServerId());
    }

    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        String msg = "服务" + instanceInfo.getAppName() + "\n" + instanceInfo.getHostName() + ":" +
                instanceInfo.getHomePageUrl() + " \nip: " + instanceInfo.getIPAddr() + "进行注册";
        logger.info(msg);
        String url = event.getInstanceInfo().getHomePageUrl();
        AdapterApi adapterClient = Feign.builder().target(AdapterApi.class, url);
        if (adapterClient != null) {
            DeviceDto info = adapterClient.getInfo();
            logger.info("获得硬件信息：" + info + "已发送至设备管理中心");
            if (info != null)
                eurekaApi.instanceRegister(info);
        }
    }
}
