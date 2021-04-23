package cn.macswelle.campusecurity.nvrlistener;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "campusecurity.info")
public final class DeviceInfo {
  public static String name, location, url, description, version;
  public static boolean independent;
  public static Integer category;
}
