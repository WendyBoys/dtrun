package com.xuan.dtrun.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.RegisterCode;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.service.UserService;
import com.xuan.dtrun.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Value("${user.iconUrl}")
    private String iconUrl;

    @Value("${user.userName}")
    private String userName;


    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    public CommonResult login(@RequestBody JSONObject json) {
        String account = json.getString("account");
        String password = json.getString("password");
        if (!StringUtils.isEmpty(account) && !StringUtils.isEmpty(password)) {
            User user = userService.login(account, password);
            if (user != null) {
                //账号未被封禁
                if (user.getIsUse() == 1) {
                    //生成token 放入redis 并返回给前端
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CommonResult register(@RequestBody User user) {
        try {
            String code = user.getRegisterCode();
            RegisterCode registercode = userService.verifyRegisterCode(code);
            if (registercode != null) {
                Integer isUse = registercode.getIsUse();
                //注册码可使用
                if (1 == isUse) {
                    int isRegister = userService.isRegister(user.getAccount());
                    //输入的账号未被注册
                    if (isRegister != 1) {
                        user.setIconUrl(iconUrl);
                        user.setUsername(userName);
                        user.setRegisterCode(code);
                        userService.register(user);
                        userService.updateRegisterCodeStatus(registercode.getId());
                        return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.REGISTERSUCCESS);
                    } else {
                        //输入的的账号已被注册
                        return new CommonResult(200, MessageEnum.FAIL, DataEnum.REGISTERALREADY);
                    }
                } else {
                    //注册码已被使用过 失效
                    return new CommonResult(200, MessageEnum.FAIL, DataEnum.REGISTERCODEEXPIRE);
                }
            } else {
                //注册码错误
                return new CommonResult(200, MessageEnum.FAIL, DataEnum.REGISTERCODEERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.REGISTERFAIL);
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
        if (user != null) {
            int id = user.getId();
            User userById = userService.findUserById(id);
            if (!StringUtils.isEmpty(userById.getPassword()) || !StringUtils.isEmpty(oldPassword)) {
                if (userById.getPassword().equals(oldPassword)) {
                    userService.modifyPassword(newPassword, id);
                    return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.MODIFYSUCCESS);
                } else {
                    return new CommonResult(200, MessageEnum.FAIL, DataEnum.MODIFYFAIL);
                }
            } else {
                return new CommonResult(200, MessageEnum.FAIL, DataEnum.MODIFYFAIL);
            }
        } else {
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.LOGINEXPIRE);
        }
    }
}