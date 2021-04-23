package cn.macswelle.campusecurity.feignapi.deviceManager;

import cn.macswelle.campusecurity.common.dto.requestDto.FaceRegDto;
import cn.macswelle.campusecurity.common.dto.responseDto.FaceDto;
import feign.Headers;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Map;

@FeignClient(value = "baidu")
public interface BaiduApi {
  @RequestLine("POST")
  @Headers("Content-Type: application/json")
  cn.macswelle.campusecurity.common.dto.responseDto.FaceRegDto faceRegister(FaceRegDto faceRegDto);

  @RequestLine("POST")
  @Headers("Content-Type: application/json")
  FaceDto faceReco(cn.macswelle.campusecurity.common.dto.requestDto.FaceDto faceDto);

  @RequestLine("GET")
  @Headers("Content-Type: application/json")
  Map<String,String> login();
}
