package com.nagygm.collaboard.whiteboard.web;

import com.nagygm.collaboard.auth.authentication.SecurityService;
import com.nagygm.collaboard.boardmanager.domain.BoardDetails;
import com.nagygm.collaboard.boardmanager.web.BoardDetailsDto;
import com.nagygm.collaboard.whiteboard.domain.BoardData;
import com.nagygm.collaboard.whiteboard.domain.Command;
import com.nagygm.collaboard.whiteboard.domain.FailCommand;
import com.nagygm.collaboard.whiteboard.service.BoardService;
import java.security.Principal;
import javax.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WhiteboardController {
  private final BoardService boardService;
  private final SecurityService securityService;
  
  @Autowired
  public WhiteboardController(BoardService boardService, SecurityService securityService) {
    this.boardService = boardService;
    this.securityService = securityService;
  }
  
  
  @MessageMapping("/{boardUrl}")
  @SendTo("/topic/board/{boardUrl}")
  public Command boards(Command command, @DestinationVariable("boardUrl") String boardUrl,
    Principal principal ) {
    String username = principal.getName();
    String[] commandId = command.getId().split("#", 2);
    if (commandId.length > 1 && commandId[0].equals(username)) {
      return boardService.translateCommand(command, boardUrl);
    } else {
      return new FailCommand(username, "Invalid commandId");
    }
  }
  
  @GetMapping("/board/{boardUrl}")
  public String board(@PathVariable("boardUrl") String boardUrl, Model model) {
    //TODO send board details dto instead
    BoardDetailsDto boardDetails = boardService.getBoardDetails(boardUrl);
    model.addAttribute("urlHash", boardDetails.getUrlHash());
    model.addAttribute("username", securityService.findLoggedInUsername());
    model.addAttribute("boardTitle", boardDetails.getTitle());
    //todo properly instead of hard wired
    model.addAttribute("boardIsReadOnly", !securityService.hasAuthority("BOARD_WRITE_" + boardUrl));
    return "board/board";
  }
  
  @GetMapping("/board/{boardUrl}/init")
  @ResponseBody
  public BoardData boardInit(@PathVariable("boardUrl") String boardUrl) {
    //TODO send only dto
    return boardService.getBoardData(boardUrl)
      .orElseThrow(() -> new ValidationException("Non existent board"));
  }
  
}
