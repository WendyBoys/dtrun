package com.xuan.dtrun.service;

import com.xuan.dtrun.entity.WhiteListEntity;

import java.util.List;

public interface WhiteListService {
     List<WhiteListEntity> findAll(int uid);

     void createWhiteList(WhiteListEntity whiteListEntity);

     String isCreate(String ip);

     void updateIp(int id,String ip);

     WhiteListEntity getWhiteListById(int id);

     void deleteWhiteList(Object[] ids);
}
