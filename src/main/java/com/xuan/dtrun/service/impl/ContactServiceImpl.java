package com.xuan.dtrun.service.impl;

import com.xuan.dtrun.entity.ContactEntity;
import com.xuan.dtrun.entity.DtSourceEntity;
import com.xuan.dtrun.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import com.xuan.dtrun.mapper.*;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactMapper contactMapper;

    @Override
    public List<ContactEntity> findAll(int id) {
        List<ContactEntity> contactEntityList = contactMapper.findAll(id);
        contactEntityList.forEach(dtSourceEntity -> {
            dtSourceEntity.setKey(dtSourceEntity.getId());
        });
        return contactEntityList;
    }

    @Override
    public void createContact(ContactEntity contactEntity) {
        contactMapper.createContact(contactEntity);
    }

    @Override
    public void deleteContact(Object[] ids) {
        contactMapper.deleteContact(ids);
    }

    @Override
    public ContactEntity getContactById(int id) {
        return contactMapper.getContactById(id);
    }

    @Override
    public void updateContact(Integer integer, String contactName, String contactEmail) {
        contactMapper.updateContact(integer, contactName, contactEmail);
    }

    @Override
    public String isCreate(String contactName) {
        return contactMapper.isCreate(contactName);
    }


}
