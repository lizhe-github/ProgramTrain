package com.miniprogramlearn.mapper;

import com.miniprogramlearn.entity.Activity;
import com.miniprogramlearn.entity.Organizer;
import com.miniprogramlearn.entity.ParamUserAct;

import java.util.List;

public interface OrganizerMapper {



//    根据userid查找组织者表对应的组织者
    public Organizer selectOByUserid(int userid);

    //    根据oid查询组织的所有活动
    public List<Activity> selectOAByOid(int oid);


    //     根据userid查找组织者组织的活动 前提是要已经判断过该user是组织者
    public List<Activity> selectOAByUserid(ParamUserAct paramUserAct);

//    插入新的组织者
    public int insertOrg(Organizer organizer);

//    组织者申请新的活动  前提是判断出该用户属于组织者
//    public int orgInsertAct(Activity activity);

//    插入org_act表中 进行组织者创建新的活动
      public int insertActToOA(int oid,int aid);



}
