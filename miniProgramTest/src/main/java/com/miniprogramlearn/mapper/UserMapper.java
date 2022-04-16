package com.miniprogramlearn.mapper;

import com.miniprogramlearn.entity.Activity;
import com.miniprogramlearn.entity.ParamUserAct;
import com.miniprogramlearn.entity.User;
import com.miniprogramlearn.entity.UserAct;

import java.util.List;

public interface UserMapper {

//    返回值会被mybatis注入 更新的行数
    public int insert(User user);

    public User selectByOpid(String openid);

//    选择根据userid选择用户参加过的活动
    public List<Activity> selectAByUserid(ParamUserAct paramUserAct);

//    根据userid找到对应的User
    public User selectByUserid(int userid);

//    用户报名新活动 插入user_act表
    public int insertUserAct(UserAct userAct);

//    检查用户是否报过该活动
    public UserAct selectUAByUidAid(UserAct userAct);

    public int deleteUAByUidAid(UserAct userAct);


}
