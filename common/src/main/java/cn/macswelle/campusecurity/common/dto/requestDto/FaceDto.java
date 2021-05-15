package cn.macswelle.campusecurity.common.dto.requestDto;

import lombok.Data;

@Data
public class FaceDto {
  private String image;
  private String image_type = "BASE64";
  private String location;
  private Long time;
}
