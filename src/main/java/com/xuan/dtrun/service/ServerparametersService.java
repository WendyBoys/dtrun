package com.xuan.dtrun.service;

import com.xuan.dtrun.entity.ServerparametersEntity;

import java.util.List;

public interface ServerparametersService {
    void create(String createTime, String cpu, String runningmemory, String diskusagepercentage, Integer uid);

    List<ServerparametersEntity> getMessage(int uid);
}
