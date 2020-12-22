package com.xuan.dtrun.dao;

import com.xuan.dtrun.entity.RegisterCode;
import com.xuan.dtrun.entity.User;

public interface UserDao {
    User login(String account, String password);

    int save(User user);

    User findUserById(int id);

    void modifyPassword(String newPassword,Integer id);


     RegisterCode verifyRegisterCode(String registerCode);

     void updateRegisterCodeStatus(int i, Integer id);


}
