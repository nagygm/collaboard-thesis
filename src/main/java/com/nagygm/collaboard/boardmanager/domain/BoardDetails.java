package com.nagygm.collaboard.boardmanager.domain;

import com.nagygm.collaboard.auth.authorization.domain.UserBoardDetailsAuthority;
import com.nagygm.collaboard.whiteboard.domain.BoardData;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain class for representing a Board in boards manager without the actual data
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "board_details")
public class BoardDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column
  private String title;
  @Column
  private String description;
  @Column
  private Boolean anonymusAllowedToRead;
  @Column(unique = true)
  private String urlHash;
  @Transient
  private BoardData boardData;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "boardDetails")
  private Set<UserBoardDetailsAuthority> userBoardDetailsAuthorities;
}
