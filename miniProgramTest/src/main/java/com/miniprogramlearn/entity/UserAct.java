package com.miniprogramlearn.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


/***
 * 对应的是表中的用户_活动 表
 *
 * 这里新建这个实体类 是为了 前端在报名活动过的时侯传递参数方便
 */
@Getter
@Setter
@ToString
public class UserAct {

    private int userid;
    private int aid;
    private String mode;
//    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private Date submitDate;
}
