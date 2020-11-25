package com.xuan.dtrun.service;

import com.xuan.dtrun.entity.DtSourceEntity;

import java.util.List;

public interface DtSourceService {

    void create(DtSourceEntity dtSourceEntity);

     List<DtSourceEntity> findAll(Integer integer);


}
