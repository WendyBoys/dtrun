package com.xuan.dtrun.dao.impl;

import com.xuan.dtrun.dao.DtSourceDao;
import com.xuan.dtrun.entity.DtSourceEntity;
import com.xuan.dtrun.mapper.DtSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DtSourceDaoImpl implements DtSourceDao {
    @Autowired
    private DtSourceMapper dtSourceMapper;

    @Override
    public void create(DtSourceEntity dtSourceEntity) {
        dtSourceMapper.create(dtSourceEntity);
    }
}
