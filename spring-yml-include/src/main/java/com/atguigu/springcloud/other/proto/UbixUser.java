package com.atguigu.springcloud.other.proto;

import io.protostuff.Tag;

import java.util.ArrayList;
import java.util.List;

public class UbixUser {
    @Tag(1)
    String uid;
    @Tag(2)
    int gender;
    @Tag(3)
    int age;
    @Tag(4)
    List<String> keywords = new ArrayList<>();
    @Tag(5)
    String ext;
    @Tag(6)
    List<String> tags = new ArrayList<>();
    @Tag(7)
    List<String> category = new ArrayList<>();
    @Tag(8)
    List<String> installedApps = new ArrayList<>();
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public int getGender() {
        return gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public List<String> getKeywords() {
        return keywords;
    }
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    public String getExt() {
        return ext;
    }
    public void setExt(String ext) {
        this.ext = ext;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public List<String> getCategory() {
        return category;
    }
    public void setCategory(List<String> category) {
        this.category = category;
    }
    public List<String> getInstalledApps() {
        return installedApps;
    }
    public void setInstalledApps(List<String> installedApps) {
        this.installedApps = installedApps;
    }

}
