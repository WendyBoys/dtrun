package com.xuan.dtrun.upload;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class OssUpload {

    private String bucketName;
    private String objectName;
    private OSS ossClient;
    private InputStream inputStream;
    private long fileLength;
    private ExecutorService executors = Executors.newFixedThreadPool(10);
    private long partSize;
    private volatile boolean flag = true;

    private Logger logger = LoggerFactory.getLogger(OssUpload.class);

    public OssUpload(String bucketName, String objectName, OSS ossClient, InputStream inputStream, long fileLength, long partSize) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.ossClient = ossClient;
        this.inputStream = inputStream;
        this.fileLength = fileLength;
        this.partSize = partSize;
    }

    public String upload() throws IOException, InterruptedException {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);
        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
        String uploadId = upresult.getUploadId();

        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
        List<PartETag> partETags = new ArrayList<PartETag>();
        // 计算文件有多少个分片。
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        logger.info("分片数为" + partCount);
        // 遍历分片上传。
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            byte[] srcBytes = new byte[(int) partSize];
            int len;
            int i = 0;
            Semaphore semaphore = new Semaphore(partCount);
            while (flag && (len = bufferedInputStream.read(srcBytes)) > -1) {
                semaphore.acquire();
                byte[] desBytes = new byte[(int) partSize];
                System.arraycopy(srcBytes, 0, desBytes, 0, len);
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                logger.info("上传分片" + i);
                executors.execute(new OssUploader(bucketName, curPartSize, objectName, uploadId, i, ossClient, partETags, desBytes));
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
                inputStream.close();
            }
        }

        try {
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);

            CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        } catch (Exception e) {
            logger.info("完成分片上传失败...");
            return "FAIL";
        }
        return "FINISH";
    }

    public void stop() {
        if (executors != null) {
            logger.info("oss终止上传");
            executors.shutdownNow();
        }
        flag = false;
    }
}
