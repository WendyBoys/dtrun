package com.xuan.dtrun.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.algorithms.Algorithm;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.service.UserService;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.utils.TokenUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

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
                    String md5Token = DigestUtils.md5Hex(TokenUtils.token(account, password) + SALT);
                    redisTemplate.opsForValue().set(md5Token, user, 7, TimeUnit.DAYS);
                    return new CommonResult(200, MessageEnum.SUCCESS, md5Token);
                } else {
                    return new CommonResult(200, MessageEnum.LOGINREFUSE, DataEnum.LOGINREFUSE);
                }
            }
        }
        return new CommonResult(200, MessageEnum.FAIL, DataEnum.LOGINERROR);
    }


    @PostMapping(value = "/register", produces = "application/json;charset=utf-8")
    public CommonResult save(@RequestBody User user) {
        userService.save(user);
        return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.REGISTERSUCCESS);
    }


    @GetMapping(value = "/getCurrentUser", produces = "application/json;charset=utf-8")
    public CommonResult save(String token) {
        User currentUser = (User) redisTemplate.opsForValue().get(token);
        if(currentUser!=null)
        {
            return new CommonResult(200, MessageEnum.SUCCESS, currentUser);
        }
        return new CommonResult(200, MessageEnum.LOGINEXPIRE, currentUser);
    }


}
