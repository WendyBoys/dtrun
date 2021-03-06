package com.xuan.dtrun.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObjectSummary;
import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.region.Region;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.*;
import com.xuan.dtrun.service.DtSourceService;
import com.xuan.dtrun.service.LogService;
import com.xuan.dtrun.service.MoveTaskService;
import com.xuan.dtrun.service.ResultService;
import com.xuan.dtrun.thread.CosThread;
import com.xuan.dtrun.thread.ObsThread;
import com.xuan.dtrun.thread.OssThread;
import com.xuan.dtrun.utils.ClientIp;
import com.xuan.dtrun.utils.DateUtils;
import com.xuan.dtrun.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping(value = "/movetask")
public class MoveTaskController {

    @Autowired
    private DtSourceService dtSourceService;

    @Autowired
    private MoveTaskService moveTaskService;

    @Autowired
    private LogService logService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private RedisTemplate redisTemplate;

    private Logger logger = LoggerFactory.getLogger(MoveTaskController.class);


    @GetMapping(value = "/findAll", produces = "application/json;charset=utf-8")
    public CommonResult findAll(@RequestHeader("token") String token) {
        try {
            User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
            if (user != null) {
                List<MoveTaskEntity> moveTaskEntityList = moveTaskService.findAll(user.getId());
                return new CommonResult(200, MessageEnum.SUCCESS, moveTaskEntityList);

            } else {
                return new CommonResult(200, MessageEnum.FAIL, DataEnum.LOGINEXPIRE);
            }

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
                return new CommonResult(200, MessageEnum.FAIL, DataEnum.LOGINEXPIRE);
            } else {
                String srcId = jsonObject.getString("srcId");
                String srcBucket = jsonObject.getString("srcBucket");
                String desId = jsonObject.getString("desId");
                String desBucket = jsonObject.getString("desBucket");
                String allMove = jsonObject.getString("allMove");
                String fileNameStart = jsonObject.getString("fileNameStart");
                String fileNameEnd = jsonObject.getString("fileNameEnd");
                String taskName = jsonObject.getString("taskName");
                String time = jsonObject.getString("time");
                String moveTaskName = moveTaskService.getMoveTaskName(taskName);
                if (moveTaskName == null) {
                    String sendMail = jsonObject.getString("sendMail");
                    String contact;
                    JSONObject json = new JSONObject();
                    json.put("srcId", srcId);
                    json.put("srcBucket", srcBucket);
                    json.put("fileNameStart", fileNameStart);
                    json.put("fileNameEnd", fileNameEnd);
                    json.put("desId", desId);
                    json.put("desBucket", desBucket);
                    json.put("allMove", allMove);
                    json.put("sendMail", sendMail);
                    json.put("time", time);
                    if ("true".equals(sendMail)) {
                        contact = jsonObject.getString("contact");
                        json.put("contact", contact);
                    }
                    String taskJson = json.toJSONString();
                    MoveTaskEntity moveTaskEntity = new MoveTaskEntity(taskName, taskJson, "READY", DateUtils.getDate(), user.getId());
                    moveTaskService.create(moveTaskEntity);
                    return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.CREATESUCCESS);
                } else if (taskName.equals(moveTaskName)) {
                    return new CommonResult(200, MessageEnum.CREATEREPEAT, DataEnum.CREATEFAIL);
                } else {
                    return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.CREATEFAIL);
                }
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
            String fileNameStart = jsonObject.getString("fileNameStart");
            String fileNameEnd = jsonObject.getString("fileNameEnd");
            String desId = jsonObject.getString("desId");
            String desBucket = jsonObject.getString("desBucket");
            String allMove = jsonObject.getString("allMove");
            String taskName = jsonObject.getString("taskName");
            String time = jsonObject.getString("time");
            String moveTaskName = moveTaskService.getMoveTaskName2(taskName, id);
            if (moveTaskName == null) {
                String sendMail = jsonObject.getString("sendMail");
                String contact;
                JSONObject json = new JSONObject();
                json.put("srcId", srcId);
                json.put("srcBucket", srcBucket);
                json.put("fileNameStart", fileNameStart);
                json.put("fileNameEnd", fileNameEnd);
                json.put("desId", desId);
                json.put("desBucket", desBucket);
                json.put("allMove", allMove);
                json.put("sendMail", sendMail);
                json.put("time", time);
                if ("true".equals(sendMail)) {
                    contact = jsonObject.getString("contact");
                    json.put("contact", contact);
                }
                String taskJson = json.toJSONString();
                MoveTaskEntity moveTaskEntity = new MoveTaskEntity(id, taskName, taskJson);
                moveTaskService.update(moveTaskEntity);
                return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.MODIFYSUCCESS);
            } else if (taskName.equals(moveTaskName)) {
                return new CommonResult(200, MessageEnum.CREATEREPEAT, DataEnum.MODIFYFAIL);
            } else {
                return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.MODIFYFAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.MODIFYFAIL);
        }
    }

    @PostMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public CommonResult delete(@RequestBody JSONObject jsonObject, @RequestHeader("token") String token, @ClientIp String ip) {
        try {
            User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
            if (user != null) {
                Object[] ids = jsonObject.getJSONObject("data").getJSONArray("id").toArray();
                moveTaskService.delete(ids);
                logService.create(user.getId(), "删除" + ids.length + "个迁移任务,ip地址为" + ip, DateUtils.getDate(), "red");
                return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.DELETESUCCESS);
            } else {
                return new CommonResult(200, MessageEnum.FAIL, DataEnum.LOGINEXPIRE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.DELETEFAIL);
        }
    }


    @PostMapping(value = "/run", produces = "application/json;charset=utf-8")
    public CommonResult run(@RequestBody JSONObject jsonObject, @ClientIp String ip) {
        Integer id = jsonObject.getInteger("id");
        long startTime = System.currentTimeMillis();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            COSClient srcCosClient = null;
            OSS srcOssClient = null;
            ObsClient srcObsClient = null;
            List<FileMessage> fileMessageList = new ArrayList<>();
            moveTaskService.updateStatus(id, "RUNNING");
            MoveTaskEntity moveTaskById = moveTaskService.getMoveTaskById(id);
            logService.create(moveTaskById.getUid(), "启动迁移任务" + moveTaskById.getTaskName() + ",ip地址为" + ip, DateUtils.getDate(), "blue");

            //执行核心迁移
            JSONObject taskJson = JSON.parseObject(moveTaskById.getTaskJson());
            Integer srcId = taskJson.getInteger("srcId");
            DtSourceEntity srcDtSourceEntity = dtSourceService.getDtSourceById(srcId);
            Integer desId = taskJson.getInteger("desId");
            DtSourceEntity desDtSourceEntity = dtSourceService.getDtSourceById(desId);
            String srcBucket = taskJson.getString("srcBucket");
            String allMove = taskJson.getString("allMove");
            String fileNameStart = taskJson.getString("fileNameStart");
            String fileNameEnd = taskJson.getString("fileNameEnd");
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
                            e.printStackTrace(pw);
                        }
                        List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
                        for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                            if ("false".equals(allMove)) {
                                if (fileNameStart != null && fileNameEnd == null && cosObjectSummary.getKey().startsWith(fileNameStart)) {
                                    fileMessageList.add(new FileMessage(cosObjectSummary.getKey(), cosObjectSummary.getSize()));
                                }
                                if (fileNameStart == null && fileNameEnd != null && cosObjectSummary.getKey().endsWith(fileNameEnd)) {
                                    fileMessageList.add(new FileMessage(cosObjectSummary.getKey(), cosObjectSummary.getSize()));
                                }
                                if (fileNameStart != null && fileNameEnd != null && cosObjectSummary.getKey().endsWith(fileNameEnd) && cosObjectSummary.getKey().endsWith(fileNameEnd)) {
                                    fileMessageList.add(new FileMessage(cosObjectSummary.getKey(), cosObjectSummary.getSize()));
                                }
                            } else {
                                fileMessageList.add(new FileMessage(cosObjectSummary.getKey(), cosObjectSummary.getSize()));
                            }
                        }
                        String nextMarker = objectListing.getNextMarker();
                        listObjectsRequest.setMarker(nextMarker);
                    } while (objectListing.isTruncated());
                    break;
                }
                case "oss": {
                    srcOssClient = new OSSClientBuilder().build(srcEntity.getString("region"), srcEntity.getString("accessKey"), srcEntity.getString("accessSecret"));
                    com.aliyun.oss.model.ObjectListing objectListing = null;
                    try {
                        objectListing = srcOssClient.listObjects(srcBucket);
                    } catch (OSSException e) {
                        e.printStackTrace(pw);
                    }
                    for (OSSObjectSummary ossObjectSummary : objectListing.getObjectSummaries()) {
                        if ("false".equals(allMove)) {
                            if (fileNameStart != null && fileNameEnd == null && ossObjectSummary.getKey().startsWith(fileNameStart)) {
                                fileMessageList.add(new FileMessage(ossObjectSummary.getKey(), ossObjectSummary.getSize()));
                            }
                            if (fileNameStart == null && fileNameEnd != null && ossObjectSummary.getKey().endsWith(fileNameEnd)) {
                                fileMessageList.add(new FileMessage(ossObjectSummary.getKey(), ossObjectSummary.getSize()));
                            }
                            if (fileNameStart != null && fileNameEnd != null && ossObjectSummary.getKey().endsWith(fileNameEnd) && ossObjectSummary.getKey().endsWith(fileNameEnd)) {
                                fileMessageList.add(new FileMessage(ossObjectSummary.getKey(), ossObjectSummary.getSize()));
                            }
                        } else {
                            fileMessageList.add(new FileMessage(ossObjectSummary.getKey(), ossObjectSummary.getSize()));
                        }
                    }
                    break;
                }
                case "obs": {
                    srcObsClient = new ObsClient(srcEntity.getString("accessKey"), srcEntity.getString("accessSecret"), srcEntity.getString("region"));
                    com.obs.services.model.ObjectListing objectListing = null;
                    try {
                        objectListing = srcObsClient.listObjects(srcBucket);
                    } catch (OSSException e) {
                        e.printStackTrace(pw);
                    }
                    for (ObsObject obsObject : objectListing.getObjects()) {
                        if ("false".equals(allMove)) {
                            if (fileNameStart != null && fileNameEnd == null && obsObject.getObjectKey().startsWith(fileNameStart)) {
                                fileMessageList.add(new FileMessage(obsObject.getObjectKey(), obsObject.getMetadata().getContentLength()));
                            }
                            if (fileNameStart == null && fileNameEnd != null && obsObject.getObjectKey().endsWith(fileNameEnd)) {
                                fileMessageList.add(new FileMessage(obsObject.getObjectKey(), obsObject.getMetadata().getContentLength()));
                            }
                            if (fileNameStart != null && fileNameEnd != null && obsObject.getObjectKey().endsWith(fileNameEnd) && obsObject.getObjectKey().endsWith(fileNameEnd)) {
                                fileMessageList.add(new FileMessage(obsObject.getObjectKey(), obsObject.getMetadata().getContentLength()));
                            }
                        } else {
                            fileMessageList.add(new FileMessage(obsObject.getObjectKey(), obsObject.getMetadata().getContentLength()));
                        }

                    }
                    break;
                }
                default: {
                    throw new RuntimeException("不支持的数据源类型: " + srcDtSourceType);
                }
            }
            //迁移文件
            String desDtSourceType = desDtSourceEntity.getDtSourceType();
            String time = taskJson.getString("time");
            if (time != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateTime = null;
                try {
                    dateTime = simpleDateFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Timer timer = new Timer();
                COSClient finalSrcCosClient = srcCosClient;
                OSS finalSrcOssClient = srcOssClient;
                ObsClient finalSrcObsClient = srcObsClient;
                timer.schedule(new TimerTask() {
                    public void run() {
                        if ("oss".equals(desDtSourceType)) {
                            JSONObject desEntity = JSON.parseObject(desDtSourceEntity.getDtSourceJson());
                            new Thread(new OssThread(id, moveTaskById.getTaskName(), desEntity, fileMessageList, srcDtSourceType, srcBucket, finalSrcCosClient, finalSrcOssClient, finalSrcObsClient, taskJson, moveTaskService, resultService), "movetask" + id).start();
                        } else if ("cos".equals(desDtSourceType)) {
                            JSONObject desEntity = JSON.parseObject(desDtSourceEntity.getDtSourceJson());
                            new Thread(new CosThread(id, moveTaskById.getTaskName(), desEntity, fileMessageList, srcDtSourceType, srcBucket, finalSrcCosClient, finalSrcOssClient, finalSrcObsClient, taskJson, moveTaskService, resultService), "movetask" + id).start();
                        } else if ("obs".equals(desDtSourceType)) {
                            JSONObject desEntity = JSON.parseObject(desDtSourceEntity.getDtSourceJson());
                            new Thread(new ObsThread(id, moveTaskById.getTaskName(), desEntity, fileMessageList, srcDtSourceType, srcBucket, finalSrcCosClient, finalSrcOssClient, finalSrcObsClient, taskJson, moveTaskService, resultService), "movetask" + id).start();
                        }
                    }
                }, dateTime);
            } else {
                if ("oss".equals(desDtSourceType)) {
                    JSONObject desEntity = JSON.parseObject(desDtSourceEntity.getDtSourceJson());
                    new Thread(new OssThread(id, moveTaskById.getTaskName(), desEntity, fileMessageList, srcDtSourceType, srcBucket, srcCosClient, srcOssClient, srcObsClient, taskJson, moveTaskService, resultService), "movetask" + id).start();
                } else if ("cos".equals(desDtSourceType)) {
                    JSONObject desEntity = JSON.parseObject(desDtSourceEntity.getDtSourceJson());
                    new Thread(new CosThread(id, moveTaskById.getTaskName(), desEntity, fileMessageList, srcDtSourceType, srcBucket, srcCosClient, srcOssClient, srcObsClient, taskJson, moveTaskService, resultService), "movetask" + id).start();
                } else if ("obs".equals(desDtSourceType)) {
                    JSONObject desEntity = JSON.parseObject(desDtSourceEntity.getDtSourceJson());
                    new Thread(new ObsThread(id, moveTaskById.getTaskName(), desEntity, fileMessageList, srcDtSourceType, srcBucket, srcCosClient, srcOssClient, srcObsClient, taskJson, moveTaskService, resultService), "movetask" + id).start();
                }
            }
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.RUNSUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            long endTime = System.currentTimeMillis();
            String timeConsume = String.valueOf((endTime - startTime) / 1000f);
            moveTaskService.updateStatus(id, "FAIL");
            String errorMsg = sw.toString();
            if (errorMsg == null) {
                e.printStackTrace(pw);
            }
            resultService.create(new ResultEntity(DateUtils.getDate(startTime), DateUtils.getDate(endTime), "FAIL", errorMsg,
                    timeConsume, 0, null, id));
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.RUNFAIL);
        }
    }

    @PostMapping(value = "/quit", produces = "application/json;charset=utf-8")
    @Transactional
    public CommonResult quit(@RequestBody JSONObject jsonObject, @ClientIp String ip) {
        try {
            Integer id = jsonObject.getInteger("id");
            MoveTaskEntity moveTaskById = moveTaskService.getMoveTaskById(id);
            ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
            int noThreads = currentGroup.activeCount();
            Thread[] lstThreads = new Thread[noThreads];
            currentGroup.enumerate(lstThreads);
            for (int i = 0; i < noThreads; i++) {
                String nm = lstThreads[i].getName();
                if (nm.equals("movetask" + id)) {
                    lstThreads[i].interrupt();
                    logger.info("终止id为+" + id + "的迁移任务");
                    logService.create(moveTaskById.getUid(), "取消迁移任务" + moveTaskById.getTaskName() + "的运行,ip地址为" + ip, DateUtils.getDate(), "orange");
                    resultService.create(new ResultEntity(DateUtils.getDate(), DateUtils.getDate(), "QUIT", null,
                            "0", 0, null, id));
                }
            }
            moveTaskService.updateStatus(id, "QUIT");
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.QUITSUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUITFAIL);
        }
    }

    @GetMapping(value = "/getResultById", produces = "application/json;charset=utf-8")
    public CommonResult getResultById(Integer id) {
        try {
            List<ResultEntity> resultList = resultService.getResultById(id);
            return new CommonResult(200, MessageEnum.SUCCESS, resultList);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
        }
    }

    @GetMapping(value = "/get", produces = "application/json;charset=utf-8")
    public CommonResult get() {
        try {
            ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
            int noThreads = currentGroup.activeCount();
            Thread[] lstThreads = new Thread[noThreads];
            currentGroup.enumerate(lstThreads);
            ArrayList<String> arrayList = new ArrayList();
            for (int i = 0; i < noThreads; i++) {
                String nm = lstThreads[i].getName();
                arrayList.add(nm);
            }
            return new CommonResult(200, MessageEnum.SUCCESS, arrayList);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
        }
    }


}
