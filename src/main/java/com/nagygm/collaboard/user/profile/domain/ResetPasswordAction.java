package com.nagygm.collaboard.user.profile.domain;

import com.nagygm.collaboard.user.domain.AppUser;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResetPasswordAction {
  @Id
  private String token;
  @Fetch(FetchMode.JOIN)
  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
  private AppUser appUser;
}
