package com.xuan.dtrun.mapper;

import com.xuan.dtrun.entity.DtSourceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DtSourceMapper {
    void create(DtSourceEntity dtSourceEntity);

    DtSourceEntity getDtSourceById(int id);

    List<DtSourceEntity> findAll(int userId);

    void delete(Object[] ids);

    void update(DtSourceEntity dtSourceEntity);

    List<DtSourceEntity> getAllDtSourceName(int userId);

    String getDtSourceName(String dataSourceName);

    String getDtSourceName2(@Param("dtSourceName")String dtSourceName, @Param("id") int id);
}
