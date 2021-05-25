package com.xuan.dtrun.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ServerparametersMapper {
     void create(String date,String  cpu, String  runningmemory, String diskusagepercentage, Integer uid);
}
