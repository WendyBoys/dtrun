package com.xuan.dtrun.mapper;

import com.xuan.dtrun.entity.MoveTaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MoveTaskMapper {
    void create(MoveTaskEntity moveTaskEntity);
}
