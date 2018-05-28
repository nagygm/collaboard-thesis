package com.nagygm.collaboard.whiteboard.web;

import com.nagygm.collaboard.boardmanager.web.SetBoardRoleForUserDto;
import com.nagygm.collaboard.boardmanager.web.UpdateBoardDetailsDto;
import com.nagygm.collaboard.whiteboard.service.BoardService;
import com.nagygm.collaboard.boardmanager.service.BoardManagerService;
import com.nagygm.collaboard.boardmanager.web.BoardDetailsWithPermissionDto;
import com.nagygm.collaboard.boardmanager.web.CreateBoardDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board-manager")
public class BoardRestController {
  
  final private BoardManagerService boardManagerService;
  final private BoardService boardService;
  final private Validator validator;
  
  @Autowired
  public BoardRestController(
    BoardManagerService boardManagerService,
    BoardService boardService,
    @Qualifier("mvcValidator") Validator validator) {
    this.boardManagerService = boardManagerService;
    this.boardService = boardService;
    this.validator = validator;
  }
  
  /**
   * @return Returns List of BoardDetails, used for managing board preferences, and relationships between Boards and users
   */
  @GetMapping("/boards")
  public List<BoardDetailsWithPermissionDto> getBoards() {
    return boardManagerService.getAllBoards();
  }
  
  @PostMapping("/boards")
  public ResponseEntity createBoard(@Valid CreateBoardDto dto, BindingResult bindingResult) {
    dto.setErrors(bindingResult);
    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body(dto);
    }
    dto = boardManagerService.createBoard(dto);
    
    if (dto.getErrors().hasErrors()) {
      return ResponseEntity.badRequest().body(dto);
    } else {
      return ResponseEntity.ok(dto);
    }
  }
  
  @DeleteMapping("/board/{boardUrl}")
  public ResponseEntity delete(@PathVariable("boardUrl") String boardUrl) {
    if (boardManagerService.deleteBoardDetails(boardUrl)) {
      return new ResponseEntity(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
  
  @PreAuthorize("hasAuthority('BOARD_OWNER_' + #boardUrl)")
  @PutMapping("/board/{boardUrl}")
  public ResponseEntity update(@Valid @RequestBody UpdateBoardDetailsDto dto,
    BindingResult bindingResult,
    @PathVariable("boardUrl") String boardUrl, Model model) {
    dto.setUrlHash(boardUrl);
    dto.setErrors(bindingResult);
    if (dto.getErrors().hasErrors()) {
      return ResponseEntity.badRequest().body(dto);
    }
    UpdateBoardDetailsDto response = boardManagerService.updateBoardDetails(dto);
    if (response.getErrors().hasErrors()) {
      return ResponseEntity.badRequest().body(dto);
    } else {
      return ResponseEntity.ok(dto);
    }
  }
  
  @PreAuthorize("hasAuthority('BOARD_ADMIN_' + #boardUrl)")
  @PutMapping("/board/{boardUrl}/user/{username}")
  public ResponseEntity updateMemberBoardRole(
    @PathVariable("boardUrl") String boardUrl,
    @PathVariable("username") String username,
    String role) {
    
    SetBoardRoleForUserDto dto = SetBoardRoleForUserDto.builder()
      .boardUrl(boardUrl)
      .username(username)
      .role(role)
      .build();
    
    validator.validate(dto, dto.getErrors());
    if (dto.getErrors().hasErrors()) {
      return ResponseEntity.badRequest().body(dto);
    }
    dto = boardManagerService.setBoardRoleForAppUser(dto);
    if (dto.getErrors().hasErrors()) {
      return ResponseEntity.badRequest().body(dto);
    } else {
      return ResponseEntity.ok(dto);
    }
  }
  
  @PostMapping("/board/{boardUrl}/users")
  public ResponseEntity inviteMember(
    @PathVariable("boardUrl") String boardUrl,
    @Valid SetBoardRoleForUserDto dto,
    BindingResult bindingResult
  ) {
    dto.setBoardUrl(boardUrl);
    dto.setErrors(bindingResult);
    
    if (dto.getErrors().hasErrors()) {
      return ResponseEntity.badRequest().body(dto);
    }
    dto = boardManagerService.setBoardRoleForAppUser(dto);
    if (dto.getErrors().hasErrors()) {
      return ResponseEntity.badRequest().body(dto);
    } else {
      return ResponseEntity.ok(dto);
    }
  }
  
  @PreAuthorize("hasAuthority('BOARD_ADMIN_' + #boardUrl)")
  @DeleteMapping("/board/{boardUrl}/user/{username}")
  public ResponseEntity removeMember(
    @PathVariable("boardUrl") String boardUrl,
    @PathVariable("username") String username
  ) {
    RemoveAppUserFromBoardDto dto =
      RemoveAppUserFromBoardDto.builder().boardUrl(boardUrl).username(username).build();
    dto.setErrors( new BeanPropertyBindingResult(dto, ""));
    dto = boardManagerService.removeBoardUserAuthoritites(dto);
    if(dto.getErrors().hasErrors()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    } else {
      return ResponseEntity.ok(dto);
    }
  }
  
  
  @GetMapping("/board/{boardUrl}/users")
  public ResponseEntity getAllBoardMembers(@PathVariable("boardUrl") String boardUrl) {
    return ResponseEntity.ok(boardManagerService.getAllBoardMembers(boardUrl));
  }
  
}
