package com.xuan.dtrun.entity;

public class ServerparametersEntity {

     private Integer id;
     private String createTime;
     private String cpu;
     private String runningmemory;
     private String diskusagepercentage;
     private Integer uid;

     public ServerparametersEntity() {
     }

     public ServerparametersEntity(Integer id, String date, String cpu, String runningmemory, String diskusagepercentage, Integer uid) {
          this.id = id;
          this.createTime = date;
          this.cpu = cpu;
          this.runningmemory = runningmemory;
          this.diskusagepercentage = diskusagepercentage;
          this.uid = uid;
     }

     public Integer getId() {
          return id;
     }

     public void setId(Integer id) {
          this.id = id;
     }

     public String getCreateTime() {
          return createTime;
     }

     public void setCreateTime(String createTime) {
          this.createTime = createTime;
     }

     public String getCpu() {
          return cpu;
     }

     public void setCpu(String cpu) {
          this.cpu = cpu;
     }

     public String getRunningmemory() {
          return runningmemory;
     }

     public void setRunningmemory(String runningmemory) {
          this.runningmemory = runningmemory;
     }

     public String getDiskusagepercentage() {
          return diskusagepercentage;
     }

     public void setDiskusagepercentage(String diskusagepercentage) {
          this.diskusagepercentage = diskusagepercentage;
     }

     public Integer getUid() {
          return uid;
     }

     public void setUid(Integer uid) {
          this.uid = uid;
     }
}
