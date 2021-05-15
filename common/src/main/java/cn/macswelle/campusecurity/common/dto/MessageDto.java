package cn.macswelle.campusecurity.common.dto;

import lombok.Data;

import java.util.Map;

/**
 * 原来用于MQ传输信息，现在配置了多个队列，不再需要传输泛型对象
 * @param <T>
 */
@Data
@Deprecated
public class MessageDto<T> {
  private T object;
  private Map<String,String> message;
}
