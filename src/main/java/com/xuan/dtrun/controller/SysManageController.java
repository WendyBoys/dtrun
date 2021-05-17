package com.xuan.dtrun.controller;

import com.alibaba.fastjson.JSONObject;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.LogEntity;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.entity.WhiteListEntity;
import com.xuan.dtrun.mapper.WhiteListMapper;
import com.xuan.dtrun.service.LogService;
import com.xuan.dtrun.service.WhiteListService;
import com.xuan.dtrun.utils.DateUtils;
import com.xuan.dtrun.utils.TokenUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/sysmanage")
public class SysManageController {

    @Autowired
    private WhiteListService whiteListService;

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
    public CommonResult getColorById(@RequestBody JSONObject json, @RequestHeader("token") String token) {
        User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
        if (user != null) {
            Integer id = user.getId();
            String color = json.getString("key");
            List<LogEntity> colorById;
            if ("all".equals(color)) {
                colorById = logService.getLogsById(id);
            } else {
                colorById = logService.getColorById(color, id);
            }
            return new CommonResult(200, MessageEnum.SUCCESS, colorById);
        } else {
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
        }
    }

    @GetMapping(value = "/findAll", produces = "application/json;charset=utf-8")
    public  CommonResult  findAll(@RequestHeader("token") String token){
        User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
        int uid = user.getId();
        List<WhiteListEntity> whiteListEntities = whiteListService.findAll(uid);
        return new CommonResult(200,MessageEnum.SUCCESS,whiteListEntities);
    }

    @PostMapping(value = "/createWhiteList",produces = "application/json;charset=utf-8")
    public  CommonResult createWhiteList(@RequestBody WhiteListEntity whiteListEntity,@RequestHeader("token") String token){
        User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
        String newIp = whiteListEntity.getIp();
        String oldIp = whiteListService.isCreate(newIp);
        if (oldIp==null) {
            whiteListEntity.setCreateTime(DateUtils.getDate());
            whiteListEntity.setIp(newIp);
            whiteListEntity.setUid(Integer.toString(user.getId()));
            whiteListService.createWhiteList(whiteListEntity);
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.CREATESUCCESS);
        }else if (newIp.equals(oldIp)){
            return new CommonResult(200,MessageEnum.FAIL,DataEnum.CREATEFAIL);
        }else {
            return new CommonResult(200,MessageEnum.FAIL,DataEnum.CREATEFAIL);
        }
    }

    @PostMapping(value = "/updateWhiteList", produces = "application/json;charset=utf-8")
    public CommonResult updateWhiteList(@RequestBody JSONObject json){
        String jsonString = json.getString("id");
        String ip = json.getString("ip");
        String oldIp = whiteListService.isCreate(ip);
        if (oldIp==null){
            int id = Integer.parseInt(jsonString);
            whiteListService.updateIp(id,ip);
            return new CommonResult(200,MessageEnum.SUCCESS,DataEnum.MODIFYSUCCESS);
        }else if (ip.equals(oldIp)){
            return new CommonResult(200,MessageEnum.CREATEREPEAT,DataEnum.MODIFYFAIL);
        }else {
            return new CommonResult(200,MessageEnum.FAIL,DataEnum.MODIFYFAIL);
        }

    }

    @PostMapping(value = "/getWhiteListById", produces = "application/json;charset=utf-8")
    public CommonResult getWhiteListById(@RequestBody JSONObject json){
        try {
            String jsonString = json.getString("id");
            int id = Integer.parseInt(jsonString);
            WhiteListEntity whiteListEntity = whiteListService.getWhiteListById(id);
            return new CommonResult(200, MessageEnum.SUCCESS, whiteListEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
        }
    }

    @PostMapping(value = "/deleteWhiteList",produces = "application/json;charset=utf-8")
    public CommonResult deleteWhiteList(@RequestBody JSONObject json){
        Object[] ids = json.getJSONObject("data").getJSONArray("id").toArray();
        whiteListService.deleteWhiteList(ids);
        return new CommonResult(200,MessageEnum.SUCCESS,DataEnum.DELETESUCCESS);
    }

}
