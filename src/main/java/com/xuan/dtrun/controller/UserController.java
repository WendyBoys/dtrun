package com.xuan.dtrun.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.RegisterCode;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.service.UserService;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    public static final String SALT = "DTRUN";

    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    public CommonResult login(@RequestBody JSONObject json) {
        String account = json.getString("account");
        String password = json.getString("password");
        if (!StringUtils.isEmpty(account) && !StringUtils.isEmpty(password)) {
            User user = userService.login(account, password);
            if (user != null) {
                if (user.getIsUse() == 1) {
                    String token = TokenUtils.token(account, password);
                    redisTemplate.opsForValue().set(TokenUtils.md5Token(token), user, 7, TimeUnit.DAYS);
                    return new CommonResult(200, MessageEnum.SUCCESS, token);
                } else {
                    return new CommonResult(200, MessageEnum.LOGINREFUSE, DataEnum.LOGINREFUSE);
                }
            }
        }
        return new CommonResult(200, MessageEnum.FAIL, DataEnum.LOGINERROR);
    }


    @PostMapping(value = "/register", produces = "application/json;charset=utf-8")
    public CommonResult save(@RequestBody User user) {
        try {
            Integer registerCode =user.getRegisterCode();
            RegisterCode registercode= userService.findRegisterCode(registerCode);
            Integer isUse = registercode.getIsUse();
            Integer value = registercode.getValue();
            if (registerCode==value ||  isUse==1) {
                user.setIconUrl("https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/icon.png");
                user.setUsername("无名氏");
                userService.save(user);
                userService.setRegisterCode(0,registercode.getId());
                return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.REGISTERSUCCESS);
            }else {
                return new CommonResult(200, MessageEnum.FAIL, DataEnum.REGISTERFAIL);
            }
        }catch (Exception e){
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.REGISTERMISS);
        }

    }


    @GetMapping(value = "/getCurrentUser", produces = "application/json;charset=utf-8")
    public CommonResult save(@RequestHeader("token") String token) {
        User currentUser = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
        if (currentUser != null) {
            return new CommonResult(200, MessageEnum.SUCCESS, currentUser);
        }
        return new CommonResult(200, MessageEnum.LOGINEXPIRE, currentUser);
    }

    @PostMapping(value = "/modifyPassword", produces = "application/json;charset=utf-8")
    public CommonResult save(@RequestBody JSONObject Json, @RequestHeader("token") String token) {
            String oldPassword = Json.getString("oldPassword");
            String newPassword = Json.getString("newPassword");
            User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
            int id = user.getId();
            User userById = userService.findUserById(id);
            if (!StringUtils.isEmpty(userById.getPassword())||!StringUtils.isEmpty(oldPassword)) {
                if (userById.getPassword().equals(oldPassword)) {
                    userService.modifyPassword(newPassword,id);
                    return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.MODIFYSUCCESS);
                }
                else
                {
                    return new CommonResult(200, MessageEnum.FAIL, DataEnum.MODIFYFAIL);
                }
            }
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.MODIFYFAIL);
    }

}