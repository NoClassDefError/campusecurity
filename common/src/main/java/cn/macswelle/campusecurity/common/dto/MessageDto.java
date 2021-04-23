package cn.macswelle.campusecurity.common.dto;

import lombok.Data;

import java.util.Map;

@Data
public class MessageDto<T> {
  private T object;
  private Map<String,String> message;
}
