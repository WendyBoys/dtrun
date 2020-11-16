package com.xuan.dtrun.service;

import com.xuan.dtrun.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserService {

    User login(String account, String password);

}
