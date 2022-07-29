package com.crio.xmeme.service;

import com.crio.xmeme.DTO.MemeDto;
import com.crio.xmeme.exchanges.postRequestDto;
import com.crio.xmeme.exchanges.postResponseDto;
import java.util.List;

public interface XMemeService {
    postResponseDto saveAndReturnId(postRequestDto newMeme);
    boolean isPresent(postRequestDto newMeme);

    List<MemeDto> getAllMemes();

    MemeDto getMemeById(String id);

    void deleteMeme(String id);

    void deleteMemes();
}
