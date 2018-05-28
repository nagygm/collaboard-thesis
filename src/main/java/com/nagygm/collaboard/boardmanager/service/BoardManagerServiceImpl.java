package com.nagygm.collaboard.boardmanager.service;

import com.nagygm.collaboard.auth.authentication.SecurityService;
import com.nagygm.collaboard.auth.authorization.dal.UserBoardDetailsAuthorityRepository;
import com.nagygm.collaboard.auth.authorization.domain.BoardRoleValues;
import com.nagygm.collaboard.auth.authorization.domain.Role.Values.Const;
import com.nagygm.collaboard.auth.authorization.domain.UserBoardDetailsAuthority;
import com.nagygm.collaboard.auth.authorization.service.BoardAuthoritiesService;
import com.nagygm.collaboard.boardmanager.dal.BoardDetailsRepository;
import com.nagygm.collaboard.boardmanager.web.BoardDetailsDto;
import com.nagygm.collaboard.boardmanager.web.SetBoardRoleForUserDto;
import com.nagygm.collaboard.boardmanager.web.UpdateBoardDetailsDto;
import com.nagygm.collaboard.user.service.AppUserService;
import com.nagygm.collaboard.whiteboard.domain.BoardData;
import com.nagygm.collaboard.whiteboard.dal.BoardDataRepository;
import com.nagygm.collaboard.boardmanager.domain.BoardDetails;
import com.nagygm.collaboard.boardmanager.web.BoardDetailsWithPermissionDto;
import com.nagygm.collaboard.boardmanager.web.CreateBoardDto;
import com.nagygm.collaboard.user.domain.AppUser;
import com.nagygm.collaboard.whiteboard.web.RemoveAppUserFromBoardDto;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardManagerServiceImpl implements BoardManagerService {
  
  final private BoardDataRepository boardDataRepository;
  final private BoardDetailsRepository boardDetailsRepository;
  final private UserBoardDetailsAuthorityRepository userBoardDetailsAuthorityRepository;
  final private SecurityService securityService;
  final private AppUserService appUserService;
  final private BoardAuthoritiesService boardAuthoritiesService;
  final private ModelMapper modelMapper;
  
  @Autowired
  public BoardManagerServiceImpl(
    BoardDataRepository boardDataRepository,
    BoardDetailsRepository boardDetailsRepository,
    UserBoardDetailsAuthorityRepository userBoardDetailsAuthorityRepository,
    SecurityService securityService, AppUserService appUserService,
    BoardAuthoritiesService boardAuthoritiesService,
    ModelMapper modelMapper) {
    this.boardDataRepository = boardDataRepository;
    this.boardDetailsRepository = boardDetailsRepository;
    this.userBoardDetailsAuthorityRepository = userBoardDetailsAuthorityRepository;
    this.securityService = securityService;
    this.appUserService = appUserService;
    this.boardAuthoritiesService = boardAuthoritiesService;
    this.modelMapper = modelMapper;
  }
  
  @PreAuthorize("hasAuthority('ROLE_USER')")
  @Transactional
  @Override
  public List<BoardDetailsWithPermissionDto> getAllBoards() {
    //TODO fix board authoritites system introducing ROLE_BOARD
    return userBoardDetailsAuthorityRepository
      .findByAppUser(getAuthenticatedAppUser())
      .stream()
      .map(it -> {
        BoardDetails boardDetails = it.getBoardDetails();
        return BoardDetailsWithPermissionDto.builder()
          .title(boardDetails.getTitle())
          .description(boardDetails.getDescription())
          .urlHash(boardDetails.getUrlHash())
          .permission(BoardRoleValues.valueOf(it.getBoardAuthority().getName()))
          .build();
      }).collect(Collectors.groupingBy(BoardDetailsWithPermissionDto::getUrlHash)).entrySet()
      .stream()
      .map(it -> it.getValue().stream().max(Comparator.comparingInt(l -> l.getPermission().level)))
      .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
  }
  
  @Transactional(rollbackFor = {Exception.class})
  @Override
  public CreateBoardDto createBoard(CreateBoardDto dto) {
    String urlHash = generateUniqueHash();
    
    BoardDetails boardDetails = BoardDetails.builder()
      .anonymusAllowedToRead(dto.getAnonymusAllowedToRead())
      .title(dto.getTitle())
      .description(dto.getDescription())
      .urlHash(generateUniqueHash())
      .build();
    boardDetails.setAnonymusAllowedToRead(dto.getAnonymusAllowedToRead());
    boardDetails.setTitle(dto.getTitle());
    boardDetails.setUrlHash(urlHash);
    
    final BoardDetails persistedBoardDetails = boardDetailsRepository.save(boardDetails);
    
    AppUser appUser = getAuthenticatedAppUser();
    
    boardAuthoritiesService
      .addToUserForBoardDetails(appUser, persistedBoardDetails, BoardRoleValues.BOARD_OWNER);
    
    securityService.updateGrantedAuthoritesForSelf();
    
    BoardData data = BoardData.builder()
      .id(BigInteger.valueOf(boardDetails.getId()))
      .drawingLibrary(dto.getDrawingLibrary())
      .urlHash(urlHash)
      .version(dto.getVersion())
      .objects(new ArrayList<>())
      .build();
    boardDataRepository.save(data);
    return dto;
  }
  
  @Override
  public BoardDetailsDto getBoardDetails(String urlHash) {
    return modelMapper.map(
      boardDetailsRepository.findByUrlHash(urlHash).get(),
      BoardDetailsDto.class
    );
  }
  
  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean deleteBoardDetails(String urlHash) {
    int result = boardDetailsRepository.deleteByUrlHash(urlHash);
    boardDataRepository.deleteByUrlHash(urlHash);
    return result > 0;
  }
  
  @Override
  public UpdateBoardDetailsDto updateBoardDetails(UpdateBoardDetailsDto dto) {
    BoardDetails boardDetails = boardDetailsRepository.findByUrlHash(dto.getUrlHash()).get();
    modelMapper.map(dto, boardDetails);
    boardDetails = boardDetailsRepository.save(boardDetails);
    return modelMapper.map(boardDetails, UpdateBoardDetailsDto.class);
  }
  
  
  @Transactional
  @Override
  public SetBoardRoleForUserDto setBoardRoleForAppUser(SetBoardRoleForUserDto dto) {
    Optional<AppUser> appUser = appUserService.findUserByUserName(dto.getUsername());
    Optional<BoardRoleValues> role = Arrays.stream(BoardRoleValues.values())
      .filter(it -> it.name().equals(dto.getRole())).findFirst();
    //TODO all validating logic to Validators
    if (!role.isPresent()) {
      dto.getErrors().rejectValue("role", "boardPreferences.error.unknowRole");
      return dto;
    }
    if(role.get().equals(BoardRoleValues.BOARD_OWNER)){
      dto.getErrors().rejectValue("role", "boardPreferences.error.cantSetOwner");
      return dto;
    }
    if (appUser.isPresent()) {
      //It is safe beacuse there is a boardUrl because of the auth
      BoardDetails boardDetails = boardDetailsRepository.findByUrlHash(dto.getBoardUrl()).get();
      
      if(boardAuthoritiesService.isOwner(appUser.get(),boardDetails)) {
        dto.getErrors().rejectValue("username", "boardPreferences.error.cantModifyOwner");
        return dto;
      }
      
      boardAuthoritiesService.addToUserForBoardDetails(
        appUser.get(),
        boardDetails,
        role.get());
      securityService.updateGrantedAuthoritesForUser(dto.getUsername());
    } else {
      dto.getErrors().rejectValue("username", "boardPreferences.error.unknownUser");
      return dto;
    }
    return dto;
    
  }
  
  @Transactional(readOnly = true)
  @Override
  public List<BoardMemberDto> getAllBoardMembers(String boardUrl) {
    //Safe to call becaue of authorization
    BoardDetails boardDetails = boardDetailsRepository.findByUrlHash(boardUrl).get();
    return userBoardDetailsAuthorityRepository.findByBoardDetails(boardDetails).stream().map(it -> {
      return BoardMemberDto.builder()
        .username(it.getAppUser().getUsername())
        .lastName(it.getAppUser().getLastName())
        .firstName(it.getAppUser().getFirstName())
        .permission(BoardRoleValues.valueOf(it.getBoardAuthority().getName()))
        .build();
    }).collect(Collectors.groupingBy(BoardMemberDto::getUsername)).entrySet().stream()
      .map(it -> it.getValue().stream().max(Comparator.comparingInt(l -> l.getPermission().level)))
      .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
  }
  
  @Transactional
  @Override
  public RemoveAppUserFromBoardDto removeBoardUserAuthoritites(RemoveAppUserFromBoardDto dto) {
    BoardDetails boardDetails = boardDetailsRepository.findByUrlHash(dto.getBoardUrl()).get();
    Optional<AppUser> appUserOptional = appUserService.findUserByUserName(dto.getUsername());
    if (appUserOptional.isPresent()) {
      AppUser appUser = appUserOptional.get();
      if (boardAuthoritiesService.isOwner(appUser,boardDetails)) {
        dto.getErrors().rejectValue("username", "removeUserFromBoard.error.isOwner");
        return dto;
      }
      boardAuthoritiesService.delete(appUser, boardDetails);
      securityService.updateGrantedAuthoritesForUser(appUser.getUsername());
      return dto;
    }
    dto.getErrors().rejectValue("username", "removeUserFromBoard.error.appUserNotPresent");
    return dto;
  }
  
  @Override
  public Optional<Boolean> initateOwnershipChange() {
    throw new UnsupportedOperationException();
  }
  
  //Tries generating unique hash till the end of times
  private String generateUniqueHash() {
    //TODO to factory
    String hash = UUID.randomUUID().toString().replaceAll("-", "");
    if (boardDetailsRepository.findByUrlHash(hash).isPresent()) {
      return generateUniqueHash();
    } else {
      return hash;
    }
  }
  
  private AppUser getAuthenticatedAppUser() {
    return appUserService.findUserByUserName(securityService.findLoggedInUsername())
      .orElseThrow(() -> new RuntimeException("Fatal error, unknown authenticated user"));
  }
  
}
