package com.xuan.dtrun.dao;

import com.xuan.dtrun.entity.RegisterCode;
import com.xuan.dtrun.entity.User;

public interface UserDao {
    User login(String account, String password);

    int save(User user);

    User findUserById(int id);

    void modifyPassword(String newPassword,Integer id);

     RegisterCode findRegisterCode(Integer registerCode);

     void setRegisterCode(Integer i,Integer id);
}
