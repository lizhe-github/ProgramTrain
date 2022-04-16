package com.miniprogramlearn.mapper;

import com.miniprogramlearn.entity.Activity;

import java.util.List;

public interface ActivityMapper {

    public List<Activity> findAllPage1();

    public List<Activity> findAllPage2();

    public Activity findAandImgById(int aid);

//    该方法并没有去查找活动详情页的所有图片
    public Activity selectActByAid(int aid);

//    插入新的活动 自增的id存储到插入的对象的对应的字段
    public int insertAct(Activity activity);

    //    插入act_img表 插入活动详情的所有图片
    public int insertActImg(int aid,String aimgz);

//    搜索活动，关键字有活动名字 ，活动内容，活动组织者姓名，时间，
    public List<Activity> searchAct(String condition);
}
