package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.entity.DtSourceEntity;
import com.xuan.dtrun.mapper.DtSourceMapper;
import com.xuan.dtrun.service.DtSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DtSourceServiceImpl implements DtSourceService {

    @Autowired
    private DtSourceMapper dtSourceMapper;

    @Override
    public void create(DtSourceEntity dtSourceEntity) {
        dtSourceMapper.create(dtSourceEntity);
    }

    @Override
    public DtSourceEntity getDtSourceById(int id) {
        return dtSourceMapper.getDtSourceById(id);
    }

    @Override
    public List<DtSourceEntity> findAll(int userId) {
        return dtSourceMapper.findAll(userId);
    }

    @Override
    public void delete(Integer id) {
        dtSourceMapper.delete(id);
    }

    @Override
    public void update(DtSourceEntity dtSourceEntity) {
        dtSourceMapper.update(dtSourceEntity);
    }


}
