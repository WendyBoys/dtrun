package com.xuan.dtrun.mapper;

import com.xuan.dtrun.entity.ResultEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ResultMapper {

    void create(ResultEntity resultEntity);

    List<ResultEntity> getResultById(Integer id);
}
