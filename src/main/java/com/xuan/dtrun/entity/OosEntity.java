package com.xuan.dtrun.entity;

public class OosEntity {

     private String secretId;
     private String secretKey;
     private String region;

     public OosEntity() {
     }

     public OosEntity(String secretId, String secretKey, String region) {
          this.secretId = secretId;
          this.secretKey = secretKey;
          this.region = region;
     }

     public String getSecretId() {
          return secretId;
     }

     public void setSecretId(String secretId) {
          this.secretId = secretId;
     }

     public String getSecretKey() {
          return secretKey;
     }

     public void setSecretKey(String secretKey) {
          this.secretKey = secretKey;
     }

     public String getRegion() {
          return region;
     }

     public void setRegion(String region) {
          this.region = region;
     }
}
