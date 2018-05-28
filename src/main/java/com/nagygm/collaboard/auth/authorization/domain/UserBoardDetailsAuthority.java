package com.nagygm.collaboard.auth.authorization.domain;

import com.nagygm.collaboard.boardmanager.domain.BoardDetails;
import com.nagygm.collaboard.user.domain.AppUser;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(exclude = {"appUser", "boardDetails", "boardAuthority"})
@Data
@Entity(name = "app_user_board_details_board_authority")
public class UserBoardDetailsAuthority {
  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "appUserId",
      column = @Column(name = "app_user_id", nullable = false)),
    @AttributeOverride(name = "boardDetailsId",
      column = @Column(name = "board_details_id", nullable = false)),
    @AttributeOverride(name = "boardAuthorityId",
      column = @Column(name = "board_authority_id", nullable = false))
  })
  UserBoardDetailsAuthorityId id;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "app_user_id", nullable = false, insertable = false, updatable = false)
  private AppUser appUser;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "board_details_id", nullable = false, insertable = false, updatable = false)
  private BoardDetails boardDetails;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "board_authority_id", nullable = false, insertable = false, updatable = false)
  private BoardAuthority boardAuthority;
}
