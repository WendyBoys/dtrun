package com.xuan.dtrun.dao.impl;

import com.xuan.dtrun.dao.UserDao;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String account, String password) {
        return userMapper.login(account, password);
    }
}
