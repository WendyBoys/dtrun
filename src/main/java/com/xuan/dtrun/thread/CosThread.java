package com.xuan.dtrun.thread;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.region.Region;
import com.xuan.dtrun.entity.FileMessage;
import com.xuan.dtrun.entity.ResultEntity;
import com.xuan.dtrun.service.MoveTaskService;
import com.xuan.dtrun.service.ResultService;
import com.xuan.dtrun.upload.CosUpload;
import com.xuan.dtrun.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CosThread implements Runnable {
    private Integer id;
    private JSONObject desEntity;
    private List<FileMessage> fileMessageList;
    private String srcDtSourceType;
    private String srcBucket;
    private COSClient srcCosClient;
    private OSS srcOssClient;
    private JSONObject taskJson;
    private MoveTaskService moveTaskService;
    private ResultService resultService;
    private InputStream inputStream;
    private Logger logger = LoggerFactory.getLogger(CosThread.class);

    public CosThread(Integer id, JSONObject desEntity, List<FileMessage> fileMessageList, String srcDtSourceType, String srcBucket, COSClient srcCosClient, OSS srcOssClient, JSONObject taskJson, MoveTaskService moveTaskService, ResultService resultService) {
        this.id = id;
        this.desEntity = desEntity;
        this.fileMessageList = fileMessageList;
        this.srcDtSourceType = srcDtSourceType;
        this.srcBucket = srcBucket;
        this.srcCosClient = srcCosClient;
        this.srcOssClient = srcOssClient;
        this.taskJson = taskJson;
        this.moveTaskService = moveTaskService;
        this.resultService = resultService;
    }

    @Override
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            COSClient cosClient = new COSClient(new BasicCOSCredentials(desEntity.getString("accessKey"), desEntity.getString("accessSecret")),
                    new ClientConfig(new Region(desEntity.getString("region"))));
            for (FileMessage fileMessage : fileMessageList) {
                switch (srcDtSourceType) {
                    case "cos": {
                        GetObjectRequest getObjectRequest = new GetObjectRequest(srcBucket, fileMessage.getFileName());
                        COSObject cosObject = srcCosClient.getObject(getObjectRequest);
                        inputStream = cosObject.getObjectContent();
                        break;
                    }
                    case "oss": {
                        com.aliyun.oss.model.GetObjectRequest getObjectRequest = new com.aliyun.oss.model.GetObjectRequest(srcBucket, fileMessage.getFileName());
                        OSSObject ossObject = srcOssClient.getObject(getObjectRequest);
                        inputStream = new BufferedInputStream(ossObject.getObjectContent());
                        break;
                    }
                    default: {
                        throw new RuntimeException("不支持的数据源类型: " + srcDtSourceType);
                    }
                }
                CosUpload cosUpload = new CosUpload(taskJson.getString("desBucket"), fileMessage.getFileName(), cosClient, inputStream, fileMessage.getFileLength(), 50 * 1024 * 1024);
                cosUpload.upload();
            }
            cosClient.shutdown();
            if (srcOssClient != null) {
                srcOssClient.shutdown();
            }
            if (srcCosClient != null) {
                srcCosClient.shutdown();
            }
            long endTime = System.currentTimeMillis();
            String timeConsume = String.valueOf((endTime - startTime) / 1000f);
            moveTaskService.updateStatus(id, "FINISH");
            resultService.create(new ResultEntity(DateUtils.getDate(startTime), DateUtils.getDate(endTime), "FINISH", null, timeConsume, fileMessageList.size(), id));
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.info(srcDtSourceType + "输入流关闭失败。。。");
                e.printStackTrace();
            }

        }
    }
}