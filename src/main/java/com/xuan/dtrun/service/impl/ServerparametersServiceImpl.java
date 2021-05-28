package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.entity.ServerparametersEntity;
import com.xuan.dtrun.mapper.ServerparametersMapper;
import com.xuan.dtrun.service.ServerparametersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServerparametersServiceImpl implements ServerparametersService{

     @Autowired
     private ServerparametersMapper serverparametersMapper;

     @Override
     public void create(String createTime, String  cpu, String runningmemory, String  diskusagepercentage, Integer uid) {
          serverparametersMapper.create(createTime,cpu,runningmemory,diskusagepercentage,uid);
     }

     @Override
     public List<ServerparametersEntity> getMessage(int uid) {
          return serverparametersMapper.getMessage(uid);
     }
}
