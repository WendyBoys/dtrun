package com.xuan.dtrun.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.region.Region;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.DtSourceEntity;
import com.xuan.dtrun.entity.FileMessage;
import com.xuan.dtrun.entity.MoveTaskEntity;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.service.DtSourceService;
import com.xuan.dtrun.service.MoveTaskService;
import com.xuan.dtrun.upload.CosUpload;
import com.xuan.dtrun.upload.CosUploader;
import com.xuan.dtrun.upload.OssUpload;
import com.xuan.dtrun.utils.TokenUtils;
import jdk.nashorn.internal.ir.SwitchNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;


@RestController
@RequestMapping(value = "/movetask")
public class MoveTaskController {

    @Autowired
    private DtSourceService dtSourceService;

    @Autowired
    private MoveTaskService moveTaskService;

    @Autowired
    private RedisTemplate redisTemplate;

    private Logger logger = LoggerFactory.getLogger(MoveTaskController.class);


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
            COSClient srcCosClient = null;
            OSS srcOssClient = null;
            List<FileMessage> fileMessageList = new ArrayList<>();
            ThreadLocal<InputStream> threadLocal = new ThreadLocal<>();
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
            String srcDtSourceType = srcDtSourceEntity.getDtSourceType();
            JSONObject srcEntity = JSON.parseObject(srcDtSourceEntity.getDtSourceJson());
            //获取源端文件信息
            switch (srcDtSourceType) {
                case "cos": {
                    srcCosClient = new COSClient(new BasicCOSCredentials(srcEntity.getString("accessKey"), srcEntity.getString("accessSecret")),
                            new ClientConfig(new Region(srcEntity.getString("region"))));
                    com.qcloud.cos.model.ListObjectsRequest listObjectsRequest = new com.qcloud.cos.model.ListObjectsRequest();
                    listObjectsRequest.setBucketName(srcBucket);
                    listObjectsRequest.setMaxKeys(1000);
                    com.qcloud.cos.model.ObjectListing objectListing = null;
                    do {
                        try {
                            objectListing = srcCosClient.listObjects(listObjectsRequest);
                        } catch (CosClientException e) {
                            e.printStackTrace();
                        }
                        List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
                        for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                            fileMessageList.add(new FileMessage(cosObjectSummary.getKey(), cosObjectSummary.getSize()));
                        }
                        String nextMarker = objectListing.getNextMarker();
                        listObjectsRequest.setMarker(nextMarker);
                    } while (objectListing.isTruncated());
                    break;
                }
                case "oss": {
                    srcOssClient = new OSSClientBuilder().build(srcEntity.getString("region"), srcEntity.getString("accessKey"), srcEntity.getString("accessSecret"));
                    com.aliyun.oss.model.ObjectListing objectListing = srcOssClient.listObjects(srcBucket);
                    for (OSSObjectSummary ossObjectSummary : objectListing.getObjectSummaries()) {
                        fileMessageList.add(new FileMessage(ossObjectSummary.getKey(), ossObjectSummary.getSize()));
                    }
                    break;
                }
                default: {
                    throw new RuntimeException("不支持的数据源类型: " + srcDtSourceType);
                }
            }

            String desDtSourceType = desDtSourceEntity.getDtSourceType();
            if ("oss".equals(desDtSourceType)) {
                JSONObject desEntity = JSON.parseObject(desDtSourceEntity.getDtSourceJson());
                COSClient finalCosClient = srcCosClient;
                OSS finalOssClient = srcOssClient;
                new Thread(() -> {
                    try {
                        OSS ossClient = new OSSClientBuilder().build(desEntity.getString("region"), desEntity.getString("accessKey"), desEntity.getString("accessSecret"));
                        logger.info("启动分片线程");
                        long l = System.currentTimeMillis();
                        for (FileMessage fileMessage : fileMessageList) {
                            switch (srcDtSourceType) {
                                case "cos": {
                                    GetObjectRequest getObjectRequest = new GetObjectRequest(srcBucket, fileMessage.getFileName());
                                    COSObject cosObject = finalCosClient.getObject(getObjectRequest);
                                    threadLocal.set(cosObject.getObjectContent());
                                    break;
                                }
                                case "oss": {
                                    com.aliyun.oss.model.GetObjectRequest getObjectRequest = new com.aliyun.oss.model.GetObjectRequest(srcBucket, fileMessage.getFileName());
                                    OSSObject ossObject = finalOssClient.getObject(getObjectRequest);
                                    BufferedInputStream bufferedInputStream = new BufferedInputStream(ossObject.getObjectContent());
                                    threadLocal.set(bufferedInputStream);
                                    break;
                                }
                                default: {
                                    throw new RuntimeException("不支持的数据源类型: " + srcDtSourceType);
                                }
                            }
                            OssUpload ossUpload = new OssUpload(taskJson.getString("desBucket"), fileMessage.getFileName(), ossClient, threadLocal.get(), fileMessage.getFileLength(), 50 * 1024 * 1024);
                            ossUpload.upload();
                        }
                        long l1 = System.currentTimeMillis();
                        System.out.println("总耗时" + (l1 - l) / 1000f);
                        moveTaskService.updateStatus(id, "FINISH");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (threadLocal.get() != null) {
                                threadLocal.get().close();
                            }
                        } catch (IOException e) {
                            logger.info("关闭" + srcDtSourceType + "端输入流异常");
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if ("cos".equals(desDtSourceType)) {
                JSONObject desEntity = JSON.parseObject(desDtSourceEntity.getDtSourceJson());
                COSClient finalCosClient = srcCosClient;
                OSS finalOssClient = srcOssClient;
                new Thread(() -> {
                    try {
                        COSClient cosClient = new COSClient(new BasicCOSCredentials(desEntity.getString("accessKey"), desEntity.getString("accessSecret")),
                                new ClientConfig(new Region(desEntity.getString("region"))));
                        logger.info("启动分片线程");
                        long l = System.currentTimeMillis();
                        for (FileMessage fileMessage : fileMessageList) {
                            switch (srcDtSourceType) {
                                case "cos": {
                                    GetObjectRequest getObjectRequest = new GetObjectRequest(srcBucket, fileMessage.getFileName());
                                    COSObject cosObject = finalCosClient.getObject(getObjectRequest);
                                    threadLocal.set(cosObject.getObjectContent());
                                    break;
                                }
                                case "oss": {
                                    com.aliyun.oss.model.GetObjectRequest getObjectRequest = new com.aliyun.oss.model.GetObjectRequest(srcBucket, fileMessage.getFileName());
                                    OSSObject ossObject = finalOssClient.getObject(getObjectRequest);
                                    byte[] buf = new byte[4096];
                                    int len;
                                    BufferedInputStream bufferedInputStream = new BufferedInputStream(ossObject.getObjectContent());
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    while ((len = bufferedInputStream.read(buf)) > -1) {
                                        byteArrayOutputStream.write(buf, 0, len);
                                    }
                                    threadLocal.set(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                                    break;
                                }
                                default: {
                                    throw new RuntimeException("不支持的数据源类型: " + srcDtSourceType);
                                }
                            }
                            CosUpload cosUpload = new CosUpload(taskJson.getString("desBucket"), fileMessage.getFileName(), cosClient, threadLocal.get(), fileMessage.getFileLength(), 50 * 1024 * 1024);
                            cosUpload.upload();
                        }
                        cosClient.shutdown();
                        long l1 = System.currentTimeMillis();
                        System.out.println("总耗时" + (l1 - l) / 1000f);
                        moveTaskService.updateStatus(id, "FINISH");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (threadLocal.get() != null) {
                                threadLocal.get().close();
                            }
                            if (finalCosClient != null) {
                                finalCosClient.shutdown();
                            }

                        } catch (IOException e) {
                            logger.info("关闭" + srcDtSourceType + "端输入流异常");
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
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
