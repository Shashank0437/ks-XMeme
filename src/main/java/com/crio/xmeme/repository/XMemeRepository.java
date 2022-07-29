package com.crio.xmeme.repository;

import com.crio.xmeme.data.XMemeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;


public interface XMemeRepository extends MongoRepository<XMemeEntity, String> {


    @Query("{'name': '?0', 'url': '?1', 'caption': '?2' }")
    List<XMemeEntity>findByAllParams(String name, String url , String caption);
    List<XMemeEntity> findAll();
    Optional<XMemeEntity> findMemeById(String id);

    Long deleteMemeById(String id);
}
