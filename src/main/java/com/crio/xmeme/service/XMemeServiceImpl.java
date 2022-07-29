package com.crio.xmeme.service;

import com.crio.xmeme.DTO.MemeDto;
import com.crio.xmeme.data.XMemeEntity;
import com.crio.xmeme.exchanges.postRequestDto;
import com.crio.xmeme.exchanges.postResponseDto;
import com.crio.xmeme.repository.XMemeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class XMemeServiceImpl implements XMemeService {

    @Autowired
    private XMemeRepository xMemeRepository;

    @Autowired
    private Provider<ModelMapper> modelMapperProvider;

    @Autowired
    private MongoTemplate mongoTemplate;


    public boolean isPresent(postRequestDto newMeme){
        List<XMemeEntity> storedMemes = xMemeRepository.findByAllParams(newMeme.getName(), newMeme.getUrl(), newMeme.getCaption());
        return !storedMemes.isEmpty();
    }

    @Override
    public List<MemeDto> getAllMemes() {
        List<XMemeEntity> memeEntities = new ArrayList<>();
        if(xMemeRepository.count()>100){
            Query query = new Query();
            query.with(Sort.by(Sort.Direction.DESC, "$natural")).limit(100);
            memeEntities = mongoTemplate.find(query,XMemeEntity.class);
        }else{
             memeEntities = xMemeRepository.findAll();
        }
        List<MemeDto> memeList = new ArrayList<>();
        for( XMemeEntity entity : memeEntities){
            memeList.add(modelMapperProvider.get().map(entity, MemeDto.class));
        }
        return memeList;
    }

    @Override
    public MemeDto getMemeById(String id) {
        Optional<XMemeEntity> opMeme = xMemeRepository.findMemeById(id);
        if(opMeme.isPresent()){
            XMemeEntity memeEntity = opMeme.get();
            return modelMapperProvider.get().map(memeEntity, MemeDto.class);
        }
        return null;
    }

    public postResponseDto saveAndReturnId(postRequestDto newMeme){
        int memes = (int) xMemeRepository.count();
        XMemeEntity xMemeEntity = modelMapperProvider.get().map(newMeme,XMemeEntity.class);
        xMemeEntity.setId(String.valueOf(memes+1));
        xMemeEntity =  xMemeRepository.save(xMemeEntity);
        return new postResponseDto(xMemeEntity.getId());
    }
    @Override
    public void deleteMeme(String id) {
        xMemeRepository.deleteMemeById(id);
    }

    @Override
    public void deleteMemes() {
        xMemeRepository.deleteAll();
    }

}
