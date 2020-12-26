package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.entity.RegisterCode;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.mapper.UserMapper;
import com.xuan.dtrun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String account, String password) {
        return userMapper.login(account, password);
    }

    @Override
    public int register(User user) {
        user.setCreateTime(new Date());
        return userMapper.register(user);
    }

    @Override
    public int isRegister(String account) {
        return userMapper.isRegister(account);
    }

    @Override
    public User findUserById(int id) {
        return userMapper.findUserById(id);
    }

    @Override
    public void modifyPassword(String newPassword, Integer id) {
        userMapper.modifyPassword(newPassword, id);
    }

    @Override
    public RegisterCode verifyRegisterCode(String registerCode) {
        return userMapper.verifyRegisterCode(registerCode);
    }

    @Override
    public void updateRegisterCodeStatus(Integer id) {
        userMapper.updateRegisterCodeStatus(id);
    }


}
