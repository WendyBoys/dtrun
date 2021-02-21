package com.xuan.dtrun.upload;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PartETag;
import com.qcloud.cos.model.UploadPartRequest;
import com.qcloud.cos.model.UploadPartResult;

import java.io.ByteArrayInputStream;
import java.util.List;

public class CosUploader implements Runnable {

    private String bucketName;
    private long curPartSize;
    private String objectName;
    private String uploadId;
    private int i;
    private COSClient cosClient;
    private List<PartETag> partETags;
    private byte[] bytes;

    public CosUploader(String bucketName, long curPartSize, String objectName, String uploadId, int i, COSClient cosClient, List<PartETag> partETags, byte[] bytes) {
        this.bucketName = bucketName;
        this.curPartSize = curPartSize;
        this.objectName = objectName;
        this.uploadId = uploadId;
        this.i = i;
        this.cosClient = cosClient;
        this.partETags = partETags;
        this.bytes = bytes;
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag && !Thread.currentThread().isInterrupted()) {
            long l = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "开始");
            UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(bucketName).
                    withUploadId(uploadId).withKey(objectName).withPartNumber(i + 1).
                    withInputStream(new ByteArrayInputStream(bytes)).withPartSize(curPartSize);
            UploadPartResult uploadPartResult = cosClient.uploadPart(uploadRequest);
            String etag = uploadPartResult.getETag();
            partETags.add(new PartETag(i + 1, etag));
            flag = false;
            long l1 = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "结束，耗时" + (l1 - l) / 1000f);
        }
    }
}
