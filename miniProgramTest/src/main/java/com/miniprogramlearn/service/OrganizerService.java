package com.miniprogramlearn.service;

import com.miniprogramlearn.entity.Activity;
import com.miniprogramlearn.entity.Organizer;
import com.miniprogramlearn.entity.ParamUserAct;

import java.util.List;

public interface OrganizerService {

    //    判断用户是否是组织者
    public boolean judgeUserIsOrg(int userid);

//    根据userid找到对应的组织者
    public Organizer findOrgByUserid(int userid);

    //    根据组织者oid查询 组织的活动
    public List<Activity> findActOfOrgByOid(int userid);

//    根据userid查找组织者组织的活动
    public List<Activity> findActOfOrgByUserid(ParamUserAct paramUserAct);

//    保存新建新的组织者
    public int saveOrg(Organizer organizer);

//    组织者创建新的活动 前提是知道用户是组织者
    public int orgSaveAct(Activity activity,int oid);

//    组织者将oid和aid插入org_act表 此处不用实现


}
