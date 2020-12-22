package com.xuan.dtrun.entity;

public class RegisterCode {
     private Integer id;
     private String value;
     private Integer isUse;

     public RegisterCode() {
     }

     public RegisterCode(Integer id, String value, Integer isUse) {
          this.id = id;
          this.value = value;
          this.isUse = isUse;
     }

     public Integer getId() {
          return id;
     }

     public void setId(Integer id) {
          this.id = id;
     }

     public String getValue() {
          return value;
     }

     public void setValue(String value) {
          this.value = value;
     }

     public Integer getIsUse() {
          return isUse;
     }

     public void setIsUse(Integer isUse) {
          this.isUse = isUse;
     }
}
