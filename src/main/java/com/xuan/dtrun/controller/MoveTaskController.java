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
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/movetask")
public class MoveTaskController {

    @Autowired
    private MoveTaskService moveTaskService;

    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping(value = "/findAll", produces = "application/json;charset=utf-8")
    public CommonResult findAll(@RequestHeader("token") String token) {
        try {
            User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
            List<MoveTaskEntity> moveTaskEntityList = moveTaskService.findAll(user.getId());
            return new CommonResult(200, MessageEnum.SUCCESS, moveTaskEntityList);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
        }
    }


    @GetMapping(value = "/getMoveTaskById", produces = "application/json;charset=utf-8")
    public CommonResult getMoveTaskById(int id) {
        try {
            MoveTaskEntity moveTaskEntity = moveTaskService.getMoveTaskById(id);
            return new CommonResult(200, MessageEnum.SUCCESS, moveTaskEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
        }
    }


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

    @PostMapping(value = "/update")
    public CommonResult update(@RequestBody JSONObject jsonObject) {
        try {
            int id = Integer.parseInt(jsonObject.getString("id"));
            String srcId = jsonObject.getString("srcId");
            String srcBucket = jsonObject.getString("srcBucket");
            String desId = jsonObject.getString("desId");
            String desBucket = jsonObject.getString("desBucket");
            String allMove = jsonObject.getString("allMove");
            String taskName = jsonObject.getString("taskName");
            JSONObject json = new JSONObject();
            json.put("srcId", srcId);
            json.put("srcBucket", srcBucket);
            json.put("desId", desId);
            json.put("desBucket", desBucket);
            json.put("allMove", allMove);
            String taskJson = json.toJSONString();
            MoveTaskEntity moveTaskEntity = new MoveTaskEntity(id, taskName, taskJson);
            moveTaskService.update(moveTaskEntity);
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.MODIFYSUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.MODIFYFAIL);
        }
    }

    @DeleteMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public CommonResult delete(@RequestBody JSONObject jsonObject) {
        try {
            Object[] ids = jsonObject.getJSONArray("id").toArray();
            moveTaskService.delete(ids);
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.DELETESUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.DELETEFAIL);
        }
    }


    @PostMapping(value = "/run", produces = "application/json;charset=utf-8")
    public CommonResult run(@RequestBody JSONObject jsonObject) {
        try {
            Integer id = jsonObject.getInteger("id");
            MoveTaskEntity moveTaskById = moveTaskService.getMoveTaskById(id);
            //执行核心迁移
            moveTaskService.updateStatus(id, "RUNNING");
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.RUNSUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.RUNFAIL);
        }
    }

    @PostMapping(value = "/quit", produces = "application/json;charset=utf-8")
    public CommonResult quit(@RequestBody JSONObject jsonObject) {
        try {
            Integer id = jsonObject.getInteger("id");
            moveTaskService.updateStatus(id, "QUIT");
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.QUITSUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUITFAIL);
        }
    }
}
