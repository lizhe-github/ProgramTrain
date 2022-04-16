package com.miniprogramlearn.controller;

import com.github.pagehelper.PageInfo;
import com.miniprogramlearn.entity.Activity;
import com.miniprogramlearn.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/activity")
public class ActivityController {


    @Resource
    private ActivityService activityService;



    /**使用分页技术查找所有活动的信息，但是并没有查找所有活动详情页面的集合
     *
     * @param pagenum
     * @param pagesize
     * @return
     */
    @GetMapping("/findAllPage")
    @ResponseBody
    public Map<String,Object> findAllPage(int pagenum,int pagesize){

        List<Activity> all = activityService.listAllPage2(pagenum,pagesize);
        //获得分页中相关参数 需要使用PageInfo
        //类的第一个泛型参数就是执行分页来查询数据库后得到的数据类型 最后一个参数是在执行分页后查询数据后得到的对象
        PageInfo<Activity> pageInfo=new PageInfo<Activity>(all);
//        System.out.println(pageInfo.getTotal());
        Map<String,Object> result=new HashMap<>();
        result.put("alist",all);
        result.put("totalnum",pageInfo.getTotal());
        result.put("status",101);

//        System.out.println(all);
        return result;
    }



    /**根据活动的id找到，活动信息和活动详情页面的所有图片集合封装在火哦的那个类实例中
     *
     * @param aid
     * @return
     */
    @GetMapping("/listAandImgById")
    @ResponseBody
    public Map<String,Object> listAandImgById(int aid){
        Activity activity = activityService.listAandImgById(aid);
        Map<String,Object> result=new HashMap<>();
        result.put("aObj",activity);
        return result;
    }

    @GetMapping("/searchAct")
    @ResponseBody
    public Map<String,Object> searchAct(String inputString,int pagenum,int pagesize){
        List<Activity> activityList = activityService.searchAct(inputString,pagenum,pagesize);

        //获得分页中相关参数 需要使用PageInfo
        //类的第一个泛型参数就是执行分页来查询数据库后得到的数据类型 最后一个参数是在执行分页后查询数据后得到的对象
        PageInfo<Activity> pageInfo=new PageInfo<Activity>(activityList);

        Map<String,Object> result=new HashMap<>();
        result.put("status",101);
        result.put("totalnum",pageInfo.getTotal());
        result.put("alist",activityList);

        return result;//spring自动将Java对象转换为json
    }




}
