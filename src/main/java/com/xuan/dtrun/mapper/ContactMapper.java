package com.xuan.dtrun.mapper;

import com.xuan.dtrun.entity.ContactEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ContactMapper {
     List<ContactEntity> findAll(int id);

     void createContact(ContactEntity contactEntity);

     void deleteContact(Object[] ids);

     ContactEntity getContactById(int id);

     void updateContact(Integer integer,String contactName, String contactEmail);

     String isCreate(String contactName);

}
