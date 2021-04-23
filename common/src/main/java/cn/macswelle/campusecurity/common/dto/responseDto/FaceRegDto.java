package cn.macswelle.campusecurity.common.dto.responseDto;

import lombok.Data;

@Data
public class FaceRegDto {
  private String face_token;

  private Location location;

  @Data
  class Location {
    private String left;
    private String height;
    private String top;
    private String width;
    private String rotation;
  }
}
