package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.entity.MoveTaskEntity;
import com.xuan.dtrun.mapper.MoveTaskMapper;
import com.xuan.dtrun.service.MoveTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MoveTaskServiceImpl implements MoveTaskService {

    @Autowired
    private MoveTaskMapper moveTaskMapper;

    @Override
    public void create(MoveTaskEntity moveTaskEntity) {
        moveTaskMapper.create(moveTaskEntity);
    }
}
