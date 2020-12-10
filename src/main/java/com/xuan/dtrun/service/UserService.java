package com.xuan.dtrun.service;

import com.xuan.dtrun.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserService {

    User login(String account, String password);

    void save(User user);

    void update(String oldPassword ,String newPassword);

    User findUserById(int id);

    void modifyPassword(String newPassword,Integer id);
}
