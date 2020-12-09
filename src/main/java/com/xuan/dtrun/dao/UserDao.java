package com.xuan.dtrun.dao;

import com.xuan.dtrun.entity.User;

public interface UserDao {
    User login(String account, String password);



    int save(User user);
}
