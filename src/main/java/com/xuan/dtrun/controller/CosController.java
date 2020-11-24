package com.xuan.dtrun.controller;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.region.Region;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.CosEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/cos")
public class CosController {


    @PostMapping(value = "/connection", produces = "application/json;charset=utf-8")
    public CommonResult connection(@RequestBody CosEntity cosEntity) {
        try {
            COSClient cosClient = new COSClient(new BasicCOSCredentials(cosEntity.getSecretId(), cosEntity.getSecretKey()),
                    new ClientConfig(new Region(cosEntity.getRegion())));
            cosClient.listBuckets();
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.CONNECTIOSUCCESS);
        } catch (CosServiceException e) {
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.CONNECTIONFAIL);
        }


    }

}
