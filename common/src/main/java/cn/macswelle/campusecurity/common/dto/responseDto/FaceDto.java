package cn.macswelle.campusecurity.common.dto.responseDto;

import lombok.Data;

@Data
public class FaceDto {
  private String face_token;
  private String group_id;
  private String user_id;
  private String user_info;
  private Double score;
}
