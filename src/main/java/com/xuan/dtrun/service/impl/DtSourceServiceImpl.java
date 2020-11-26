package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.dao.DtSourceDao;
import com.xuan.dtrun.entity.DtSourceEntity;
import com.xuan.dtrun.service.DtSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DtSourceServiceImpl implements DtSourceService {

    @Autowired
    private DtSourceDao dtSourceDao;

    @Override
    public void create(DtSourceEntity dtSourceEntity) {
        dtSourceDao.create(dtSourceEntity);
    }

    @Override
    public DtSourceEntity getDtSourceById(int id) {
        return dtSourceDao.getDtSourceById(id);
    }

    @Override
    public List<DtSourceEntity> findAll(int userId) {
        return dtSourceDao.findAll(userId);
    }

    @Override
    public void delete(Integer id) {
        dtSourceDao.delete(id);
    }


}
