package cn.macswelle.campusecurity.nvrlistener;

import cn.macswelle.campusecurity.feignapi.deviceManager.BaiduApi;
import feign.Feign;
import lombok.Data;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;


@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "campusecurity.face")
@Data
public class Configuration {
  @Autowired
  private ObjectFactory<HttpMessageConverters> messageConverters;
  private String secretKey;
  private String apiKey;

  @Bean
  @Lazy
  @Cacheable(cacheNames = {"token"})
  public String getToken() {
    BaiduApi baiduApi = Feign.builder()
      .decoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)))
      .target(BaiduApi.class, "https://aip.baidubce.com/oauth/2.0/token?" +
        "grant_type=client_credentials&client_id=" + apiKey + "&client_secret=" + secretKey);
    LoggerFactory.getLogger(this.getClass()).info("正在登录百度AI平台");
    return baiduApi.login().get("access_token");
  }
}
