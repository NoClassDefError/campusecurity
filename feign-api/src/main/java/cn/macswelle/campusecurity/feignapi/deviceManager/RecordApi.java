package cn.macswelle.campusecurity.feignapi.deviceManager;

import cn.macswelle.campusecurity.common.dto.requestDto.RecordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "data_process", path = "/device")
public interface RecordApi {
  @RequestMapping(value = "/admin/savePersonnel", method = RequestMethod.POST)
  String savePersonnel(int category, String name, String description);

  @RequestMapping(value = "/admin/addRecord", method = RequestMethod.POST)
  String saveRecord(@RequestBody RecordDto dto);
}
