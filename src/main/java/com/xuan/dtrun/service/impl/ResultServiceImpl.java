package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.entity.ResultEntity;
import com.xuan.dtrun.mapper.ResultMapper;
import com.xuan.dtrun.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultMapper resultMapper;

    @Override
    public void create(ResultEntity resultEntity) {
        resultMapper.create(resultEntity);
    }

    @Override
    public List<ResultEntity> getResultById(Integer id) {
        return resultMapper.getResultById(id);
    }
}
