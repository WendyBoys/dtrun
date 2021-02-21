package com.xuan.dtrun.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LogMapper {

    void create(Integer uid,String log,String createTime);

    List<String> getLogsById(Integer uid);
}
