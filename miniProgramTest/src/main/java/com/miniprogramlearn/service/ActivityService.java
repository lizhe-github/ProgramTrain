package com.miniprogramlearn.service;

import com.miniprogramlearn.entity.Activity;

import java.util.List;

public interface ActivityService {

    public List<Activity> listAllPage1();
    public List<Activity> listAllPage2(int pagenum,int pagesize);
    public Activity listAandImgById(int aid);

//    搜索活动业务 关键字活动名字或者内容或者组织者姓名或者时间
    public List<Activity> searchAct(String inputString,int pagenum,int pagesize);
}
