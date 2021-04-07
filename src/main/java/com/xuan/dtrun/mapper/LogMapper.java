package com.xuan.dtrun.mapper;

import com.xuan.dtrun.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LogMapper {

    void create(Integer uid,String log,String createTime,String color);

    List<LogEntity> getLogsById(Integer uid);

     List<LogEntity> getColorById(String color,Integer id);
}
