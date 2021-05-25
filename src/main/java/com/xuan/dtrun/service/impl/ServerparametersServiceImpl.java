package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.mapper.ServerparametersMapper;
import com.xuan.dtrun.service.ServerparametersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServerparametersServiceImpl implements ServerparametersService{

     @Autowired
     private ServerparametersMapper serverparametersMapper;

     @Override
     public void create(String date, String  cpu, String runningmemory, String  diskusagepercentage, Integer uid) {
          serverparametersMapper.create(date,cpu,runningmemory,diskusagepercentage,uid);
     }
}
