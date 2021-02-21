package com.xuan.dtrun.service;

import java.util.List;

public interface LogService {

    void create(Integer uid,String log,String createTime);

    List<String> getLogsById(Integer uid);
}
