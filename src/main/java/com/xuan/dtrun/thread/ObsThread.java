package com.xuan.dtrun.thread;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
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
import com.xuan.dtrun.utils.MailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ObsThread implements Runnable {
    private Integer id;
    private String taskName;
    private JSONObject desEntity;
    private List<FileMessage> fileMessageList;
    private String srcDtSourceType;
    private String srcBucket;
    private COSClient srcCosClient;
    private OSS srcOssClient;
    private ObsClient srcoObsClient;
    private JSONObject taskJson;
    private MoveTaskService moveTaskService;
    private ResultService resultService;
    private InputStream inputStream;
    private Logger logger = LoggerFactory.getLogger(ObsThread.class);

    public ObsThread(Integer id, String taskName, JSONObject desEntity, List<FileMessage> fileMessageList, String srcDtSourceType, String srcBucket, COSClient srcCosClient, OSS srcOssClient, ObsClient srcoObsClient, JSONObject taskJson, MoveTaskService moveTaskService, ResultService resultService) {
        this.id = id;
        this.taskName = taskName;
        this.desEntity = desEntity;
        this.fileMessageList = fileMessageList;
        this.srcDtSourceType = srcDtSourceType;
        this.srcBucket = srcBucket;
        this.srcCosClient = srcCosClient;
        this.srcOssClient = srcOssClient;
        this.srcoObsClient = srcoObsClient;
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
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        int len;
                        byte[] srcBytes = new byte[10 * 1024 * 1024];
                        while ((len = ossObject.getObjectContent().read(srcBytes)) > -1) {
                            byteArrayOutputStream.write(srcBytes, 0, len);
                        }
                        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                        break;
                    }
                    case "obs": {
                        ObsObject obsObject = srcoObsClient.getObject(srcBucket, fileMessage.getFileName());
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        int len;
                        byte[] srcBytes = new byte[10 * 1024 * 1024];
                        while ((len = obsObject.getObjectContent().read(srcBytes)) > -1) {
                            byteArrayOutputStream.write(srcBytes, 0, len);
                        }
                        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
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
            resultService.create(new ResultEntity(DateUtils.getDate(startTime), DateUtils.getDate(endTime), "FINISH", null, timeConsume, fileMessageList.size(), JSONObject.toJSONString(fileMessageList), id));
            String sendMail = taskJson.getString("sendMail");
            if ("true".equals(sendMail)) {
                String mail = taskJson.getString("contact");
                {
                    MailUtils.sendMail("您的迁移任务" + taskName + "已完成", mail);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
