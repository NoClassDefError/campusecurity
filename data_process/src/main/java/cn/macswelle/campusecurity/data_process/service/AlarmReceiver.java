package cn.macswelle.campusecurity.data_process.service;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "alarm", autoDelete = "true")
  , exchange = @Exchange(value = "topic", type = ExchangeTypes.TOPIC), key = "alarm.*"))
public class AlarmReceiver extends AmqpReceiver {
  @RabbitHandler
  private void alarm(String message) {
    logger.error("警告！" + message);
  }
}
