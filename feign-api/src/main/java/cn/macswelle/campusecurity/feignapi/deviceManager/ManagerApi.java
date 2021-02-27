package cn.macswelle.campusecurity.feignapi.deviceManager;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 用户向设备管理中心发送信息的api
 */
@FeignClient(value = "deviceManager")
public interface ManagerApi {

}
