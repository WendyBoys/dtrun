package com.xuan.dtrun.service;

import com.xuan.dtrun.entity.RegisterCode;
import com.xuan.dtrun.entity.User;


public interface UserService {

    User login(String account, String password);

    int register(User user);

    int isRegister(String account);

    User findUserById(int id);

    void modifyPassword(String newPassword,Integer id);

    RegisterCode verifyRegisterCode(String registerCode);

    void updateRegisterCodeStatus(Integer id);


}
