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
    public void create(Integer uid, String log, String createTime) {
        logMapper.create(uid, log, DateUtils.getDate());
    }

    @Override
    public List<LogEntity> getLogsById(Integer uid) {
        return logMapper.getLogsById(uid);
    }
}
