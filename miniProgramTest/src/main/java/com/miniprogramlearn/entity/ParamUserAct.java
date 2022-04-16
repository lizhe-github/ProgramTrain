package com.miniprogramlearn.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 封装请求参数的实体类 一般用在前端请求用户参加过的活动 以及搜索自己参加过的活动
 * 只有在登录后查看参加过的活动需要userid  其它不需要
 *
 * pagenum和pagesize是为了实现分页
 *
 * 还可以用在前端 在没有登录的情况下 查询所有活动或者是 搜索活动
 * inputString 参数就是前端搜索的关键字
 */
@Getter
@Setter
@ToString
public class ParamUserAct {
    private String inputString;//默认为空字符串
    private int pagenum;
    private int pagesize;
    private int userid;
}
