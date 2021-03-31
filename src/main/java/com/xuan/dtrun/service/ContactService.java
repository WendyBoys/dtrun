package com.xuan.dtrun.service;

import com.xuan.dtrun.entity.ContactEntity;

import java.util.List;

public interface ContactService {
     List<ContactEntity> findAll(int id);

     void  createContact(ContactEntity contactEntity);

     void deleteContact(Object[] ids);

     ContactEntity getContactById(int id);


     void updateContact( Integer integer,String contactName, String contactEmail);

     String isCreate(String contactName);


}
