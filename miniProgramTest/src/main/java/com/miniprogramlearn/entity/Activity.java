package com.miniprogramlearn.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class Activity {
    private  int aid;
    private String aname;
    private String aicon;
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")//配置返回给前端的格式化日期形式
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signtimefrom;
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date signtimeto;
    private String place;
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date abegintime;
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date aendtime;
    private String organizername;
    private String organizerph;
    private String ainfo;
    private String amode;

    private List<ActivityImg> activityImgList;


}
