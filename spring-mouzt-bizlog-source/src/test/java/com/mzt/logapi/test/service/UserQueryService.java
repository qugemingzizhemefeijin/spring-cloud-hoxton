package com.mzt.logapi.test.service;

import org.apache.catalina.User;

import java.util.List;

public interface UserQueryService {

    List<User> getUserList(List<String> userIds);

    String getDesc();

}
