package com.xuan.dtrun.mapper;

import com.xuan.dtrun.entity.ServerparametersEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ServerparametersMapper {
     void create(String createTime,String  cpu, String  runningmemory, String diskusagepercentage, Integer uid);

     List<ServerparametersEntity> getMessage(int uid);
}
