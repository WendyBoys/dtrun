package com.xuan.dtrun.controller;


import com.alibaba.fastjson.JSONObject;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.MoveTaskEntity;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.service.MoveTaskService;
import com.xuan.dtrun.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/movetask")
public class MoveTaskController {

    @Autowired
    private MoveTaskService moveTaskService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
        try {
            User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
            if (user == null) {
                return new CommonResult(200, MessageEnum.FAIL, DataEnum.CREATEFAIL);
            } else {
                String srcId = jsonObject.getString("srcId");
                String srcBucket = jsonObject.getString("srcBucket");
                String desId = jsonObject.getString("desId");
                String desBucket = jsonObject.getString("desBucket");
                String allMove = jsonObject.getString("allMove");
                String taskName = jsonObject.getString("taskName");
                Map map = new HashMap<>();
                map.put("srcId", srcId);
                map.put("srcBucket", srcBucket);
                map.put("desId", desId);
                map.put("desBucket", desBucket);
                map.put("allMove", allMove);
                String taskJson = new JSONObject(map).toJSONString();
                MoveTaskEntity moveTaskEntity = new MoveTaskEntity(taskName, taskJson, "READY", new Date().toLocaleString(), user.getId());
                moveTaskService.create(moveTaskEntity);
                return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.CREATESUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.CREATEFAIL);
        }
    }

}
