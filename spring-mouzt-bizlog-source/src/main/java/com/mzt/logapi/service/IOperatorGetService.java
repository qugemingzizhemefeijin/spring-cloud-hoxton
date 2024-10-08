package com.mzt.logapi.service;

import com.mzt.logapi.beans.Operator;

/**
 * 获取用户的信息，业务代码
 */
public interface IOperatorGetService {

    /**
     * 可以在里面外部的获取当前登陆的用户，比如UserContext.getCurrentUser()
     *
     * @return 转换成Operator返回
     */
    Operator getUser();

}
