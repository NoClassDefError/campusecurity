package cn.macswelle.campusecurity.common.dto.requestDto;

import lombok.Data;

import java.util.List;

@Data
public class FaceDto {
  private String image;
  private String image_type = "BASE64";
  private List<String> group_id_list;
}
