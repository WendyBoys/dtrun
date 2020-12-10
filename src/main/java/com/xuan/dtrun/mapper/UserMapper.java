package com.xuan.dtrun.mapper;

import com.xuan.dtrun.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    User login(String account, String password);

    int save(User user);

    User findUserById(int id);

    void modifyPassword(String newPassword,Integer id);
}
