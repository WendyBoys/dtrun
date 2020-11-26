package com.xuan.dtrun.dao;

import com.xuan.dtrun.entity.DtSourceEntity;

import java.util.List;

public interface DtSourceDao {
    void create(DtSourceEntity dtSourceEntity);

    DtSourceEntity getDtSourceById(int id);

     List<DtSourceEntity> findAll(int userId);

}
