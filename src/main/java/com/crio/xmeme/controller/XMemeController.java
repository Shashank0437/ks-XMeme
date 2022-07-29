package com.crio.xmeme.controller;

import com.crio.xmeme.DTO.MemeDto;
import com.crio.xmeme.exchanges.postRequestDto;
import com.crio.xmeme.exchanges.postResponseDto;
import com.crio.xmeme.service.XMemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/memes")
@Api(value = "memes")
public class XMemeController {

  private final XMemeService xmemeServiceImpl;

  @ApiOperation(value = "Send a meme to the backend")
  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<postResponseDto> postMeme(@RequestBody @Valid postRequestDto reqDto) {

    if (reqDto.getName() == null || reqDto.getCaption() == null || reqDto.getUrl() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    if (xmemeServiceImpl.isPresent(reqDto)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    postResponseDto idDto = xmemeServiceImpl.saveAndReturnId(reqDto);
    return ResponseEntity.ok().body(idDto);
  }

  @ApiOperation(value = "Get all memes from the backend")
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MemeDto>> getMemes() {
    List<MemeDto> getRes = xmemeServiceImpl.getAllMemes();
    return ResponseEntity.ok().body(getRes);
  }

  @ApiOperation(value = "Get a meme from the backend")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MemeDto> getMeme(@PathVariable String id) {
    MemeDto getRes = xmemeServiceImpl.getMemeById(id);
    if (getRes != null) {
      return ResponseEntity.ok().body(getRes);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @ApiOperation(value = "Delete a meme from the backend")
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<MemeDto> deleteMeme(@PathVariable String id) {
    if (xmemeServiceImpl.getMemeById(id) != null) {
      xmemeServiceImpl.deleteMeme(id);
      return ResponseEntity.ok().body(null);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @ApiOperation(value = "Delete all memes from the backend")
  @RequestMapping(method = RequestMethod.DELETE)
  public ResponseEntity<MemeDto> deleteAllMemes() {
    xmemeServiceImpl.deleteMemes();
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}
