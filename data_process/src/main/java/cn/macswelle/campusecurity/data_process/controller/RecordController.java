package cn.macswelle.campusecurity.data_process.controller;

import cn.macswelle.campusecurity.common.dto.requestDto.RecordDto;
import cn.macswelle.campusecurity.data_process.service.AmqpReceiver;
import cn.macswelle.campusecurity.database.entities.Personnel;
import cn.macswelle.campusecurity.database.entities.Record;
import cn.macswelle.campusecurity.feignapi.deviceManager.RecordApi;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecordController extends AmqpReceiver implements RecordApi {

  @Override
  public String savePersonnel(int category, String name, String description) {
    Personnel personnel = new Personnel();
    personnel.setName(name);
    personnel.setDescription(description);
    personnel.setCategory(category);
    return personnelRepository.save(personnel).getId();
  }

  @Override
  public String saveRecord(RecordDto dto) {
    Record record = new Record();
    record.setPersonnel(personnelRepository.findById(dto.getPersonnel()).orElse(null));
    record.setFile(dto.getFile());
    record.setLocation(locationRepository.findById(dto.getLocation()).orElse(null));
    record.setEvent(dto.getEvent());
    record.setCreateTime(dto.getCreateTime());
    return recordRepository.save(record).getId();
  }
}
