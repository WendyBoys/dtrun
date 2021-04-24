package com.xuan.dtrun.entity;

public class WhiteListEntity {
     private int id;
     private String ip;
     private String createTime;
     private int key;

     public WhiteListEntity() {
     }

     public WhiteListEntity(int id, String ip, String createTime) {
          this.id = id;
          this.ip = ip;
          this.createTime = createTime;
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
