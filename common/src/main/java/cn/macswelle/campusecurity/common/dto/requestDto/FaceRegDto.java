package cn.macswelle.campusecurity.common.dto.requestDto;

import lombok.Data;

@Data
public class FaceRegDto {
  private String image;
  private String image_type = "BASE64";
  private String group_id;
  private String user_id;
  private String user_info;
  private String quality_control = "NONE";
  private String liveness_control = "NONE";
  private String action_type = "REPLACE";
  private int face_sort_type = 0;
}
