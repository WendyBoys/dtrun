package com.xuan.dtrun.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.obs.services.ObsClient;
import com.obs.services.model.S3Bucket;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.region.Region;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.DtSourceEntity;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.service.DtSourceService;
import com.xuan.dtrun.service.LogService;
import com.xuan.dtrun.utils.ClientIp;
import com.xuan.dtrun.utils.DateUtils;
import com.xuan.dtrun.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/dtsource")
public class DtSourceController {

    @Autowired
    private DtSourceService dtSourceService;

    @Autowired
    private LogService logService;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping(value = "/testconnection", produces = "application/json;charset=utf-8")
    public CommonResult testconnection(@RequestBody JSONObject json) {
        String dataSourceType = json.getString("dataSourceType");
        String accessKey = json.getString("accessKey");
        String accessSecret = json.getString("accessSecret");
        String region = json.getString("region");
        try {
            if ("cos".equals(dataSourceType)) {
                COSClient cosClient = new COSClient(new BasicCOSCredentials(accessKey, accessSecret),
                        new ClientConfig(new Region(region)));
                cosClient.listBuckets();
                cosClient.shutdown();
            } else if ("oss".equals(dataSourceType)) {
                OSS ossClient = new OSSClientBuilder().build(region, accessKey, accessSecret);
                ossClient.listBuckets();
                ossClient.shutdown();
            }else if ("obs".equals(dataSourceType)) {
                ObsClient obsClient = new ObsClient(accessKey, accessSecret, region);
                obsClient.listBuckets();
                obsClient.close();
            }
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.CONNECTIOSUCCESS);
        } catch (Exception e) {
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.CONNECTIONFAIL);
        }
    }

    @GetMapping(value = "/getDtSourceById", produces = "application/json;charset=utf-8")
    public CommonResult getDtSourceById(int id) {
        try {
            DtSourceEntity dtSourceEntity = dtSourceService.getDtSourceById(id);
            return new CommonResult(200, MessageEnum.SUCCESS, dtSourceEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
        }
    }

    @PostMapping(value = "/connection", produces = "application/json;charset=utf-8")
    public CommonResult connection(@RequestBody JSONObject idJson) {
        try {
            DtSourceEntity dtSourceEntity = dtSourceService.getDtSourceById(Integer.parseInt(idJson.getString("id")));
            String dtSourceType = dtSourceEntity.getDtSourceType();
            JSONObject jsonObject = JSON.parseObject(dtSourceEntity.getDtSourceJson());
            if ("cos".equals(dtSourceType)) {
                COSClient cosClient = new COSClient(new BasicCOSCredentials(jsonObject.getString("accessKey"), jsonObject.getString("accessSecret")),
                        new ClientConfig(new Region(jsonObject.getString("region"))));
                cosClient.listBuckets();
                cosClient.shutdown();
            } else if ("oss".equals(dtSourceType)) {
                OSS ossClient = new OSSClientBuilder().build(jsonObject.getString("region"), jsonObject.getString("accessKey"), jsonObject.getString("accessSecret"));
                ossClient.listBuckets();
                ossClient.shutdown();
            }else if ("obs".equals(dtSourceType)) {
                ObsClient obsClient = new ObsClient(jsonObject.getString("accessKey"), jsonObject.getString("accessSecret"), jsonObject.getString("region"));
                List<S3Bucket> s3Buckets = obsClient.listBuckets();
                obsClient.close();
            }
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.CONNECTIOSUCCESS);
        } catch (Exception e) {
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.CONNECTIONFAIL);
        }
    }

    @PostMapping(value = "/create", produces = "application/json;charset=utf-8")
    public CommonResult create(@RequestBody JSONObject json, @RequestHeader String token) {
        try {
            User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
            if (user == null) {
                return new CommonResult(200, MessageEnum.FAIL, DataEnum.LOGINEXPIRE);
            }
            String dataSourceName = json.getString("dataSourceName");
            String dataSourceType = json.getString("dataSourceType");
            String secretId = json.getString("secretId");
            String secretKey = json.getString("secretKey");
            String region = json.getString("region");
            DtSourceEntity dtSourceEntity = new DtSourceEntity();
            dtSourceEntity.setDtSourceName(dataSourceName);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dataSourceType", dataSourceType);
            jsonObject.put("accessKey", secretId);
            jsonObject.put("accessSecret", secretKey);
            jsonObject.put("region", region);
            dtSourceEntity.setUser(user);
            dtSourceEntity.setDtSourceType(dataSourceType);
            dtSourceEntity.setCreateTime(DateUtils.getDate());
            dtSourceEntity.setDtSourceJson(jsonObject.toJSONString());
            dtSourceService.create(dtSourceEntity);
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.CREATESUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.CREATEFAIL);
        }
    }


    @PostMapping(value = "/update", produces = "application/json;charset=utf-8")
    public CommonResult update(@RequestBody JSONObject json) {
        try {
            String id = json.getString("id");
            String dataSourceName = json.getString("dataSourceName");
            String dataSourceType = json.getString("dataSourceType");
            String secretId = json.getString("secretId");
            String secretKey = json.getString("secretKey");
            String region = json.getString("region");
            DtSourceEntity dtSourceEntity = new DtSourceEntity();
            dtSourceEntity.setDtSourceName(dataSourceName);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dataSourceType", dataSourceType);
            jsonObject.put("accessKey", secretId);
            jsonObject.put("accessSecret", secretKey);
            jsonObject.put("region", region);
            dtSourceEntity.setId(Integer.parseInt(id));
            dtSourceEntity.setDtSourceType(dataSourceType);
            dtSourceEntity.setDtSourceJson(jsonObject.toJSONString());
            dtSourceService.update(dtSourceEntity);
            return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.CREATESUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.CREATEFAIL);
        }
    }

    @GetMapping(value = "/findAll", produces = "application/json;charset=utf-8")
    public CommonResult findAll(@RequestHeader("token") String token) {
        try {
            User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
            List<DtSourceEntity> dtSourceEntityList = dtSourceService.findAll(user.getId());
            return new CommonResult(200, MessageEnum.SUCCESS, dtSourceEntityList);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
        }

    }

    @GetMapping(value = "/getAllDtSourceName", produces = "application/json;charset=utf-8")
    public CommonResult getAllDtSourceName(@RequestHeader("token") String token) {
        try {
            User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
            List<DtSourceEntity> dtSourceEntityList = dtSourceService.getAllDtSourceName(user.getId());
            return new CommonResult(200, MessageEnum.SUCCESS, dtSourceEntityList);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
        }
    }

    @PostMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public CommonResult delete(@RequestBody JSONObject json, @RequestHeader("token") String token, @ClientIp String ip) {
        try {
            User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
            if (user != null) {
                Object[] ids = json.getJSONObject("data").getJSONArray("id").toArray();
                dtSourceService.delete(ids);
                logService.create(user.getId(), "删除" + ids.length + "个数据源,ip地址为" + ip, DateUtils.getDate(),"red");
                return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.DELETESUCCESS);
            } else {
                return new CommonResult(200, MessageEnum.FAIL, DataEnum.LOGINEXPIRE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.DELETEFAIL);
        }
    }


    @GetMapping(value = "/getBucketLists", produces = "application/json;charset=utf-8")
    public CommonResult getBucketLists(int id) {
        COSClient cosClient = null;
        OSS ossClient = null;
        try {
            List<String> buckets = null;
            DtSourceEntity dtSourceEntity = dtSourceService.getDtSourceById(id);
            if (dtSourceEntity != null) {
                String dtSourceType = dtSourceEntity.getDtSourceType();
                JSONObject jsonObject = JSON.parseObject(dtSourceEntity.getDtSourceJson());
                if ("cos".equals(dtSourceType)) {
                    cosClient = new COSClient(new BasicCOSCredentials(jsonObject.getString("accessKey"), jsonObject.getString("accessSecret")),
                            new ClientConfig(new Region(jsonObject.getString("region"))));
                    buckets = cosClient.listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());
                    cosClient.shutdown();

                } else if ("oss".equals(dtSourceType)) {
                    ossClient = new OSSClientBuilder().build(jsonObject.getString("region"), jsonObject.getString("accessKey"), jsonObject.getString("accessSecret"));
                    buckets = ossClient.listBuckets().stream().map(com.aliyun.oss.model.Bucket::getName).collect(Collectors.toList());
                }else if ("obs".equals(dtSourceType)) {
                    ObsClient obsClient = new ObsClient(jsonObject.getString("accessKey"), jsonObject.getString("accessSecret"), jsonObject.getString("region"));
                    buckets = obsClient.listBuckets().stream().map(com.obs.services.model.S3Bucket::getBucketName).collect(Collectors.toList());
                }
            }
            return new CommonResult(200, MessageEnum.SUCCESS, buckets);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @PostMapping(value = "/createBucket", produces = "application/json;charset=utf-8")
    public CommonResult createBucket(@RequestBody JSONObject json) {
        COSClient cosClient = null;
        OSS ossClient = null;
        try {
            List<String> buckets = null;
            int desId = Integer.parseInt(json.getString("desId"));
            String newBucketName = json.getString("newBucketName");
            DtSourceEntity dtSourceEntity = dtSourceService.getDtSourceById(desId);
            if (dtSourceEntity != null) {
                String dtSourceType = dtSourceEntity.getDtSourceType();
                JSONObject jsonObject = JSON.parseObject(dtSourceEntity.getDtSourceJson());
                if ("cos".equals(dtSourceType)) {
                    cosClient = new COSClient(new BasicCOSCredentials(jsonObject.getString("accessKey"), jsonObject.getString("accessSecret")),
                            new ClientConfig(new Region(jsonObject.getString("region"))));
                    com.qcloud.cos.model.CreateBucketRequest createBucketRequest = new com.qcloud.cos.model.CreateBucketRequest(newBucketName);
                    // 设置 bucket 的权限为 Private(私有读写), 其他可选有公有读私有写, 公有读写
                    createBucketRequest.setCannedAcl(CannedAccessControlList.Private);
                    cosClient.createBucket(createBucketRequest);
                    buckets = cosClient.listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());
                } else if ("oss".equals(dtSourceType)) {
                    ossClient = new OSSClientBuilder().build(jsonObject.getString("region"), jsonObject.getString("accessKey"), jsonObject.getString("accessSecret"));
                    com.aliyun.oss.model.CreateBucketRequest createBucketRequest = new com.aliyun.oss.model.CreateBucketRequest(newBucketName);
                    createBucketRequest.setCannedACL(com.aliyun.oss.model.CannedAccessControlList.Private);
                    ossClient.createBucket(createBucketRequest);
                    buckets = ossClient.listBuckets().stream().map(com.aliyun.oss.model.Bucket::getName).collect(Collectors.toList());
                }
            }
            return new CommonResult(200, MessageEnum.SUCCESS, buckets);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(200, MessageEnum.FAIL, DataEnum.CREATEFAIL);
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
