package com.xuan.dtrun.mapper;

import com.xuan.dtrun.entity.RegisterCode;
import com.xuan.dtrun.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    User login(@Param("account")String account, @Param("password")String password);

    int register(User user);

    int isRegister(String account);

    User findUserById(int id);

    void modifyPassword(@Param("newPassword") String newPassword, @Param("id") Integer id);

    RegisterCode verifyRegisterCode(String registerCode);

    void updateRegisterCodeStatus(Integer id);
}
