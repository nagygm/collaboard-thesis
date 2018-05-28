package com.nagygm.collaboard.auth.authorization.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBoardDetailsAuthorityId implements Serializable {
  @Column(name = "app_user_id", nullable = false)
  private Long appUserId;
  
  @Column(name = "board_details_id", nullable = false)
  private Long boardDetailsId;
  
  @Column(name = "board_authority_id", nullable = false)
  private Integer boardAuthorityId;
}