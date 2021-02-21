package com.xuan.dtrun.upload;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CosUpload {
    private String bucketName;
    private String objectName;
    private COSClient cosClient;
    private InputStream inputStream;
    private long fileLength;
    private ExecutorService executors = Executors.newFixedThreadPool(10);
    private long partSize;
    private volatile boolean flag = true;

    private Logger logger = LoggerFactory.getLogger(OssUpload.class);

    public CosUpload(String bucketName, String objectName, COSClient cosClient, InputStream inputStream, long fileLength, long partSize) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.cosClient = cosClient;
        this.inputStream = inputStream;
        this.fileLength = fileLength;
        this.partSize = partSize;
    }


    public String upload() throws IOException, InterruptedException {

        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, objectName);
        InitiateMultipartUploadResult initResponse = cosClient.initiateMultipartUpload(initRequest);
        String uploadId = initResponse.getUploadId();


        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
        List<PartETag> partETags = new ArrayList<PartETag>();
        // 计算文件有多少个分片。
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        logger.info("分片数为" + partCount);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        // 遍历分片上传。
        byte[] srcBytes = new byte[(int) partSize];
        int len;
        int i = 0;
        Semaphore semaphore = new Semaphore(partCount);
        try {
            while ((len = bufferedInputStream.read(srcBytes)) > -1) {
                semaphore.acquire();
                byte[] desBytes = new byte[(int) partSize];
                System.arraycopy(srcBytes, 0, desBytes, 0, len);
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                logger.info("上传分片" + i);
                executors.execute(new CosUploader(bucketName, curPartSize, objectName, uploadId, i, cosClient, partETags, desBytes));
                i++;
                semaphore.release();
            }
        } catch (RejectedExecutionException e) {
            logger.info("中断线程池...");
            return "QUIT";
        } finally {
            inputStream.close();
        }

        executors.shutdown();
        while (!executors.isTerminated()) {
            try {
                executors.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bufferedInputStream.close();
            }
        }

        try {
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);
            CompleteMultipartUploadResult result = cosClient.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            logger.info("完成分片上传失败...");
            return "FAIL";
        }
        return "FINISH";
    }

    public void stop() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (executors != null) {
            logger.info("cos终止上传");
            executors.shutdownNow();
        }
    }
}
