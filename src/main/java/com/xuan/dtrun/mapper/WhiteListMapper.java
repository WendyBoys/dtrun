package com.xuan.dtrun.mapper;

import com.xuan.dtrun.entity.WhiteListEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface WhiteListMapper {
     List<WhiteListEntity> findAll();

     void createWhiteList(WhiteListEntity whiteListEntity);

     String isCreate(String ip);

     void updateIp(int id,String ip);

     WhiteListEntity getWhiteListById(int id);

     void deleteWhiteList(Object[] ids);
}
