package com.nagygm.collaboard.boardmanager.web;

import com.nagygm.collaboard.common.web.BaseDto;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SetBoardRoleForUserDto extends BaseDto {
  String boardUrl;
  @NotEmpty
  String username;
  @NotEmpty
  String role;
}
