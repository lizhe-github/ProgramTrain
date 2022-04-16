package com.miniprogramlearn.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class User {
    private String openid;
    private int userid;
    private String nickName;
    private String avatarUrl;
    private String sessionKey;
    private double voTime;
    private double voScore;
//    private int oid;

    private List<Activity> activityList;

}
