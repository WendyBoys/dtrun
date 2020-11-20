package com.xuan.dtrun.mapper;

import com.xuan.dtrun.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    User login(String account, String password);

    void save(User user);
}
