package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.entity.LogEntity;
import com.xuan.dtrun.mapper.LogMapper;
import com.xuan.dtrun.service.LogService;
import com.xuan.dtrun.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public void create(Integer uid, String log, String createTime,String color) {
        logMapper.create(uid, log, DateUtils.getDate(),color);
    }

    @Override
    public List<LogEntity> getLogsById(Integer uid) {
        return logMapper.getLogsById(uid);
    }

    @Override
    public List<LogEntity> getColorById(String color,Integer id) {
        List<LogEntity> colorById = logMapper.getColorById(color, id);
        System.out.println(colorById);
        return logMapper.getColorById(color,id);
    }
}
