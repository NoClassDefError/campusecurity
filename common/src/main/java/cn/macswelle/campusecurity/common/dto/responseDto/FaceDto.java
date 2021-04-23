package cn.macswelle.campusecurity.common.dto.responseDto;

import lombok.Data;

import java.util.List;

@Data
public class FaceDto {
  private String face_token;
  private List<PersonnelDto> user_list;
}
