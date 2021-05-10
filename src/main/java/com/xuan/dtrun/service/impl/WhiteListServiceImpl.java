package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.entity.WhiteListEntity;
import com.xuan.dtrun.service.WhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.xuan.dtrun.mapper.*;

import java.util.List;

@Repository
public class WhiteListServiceImpl implements WhiteListService {

     @Autowired
     private WhiteListMapper whiteListMapper;


     @Override
     public List<WhiteListEntity> findAll(int uid) {
          List<WhiteListEntity> whiteListEntityList = whiteListMapper.findAll(uid);
          whiteListEntityList.forEach(dtSourceEntity -> {
               dtSourceEntity.setKey(dtSourceEntity.getId());
          });
          return whiteListEntityList;
     }

     @Override
     public void createWhiteList(WhiteListEntity whiteListEntity) {
          whiteListMapper.createWhiteList(whiteListEntity);
     }

     @Override
     public String isCreate(String ip) {
          return whiteListMapper.isCreate(ip);
     }

     @Override
     public void updateIp(int id, String ip) {
          whiteListMapper.updateIp(id,ip);
     }

     @Override
     public WhiteListEntity getWhiteListById(int id) {
          return whiteListMapper.getWhiteListById(id);
     }

     @Override
     public void deleteWhiteList(Object[] ids) {
          whiteListMapper.deleteWhiteList(ids);
     }


}
