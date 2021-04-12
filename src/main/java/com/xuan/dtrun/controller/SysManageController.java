package com.xuan.dtrun.controller;

import com.alibaba.fastjson.JSONObject;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.LogEntity;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.service.LogService;
import com.xuan.dtrun.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/sysmanage")
public class SysManageController {

    @Autowired
    private LogService logService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping(value = "/getLogsById", produces = "application/json;charset=utf-8")
    public CommonResult getLogs(@RequestHeader("token") String token) {
        try {
            User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
            if (user != null) {
                List<LogEntity> logsById = logService.getLogsById(user.getId());
                return new CommonResult(200, MessageEnum.SUCCESS, logsById);
            } else {
                return new CommonResult(200, MessageEnum.FAIL, DataEnum.LOGINEXPIRE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
        }
    }

    @PostMapping(value = "/getColorById", produces = "application/json;charset=utf-8")
    public CommonResult getColorById(@RequestBody JSONObject json,@RequestHeader("token") String token){
        User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
        if (user!=null){
            Integer id = user.getId();
            String color = json.getString("key");
            List<LogEntity> colorById = logService.getColorById(color,id);
            return new CommonResult(200,MessageEnum.SUCCESS,colorById);
        }else {
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
        }

    }

}
