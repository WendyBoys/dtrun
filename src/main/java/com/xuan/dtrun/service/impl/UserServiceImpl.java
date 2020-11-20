package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.dao.UserDao;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User login(String account, String password) {
        return userDao.login(account, password);
    }

    @Override
    public void save(User user) {

         userDao.save(user);
    }
}
