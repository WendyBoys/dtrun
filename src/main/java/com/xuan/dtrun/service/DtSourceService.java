package com.xuan.dtrun.service;

import com.xuan.dtrun.entity.DtSourceEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DtSourceService {

    void create(DtSourceEntity dtSourceEntity);

    DtSourceEntity getDtSourceById(int id);

    List<DtSourceEntity> findAll(int userId);

    void delete(Object[] ids);

    void update(DtSourceEntity dtSourceEntity);

    List<DtSourceEntity> getAllDtSourceName(int userId);

    String getDtSourceName(String dataSourceName);

    String getDtSourceName2(@Param("dtSourceName")String dtSourceName, @Param("id") int id);
}
