package com.miniprogramlearn.service;

import com.miniprogramlearn.entity.Activity;
import com.miniprogramlearn.entity.ParamUserAct;
import com.miniprogramlearn.entity.User;
import com.miniprogramlearn.entity.UserAct;

import java.util.List;

public interface UserService {

//    返回值 更新的行数
    public int save(User user);

    public User queryByOpid(String openid);
//    登录的业务方法
    public User login(String openid,String nickName,String avatarUrl,String session_key);
//    根据userid查询用户参加的所有活动
    public List<Activity> findAllActOfUser(ParamUserAct paramUserAct);

//    根据组织者id查询 组织的活动
//    public List<Activity> findActOfOrg(int userid);

//    根据userid  查看user
    public User findUserByUserid(int userid);

//    用户报名新活动
    public int signAct(UserAct userAct);

//    取消用户报名过的该活动
    public int cancelSignedAct(UserAct userAct);

//    判断用户是否报名了该活动
    public UserAct IssignAct(UserAct userAct);


//    判断用户是否是组织者
//    public int judgeUserIsOrg(int userid);
}
