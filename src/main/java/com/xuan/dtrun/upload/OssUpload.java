package com.xuan.dtrun.upload;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class OssUpload {

    private String bucketName;
    private String objectName;
    private OSS ossClient;
    private InputStream inputStream;
    private long fileLength;
    private ExecutorService executors = Executors.newFixedThreadPool(10);
    private final long partSize = 10 * 1024 * 1024L;
    private int byteSize = 10 * 1024 * 1024;

    public OssUpload(String bucketName, String objectName, OSS ossClient, InputStream inputStream, long fileLength, int byteSize) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.ossClient = ossClient;
        this.inputStream = inputStream;
        this.fileLength = fileLength;
        this.byteSize = byteSize;
    }

    public void upload() throws IOException, InterruptedException {

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
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        // 遍历分片上传。
        byte[] srcBytes = new byte[byteSize];
        int len;
        int i = 0;
        while ((len = bufferedInputStream.read(srcBytes)) > -1) {
            byte[] desBytes = new byte[byteSize];
            System.arraycopy(srcBytes, 0, desBytes, 0, len);
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            // 跳过已经上传的分片。
            executors.execute(new OssUploader(bucketName, curPartSize, objectName, uploadId, i, ossClient, partETags, desBytes));
            i++;
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

        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);

        CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        ossClient.shutdown();
    }
}
