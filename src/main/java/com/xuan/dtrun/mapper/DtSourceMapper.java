package com.xuan.dtrun.mapper;

import com.xuan.dtrun.entity.DtSourceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface DtSourceMapper {
    void create(DtSourceEntity dtSourceEntity);
}
