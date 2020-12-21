package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.dao.UserDao;
import com.xuan.dtrun.entity.RegisterCode;
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
    public int save(User user) {
        return userDao.save(user);
    }

    @Override
    public void update(String oldPassword, String newPassword) {

    }

    @Override
    public User findUserById(int id) {
        return userDao.findUserById(id);
    }

    @Override
    public void modifyPassword(String newPassword,Integer id) {
        userDao.modifyPassword(newPassword,id);
    }

    @Override
    public RegisterCode findRegisterCode(Integer registerCode) {
        return userDao.findRegisterCode(registerCode);
    }

    @Override
    public void setRegisterCode(Integer i, Integer id) {
        userDao.setRegisterCode(i,id);
    }



}
