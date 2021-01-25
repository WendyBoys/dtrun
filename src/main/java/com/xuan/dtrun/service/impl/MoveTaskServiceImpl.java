package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.entity.MoveTaskEntity;
import com.xuan.dtrun.mapper.MoveTaskMapper;
import com.xuan.dtrun.service.MoveTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MoveTaskServiceImpl implements MoveTaskService {

    @Autowired
    private MoveTaskMapper moveTaskMapper;

    @Override
    public List<MoveTaskEntity> findAll(Integer id) {
        List<MoveTaskEntity> taskEntityList = moveTaskMapper.findAll(id);
        taskEntityList.forEach(moveTaskEntity -> {
            moveTaskEntity.setKey(moveTaskEntity.getId());
        });
        return taskEntityList;
    }

    @Override
    public MoveTaskEntity getMoveTaskById(Integer id) {
        return moveTaskMapper.getMoveTaskById(id);
    }

    @Override
    public void update(MoveTaskEntity moveTaskEntity) {
        moveTaskMapper.update(moveTaskEntity);
    }

    @Override
    public void delete(Object[] ids) {
        moveTaskMapper.delete(ids);
    }

    @Override
    public void updateStatus(Integer id,String status) {
        moveTaskMapper.updateStatus(id,status);
    }

    @Override
    public void create(MoveTaskEntity moveTaskEntity) {
        moveTaskMapper.create(moveTaskEntity);
    }


}
