package com.xuan.dtrun.mapper;

import com.xuan.dtrun.entity.MoveTaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MoveTaskMapper {
    void create(MoveTaskEntity moveTaskEntity);

    List<MoveTaskEntity> findAll(Integer id);

    MoveTaskEntity getMoveTaskById(Integer id);

    void update(MoveTaskEntity moveTaskEntity);

    void delete(Object[] ids);

    void updateStatus(Integer id,String status);

     String getMoveTaskName(String taskName);
}
