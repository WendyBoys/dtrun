package com.xuan.dtrun.controller;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.region.Region;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.CosEntity;
import com.xuan.dtrun.entity.DtSourceEntity;
import com.xuan.dtrun.service.DtSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/dtsource")
public class DtSourceController {

    @Autowired
    private DtSourceService dtSourceService;


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

    @PostMapping(value = "/create", produces = "application/json;charset=utf-8")
    public CommonResult create(@RequestBody JSONObject json) {
        try {
            String dataSourceName = json.getString("dataSourceName");
            String dataSourceType = json.getString("dataSourceType");
            String secretId = json.getString("secretId");
            String secretKey = json.getString("secretKey");
            String region = json.getString("region");
            DtSourceEntity dtSourceEntity = new DtSourceEntity();
            dtSourceEntity.setDtsourcename(dataSourceName);
            Map map = new HashMap<>();
            map.put("dataSourceType", dataSourceType);
            map.put("secretId", secretId);
            map.put("accessSecret", secretKey);
            map.put("region", region);
            JSONObject jsonObject = new JSONObject(map);
            dtSourceEntity.setDtSourceType(dataSourceType);
            dtSourceEntity.setCreateTime(new Date());
            dtSourceEntity.setDtsourceJson(jsonObject.toJSONString());
            dtSourceService.create(dtSourceEntity);
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.CREATESUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.CREATEFAIL);
        }
    }

}
