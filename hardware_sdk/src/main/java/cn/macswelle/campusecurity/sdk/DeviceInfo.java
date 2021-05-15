package cn.macswelle.campusecurity.sdk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "campusecurity.info")
public final class DeviceInfo {
  public String name;
  public String location;
  public String url;
  public String description;
  public String version;
  public boolean independent;
  public Integer category;
}
