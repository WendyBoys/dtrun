package com.xuan.dtrun.service;

import com.xuan.dtrun.entity.ResultEntity;

import java.util.List;

public interface ResultService {

    void create(ResultEntity resultEntity);

    List<ResultEntity> getResultById(Integer id);


}
