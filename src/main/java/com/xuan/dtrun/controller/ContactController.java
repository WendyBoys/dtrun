package com.xuan.dtrun.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.xuan.dtrun.common.CommonResult;
import com.xuan.dtrun.common.DataEnum;
import com.xuan.dtrun.common.MessageEnum;
import com.xuan.dtrun.entity.ContactEntity;
import com.xuan.dtrun.entity.User;
import com.xuan.dtrun.service.ContactService;
import com.xuan.dtrun.utils.DateUtils;
import com.xuan.dtrun.utils.TokenUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/contact")
public class ContactController {

     @Autowired
     private ContactService contactService;

     @Autowired
     private RedisTemplate redisTemplate;


     @GetMapping(value = "/findAll", produces = "application/json;charset=utf-8")
     public CommonResult findAll(@RequestHeader("token") String token) {
          try {
               User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
               List<ContactEntity> contact = contactService.findAll(user.getId());
               return new CommonResult(200, MessageEnum.SUCCESS, contact);
          } catch (Exception e) {
               e.printStackTrace();
               return new CommonResult(200, MessageEnum.FAIL, DataEnum.CREATEFAIL);
          }
     }

     @GetMapping(value = "getContactById", produces = "application/json;charset=utf-8")
     public CommonResult getContactById(int id) {
          try {
               ContactEntity contactEntity = contactService.getContactById(id);
               return new CommonResult(200, MessageEnum.SUCCESS, contactEntity);
          } catch (Exception e) {
               e.printStackTrace();
               return new CommonResult(200, MessageEnum.FAIL, DataEnum.QUERYFAILE);
          }
     }


     @PostMapping(value = "/updateContact", produces = "application/json;charset=utf-8")
     public CommonResult update(@RequestBody JSONObject json) {
          try {
               String id = json.getString("id");
               String contactName = json.getString("contactName");
               String contactEmail = json.getString("contactEmail");
               Integer integer = Integer.parseInt(id);
               contactService.updateContact(integer, contactName, contactEmail);
               return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.MODIFYSUCCESS);
          } catch (Exception e) {
               e.printStackTrace();
               return new CommonResult(200, MessageEnum.FAIL, DataEnum.MODIFYFAIL);
          }

     }


     @PostMapping(value = "createContact", produces = "application/json;charset=utf-8")
     public CommonResult createContact(@RequestBody ContactEntity contactEntity, @RequestHeader("token") String token) {
          User user = (User) redisTemplate.opsForValue().get(TokenUtils.md5Token(token));
          String newContactName = contactEntity.getContactName();
          String oldContactName = contactService.isCreate(newContactName);
          if (oldContactName == null) {
               Integer uid = user.getId();
               contactEntity.setCreateTime(DateUtils.getDate());
               contactEntity.setUid(uid);
               contactService.createContact(contactEntity);
               return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.CREATESUCCESS);
          } else if (oldContactName.equals(newContactName)) {
                    return new CommonResult(200, MessageEnum.FAIL, DataEnum.CREATEFAIL);
          } else {
               return new CommonResult(200, MessageEnum.SUCCESS, DataEnum.CREATEFAIL);
               }

     }

    @PostMapping(value = "/delete",produces = "application/json;charset=utf-8")
     public CommonResult delete(@RequestBody JSONObject json){
         Object[] ids = json.getJSONObject("data").getJSONArray("id").toArray();
         contactService.deleteContact(ids);
         return new CommonResult(200,MessageEnum.SUCCESS,DataEnum.DELETESUCCESS);
    }


     @GetMapping(value = "getMailById", produces = "application/json;charset=utf-8")
     public String getMailById(int id){
          return "email:"+id;
     }
}
