package com.atguigu.springcloud.other.proto;

import io.protostuff.Tag;

public class UbixBidRequest {
    @Tag(1)
    String requestId;
    @Tag(2)
    String apiVersion;
    @Tag(4)
    UbixUser user;
    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public String getApiVersion() {
        return apiVersion;
    }
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
    public UbixUser getUser() {
        return user;
    }
    public void setUser(UbixUser user) {
        this.user = user;
    }

}
