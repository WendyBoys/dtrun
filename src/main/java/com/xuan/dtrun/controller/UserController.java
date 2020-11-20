package com.xuan.dtrun.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.service.UserService;
import com.xuan.dtrun.common.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    public CommonResult login(@RequestBody JSONObject json) {
        String account = json.getString("account");
        String password = json.getString("password");
        if (!StringUtils.isEmpty(account) && !StringUtils.isEmpty(password)) {
            User user = userService.login(account, password);
            if (user != null) {
                if (user.getIsUse() == 1) {
                    return new CommonResult(200, MessageEnum.SUCCESS, user);
                } else {
                    return new CommonResult(200, MessageEnum.LOGINREFUSE, DataEnum.LOGINREFUSE);
                }
            }
        }
        return new CommonResult(500, MessageEnum.FAIL, DataEnum.LOGINERROR);
    }


    @PostMapping(value = "/save",produces = "application/json;charset=utf-8")
    public String  save(@RequestBody  User user){
        userService.save(user);
        return "redirect:login.html";
    }

}
