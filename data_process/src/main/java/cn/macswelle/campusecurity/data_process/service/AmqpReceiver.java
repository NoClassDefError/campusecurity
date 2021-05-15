package cn.macswelle.campusecurity.data_process.service;

import cn.macswelle.campusecurity.database.repositories.LocationRepository;
import cn.macswelle.campusecurity.database.repositories.PersonnelRepository;
import cn.macswelle.campusecurity.database.repositories.RecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AmqpReceiver {
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected LocationRepository locationRepository;

  @Autowired
  protected PersonnelRepository personnelRepository;

  @Autowired
  protected RecordRepository recordRepository;
}
