package com.xuan.dtrun.upload;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;

import java.io.ByteArrayInputStream;
import java.util.List;

public class OssUploader implements Runnable {

    private String bucketName;
    private long curPartSize;
    private String objectName;
    private String uploadId;
    private int i;
    private OSS ossClient;
    private List<PartETag> partETags;
    private byte[] bytes;

    public OssUploader(String bucketName, long curPartSize, String objectName, String uploadId, int i, OSS ossClient, List<PartETag> partETags, byte[] bytes) {
        this.bucketName = bucketName;
        this.curPartSize = curPartSize;
        this.objectName = objectName;
        this.uploadId = uploadId;
        this.i = i;
        this.ossClient = ossClient;
        this.partETags = partETags;
        this.bytes = bytes;
    }

    @Override
    public void run() {
        UploadPartRequest uploadPartRequest = new UploadPartRequest();
        uploadPartRequest.setBucketName(bucketName);
        uploadPartRequest.setKey(objectName);
        uploadPartRequest.setUploadId(uploadId);
        uploadPartRequest.setInputStream(new ByteArrayInputStream(bytes));
        // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
        uploadPartRequest.setPartSize(curPartSize);
        // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
        uploadPartRequest.setPartNumber(i + 1);
        // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
        UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
        // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
        partETags.add(uploadPartResult.getPartETag());
    }
}
