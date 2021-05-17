package com.xuan.dtrun.entity;

public class WhiteListEntity {
     private int id;
     private String ip;
     private String createTime;
     private String uid;
     private int key;

     public WhiteListEntity() {
     }

     public WhiteListEntity(int id, String ip, String createTime, String uid, int key) {
          this.id = id;
          this.ip = ip;
          this.createTime = createTime;
          this.uid = uid;
          this.key = key;
     }

     public String getUid() {
          return uid;
     }

     public void setUid(String uid) {
          this.uid = uid;
     }

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public String getIp() {
          return ip;
     }

     public void setIp(String ip) {
          this.ip = ip;
     }

     public String getCreateTime() {
          return createTime;
     }

     public void setCreateTime(String createTime) {
          this.createTime = createTime;
     }

     public int getKey() {
          return key;
     }

     public void setKey(int key) {
          this.key = key;
     }


}
