package cn.macswelle.campusecurity.data_process.service;

import cn.macswelle.campusecurity.common.dto.requestDto.RecordDto;
import cn.macswelle.campusecurity.database.entities.Record;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "alarm", autoDelete = "true")
  , exchange = @Exchange(value = "topic", type = ExchangeTypes.TOPIC), key = "*.record"))
public class RecordReceiver extends AmqpReceiver {
  @RabbitHandler
  private void record(String message) throws IOException {
    RecordDto dto = objectMapper.readValue(message, RecordDto.class);
    Record record = new Record();
    record.setPersonnel(personnelRepository.findById(dto.getPersonnel()).orElse(null));
    record.setFile(dto.getFile());
    record.setLocation(locationRepository.findById(dto.getLocation()).orElse(null));
    record.setEvent(dto.getEvent());
    record.setCreateTime(dto.getCreateTime());
    recordRepository.save(record);
  }
}
