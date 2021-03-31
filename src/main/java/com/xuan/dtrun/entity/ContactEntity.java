package com.xuan.dtrun.entity;

import java.util.Date;

public class ContactEntity {
     private Integer id;
     private String contactName;
     private String contactEmail;
     private String createTime;
     private Integer uid;

     public ContactEntity() {
     }

     public ContactEntity(Integer id, String contactName, String contactEmail, String createTime, Integer uid) {
          this.id = id;
          this.contactName = contactName;
          this.contactEmail = contactEmail;
          this.createTime = createTime;
          this.uid = uid;
     }

     public Integer getId() {
          return id;
     }

     public void setId(Integer id) {
          this.id = id;
     }

     public String getContactName() {
          return contactName;
     }

     public void setContactName(String contactName) {
          this.contactName = contactName;
     }

     public String getContactEmail() {
          return contactEmail;
     }

     public void setContactEmail(String contactEmail) {
          this.contactEmail = contactEmail;
     }

     public String getCreateTime() {
          return createTime;
     }

     public void setCreateTime(String createTime) {
          this.createTime = createTime;
     }

     public Integer getUid() {
          return uid;
     }

     public void setUid(Integer uid) {
          this.uid = uid;
     }
}