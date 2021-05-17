package com.xuan.dtrun.service;

import com.xuan.dtrun.entity.MoveTaskEntity;

import java.util.List;

public interface MoveTaskService {

    void create(MoveTaskEntity moveTaskEntity);

    List<MoveTaskEntity> findAll(Integer id);

    MoveTaskEntity getMoveTaskById(Integer id);

    void update(MoveTaskEntity moveTaskEntity);

    void delete(Object[] ids);

    void updateStatus(Integer id,String status);

     String getMoveTaskName(String taskName);
}
