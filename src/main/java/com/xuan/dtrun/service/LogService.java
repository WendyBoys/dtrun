package com.xuan.dtrun.service;

import com.xuan.dtrun.entity.LogEntity;

import java.util.List;

public interface LogService {

    void create(Integer uid,String log,String createTime,String color);

    List<LogEntity> getLogsById(Integer uid);
}
