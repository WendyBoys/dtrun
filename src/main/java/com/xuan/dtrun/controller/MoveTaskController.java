package com.xuan.dtrun.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.region.Region;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.DtSourceEntity;
import com.xuan.dtrun.entity.MoveTaskEntity;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.service.DtSourceService;
import com.xuan.dtrun.service.MoveTaskService;
import com.xuan.dtrun.upload.OssUpload;
import com.xuan.dtrun.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/movetask")
public class MoveTaskController {

    @Autowired
    private DtSourceService dtSourceService;

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
            COSClient cosClient = null;
            OSS ossClient = null;
            Integer id = jsonObject.getInteger("id");
            moveTaskService.updateStatus(id, "RUNNING");
            MoveTaskEntity moveTaskById = moveTaskService.getMoveTaskById(id);

            //执行核心迁移
            JSONObject taskJson = JSON.parseObject(moveTaskById.getTaskJson());
            Integer srcId = taskJson.getInteger("srcId");
            DtSourceEntity srcDtSourceEntity = dtSourceService.getDtSourceById(srcId);
            Integer desId = taskJson.getInteger("desId");
            DtSourceEntity desDtSourceEntity = dtSourceService.getDtSourceById(desId);
            String srcBucket = taskJson.getString("srcBucket");
            String dtSourceType = srcDtSourceEntity.getDtSourceType();
            JSONObject srcEntity = JSON.parseObject(srcDtSourceEntity.getDtSourceJson());
            JSONObject desEntity = JSON.parseObject(desDtSourceEntity.getDtSourceJson());
            cosClient = new COSClient(new BasicCOSCredentials(srcEntity.getString("accessKey"), srcEntity.getString("accessSecret")),
                    new ClientConfig(new Region(srcEntity.getString("region"))));
            ossClient = new OSSClientBuilder().build(desEntity.getString("region"), desEntity.getString("accessKey"), desEntity.getString("accessSecret"));

            String bucketName = srcBucket;
            com.qcloud.cos.model.ListObjectsRequest listObjectsRequest = new com.qcloud.cos.model.ListObjectsRequest();
            listObjectsRequest.setBucketName(bucketName);
            listObjectsRequest.setMaxKeys(1000);
            com.qcloud.cos.model.ObjectListing objectListing = null;
            do {
                try {
                    objectListing = cosClient.listObjects(listObjectsRequest);
                } catch (CosServiceException e) {
                    e.printStackTrace();
                } catch (CosClientException e) {
                    e.printStackTrace();
                }
                List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
                COSClient finalCosClient = cosClient;
                OSS finalOssClient = ossClient;
                new Thread(() -> {
                    try {
                        for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                            String key = cosObjectSummary.getKey();
                            long fileSize = cosObjectSummary.getSize();
                            com.qcloud.cos.model.GetObjectRequest getObjectRequest = new com.qcloud.cos.model.GetObjectRequest(bucketName, key);
                            COSObject cosObject = finalCosClient.getObject(getObjectRequest);
                            COSObjectInputStream cosObjectInput = cosObject.getObjectContent();
                            OssUpload ossUpload = new OssUpload(taskJson.getString("desBucket"), key, finalOssClient, cosObjectInput, fileSize, 10 * 1024 * 1024);
                            ossUpload.upload();
                            cosObjectInput.close();
                        }
                        moveTaskService.updateStatus(id, "FINISH");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                String nextMarker = objectListing.getNextMarker();
                listObjectsRequest.setMarker(nextMarker);
            } while (objectListing.isTruncated());
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
