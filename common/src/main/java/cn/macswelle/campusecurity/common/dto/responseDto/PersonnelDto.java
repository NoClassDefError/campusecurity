package cn.macswelle.campusecurity.common.dto.responseDto;

import lombok.Data;

@Data
public class PersonnelDto {
  private String group_id;
  private String user_id;
  private String user_info;
  private String score;
}
