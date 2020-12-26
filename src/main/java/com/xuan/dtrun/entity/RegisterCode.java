package com.xuan.dtrun.entity;

public class RegisterCode {
     private Integer id;
     private String registerCode;
     private Integer isUse;

     public RegisterCode() {
     }

     public RegisterCode(Integer id, String registerCode, Integer isUse) {
          this.id = id;
          this.registerCode = registerCode;
          this.isUse = isUse;
     }

     public Integer getId() {
          return id;
     }

     public void setId(Integer id) {
          this.id = id;
     }

     public String getRegisterCode() {
          return registerCode;
     }

     public void setRegisterCode(String registerCode) {
          this.registerCode = registerCode;
     }

     public Integer getIsUse() {
          return isUse;
     }

     public void setIsUse(Integer isUse) {
          this.isUse = isUse;
     }
}
