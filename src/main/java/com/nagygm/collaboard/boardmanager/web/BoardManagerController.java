package com.nagygm.collaboard.boardmanager.web;

import com.nagygm.collaboard.auth.authorization.domain.BoardRoleValues;
import com.nagygm.collaboard.auth.authorization.domain.Role.Values.Const;
import com.nagygm.collaboard.boardmanager.domain.BoardDetails;
import com.nagygm.collaboard.boardmanager.service.BoardManagerService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board-manager")
public class BoardManagerController {
  final BoardManagerService boardManagerService;
  
  @Autowired
  public BoardManagerController(BoardManagerService boardManagerService) {
    this.boardManagerService = boardManagerService;
  }
  
  @PreAuthorize("hasAuthority('ROLE_USER') && hasAuthority('BOARD_ADMIN_' + #boardUrl)")
  @GetMapping("/preferences/{boardUrl}")
  public String preferences(@PathVariable("boardUrl") String boardUrl, Model model) {
    model.addAttribute("board", boardManagerService.getBoardDetails(boardUrl));
    model.addAttribute("authExpressionForOwner", "hasAuthority('BOARD_OWNER_" + boardUrl + "')");
    model.addAttribute("permissions", BoardRoleValues.BOARD_ADMIN.getAllBoardValuesforRole());
    return "board/board-preferences";
  }
  
  @PreAuthorize("hasAuthority('ROLE_USER')")
  @GetMapping(value={"/", ""})
  public String boardManager() {
    return "board/board-manager";
  }
}
