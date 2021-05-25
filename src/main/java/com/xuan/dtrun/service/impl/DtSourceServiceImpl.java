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
        List<DtSourceEntity> dtSourceEntityList = dtSourceMapper.findAll(userId);
        dtSourceEntityList.forEach(dtSourceEntity -> {
            dtSourceEntity.setKey(dtSourceEntity.getId());
        });
        return dtSourceEntityList;
    }

    @Override
    public void delete(Object[] ids) {
        dtSourceMapper.delete(ids);
    }

    @Override
    public void update(DtSourceEntity dtSourceEntity) {
        dtSourceMapper.update(dtSourceEntity);
    }

    @Override
    public List<DtSourceEntity> getAllDtSourceName(int userId) {
        return dtSourceMapper.getAllDtSourceName(userId);
    }

    @Override
    public String getDtSourceName(String dataSourceName) {
        return dtSourceMapper.getDtSourceName(dataSourceName);
    }

    @Override
    public String getDtSourceName2(String dataSourceName,int id) {
        return dtSourceMapper.getDtSourceName2(dataSourceName,id);
    }


}
