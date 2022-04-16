package com.miniprogramlearn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.miniprogramlearn.entity.Activity;
import com.miniprogramlearn.entity.Organizer;
import com.miniprogramlearn.entity.ParamUserAct;
import com.miniprogramlearn.service.OrganizerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/organizer")
public class OrganizerController {
    @Resource
    private OrganizerService organizerService;

    /***
     * 根据用户的userid找到用户组织者组织的所有活动 前提是用户是组织者
     * @param
     * @param httpSession
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/findOrgAct")
    @ResponseBody
    public Map<String,Object> findOrgAct(ParamUserAct paramUserAct, HttpSession httpSession) throws JsonProcessingException {
        Map attribute = (Map) httpSession.getAttribute("userid");
        int userid1 = (int) attribute.get("userid");

        paramUserAct.setUserid(userid1);

        List<Activity> allActOfOrg = organizerService.findActOfOrgByUserid(paramUserAct);

        PageInfo<Activity> pageInfo=new PageInfo<Activity>(allActOfOrg);

        Map<String,Object> info=new HashMap<>(2);

        info.put("orgAlist",allActOfOrg);//组织者id呢
        info.put("status",101);
        info.put("msg","获取成功");
        info.put("totalnum",pageInfo.getTotal());

        return info;
    }


    /***
     * 用户提交信息申请成为组织者
     * @param userid
     * @param httpSession
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/applyOrg")
    @ResponseBody
    public Map<String,Object> applyOrg(String userid,String unit,Date authtime,String authway,String authinfo, HttpSession httpSession){
        Map attribute = (Map) httpSession.getAttribute("userid");
        int userid1 = (int) attribute.get("userid");

        Map<String,Object> info=new HashMap<>(5);

        if(!organizerService.judgeUserIsOrg(userid1)){//判断该用户是不是组织者，是的 话不允许再次申请成为组织者
            Organizer organizer=new Organizer();
            organizer.setUserid(userid1);
            organizer.setAuthinfo(authinfo);
            organizer.setMode("审核");
            organizer.setAuthway(authway);
            organizer.setAuthtime(authtime);
            organizer.setUnit(unit);

            int result = organizerService.saveOrg(organizer);

            if(result!=0){
                info.put("result",result);//组织者id
                info.put("status",1);
                info.put("msg","get success");
            }else{
                info.put("status",2);
                info.put("msg","apply failed");
            }
        }else{
            info.put("status",-1);
            info.put("errmsg","have been organized");
        }


        return info;
    }


    /***
     * 如果是组织者的话进行创建活动
     * @param userid
     * @param httpSession
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/saveAct")
    @ResponseBody
    public Map<String,Object> saveAct(String userid,Activity activity, HttpSession httpSession){
        Map attribute = (Map) httpSession.getAttribute("userid");
        int userid1 = (int) attribute.get("userid");

        Organizer orgByUserid = organizerService.findOrgByUserid(userid1);

        activity.setAmode("审核中");

        int result = organizerService.orgSaveAct(activity,orgByUserid.getOid());

        Map<String,Object> info=new HashMap<>(5);
        if(result!=0){
            info.put("result",result);//组织者id
            info.put("status",1);
            info.put("msg","get success");
        }else{
            info.put("status",2);
            info.put("msg","apply failed");
        }
        return info;
    }

    /***
     * 根据userid  以及oid  aid 更改活动的信息
     */



}
