package com.xuan.dtrun.upload;


import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.PartEtag;
import com.obs.services.model.UploadPartRequest;
import com.obs.services.model.UploadPartResult;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

public class ObsUploader implements Runnable {

    private String bucketName;
    private long curPartSize;
    private String objectName;
    private String uploadId;
    private int i;
    private ObsClient obsClient;
    private List<PartEtag> partETags;
    private byte[] bytes;

    public ObsUploader(String bucketName, long curPartSize, String objectName, String uploadId, int i, ObsClient obsClient, List<PartEtag> partETags, byte[] bytes) {
        this.bucketName = bucketName;
        this.curPartSize = curPartSize;
        this.objectName = objectName;
        this.uploadId = uploadId;
        this.i = i;
        this.obsClient = obsClient;
        this.partETags = partETags;
        this.bytes = bytes;
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag && !Thread.currentThread().isInterrupted()) {
            long l = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "开始");
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setObjectKey(objectName);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInput(new ByteArrayInputStream(bytes));
            uploadPartRequest.setPartSize(curPartSize);
            uploadPartRequest.setPartNumber(i + 1);
            UploadPartResult uploadPartResult;
            uploadPartResult = obsClient.uploadPart(uploadPartRequest);
            partETags.add(new PartEtag(uploadPartResult.getEtag(), uploadPartResult.getPartNumber()));
            flag = false;
            long l1 = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "结束，耗时" + (l1 - l) / 1000f);

        }
    }
}
