package cn.macswelle.campusecurity.nvrlistener;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "campusecurity.info")
public final class DeviceInfo {
  private String name, location, url, description, version;
  private boolean independent;
  private Integer category;
}
