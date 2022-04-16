package com.miniprogramlearn.service.imply;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.miniprogramlearn.entity.Activity;
import com.miniprogramlearn.mapper.ActivityMapper;
import com.miniprogramlearn.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ActivityServiceImply implements ActivityService {
    @Resource
    private ActivityMapper activityMapper;

    /***
     * 查询出所有活动而且活动对象包含了 活动详情页面的所有图片
     * @return
     */
    @Override
    public List<Activity> listAllPage1() {

        PageHelper.startPage(1,3);
        List<Activity> all = activityMapper.findAllPage1();

        //获得分页中相关参数 需要使用PageInfo
        //类的第一个泛型参数就是执行分页来查询数据库后得到的数据类型 最后一个参数是在执行分页后查询数据后得到的对象
        PageInfo<Activity> pageInfo=new PageInfo<Activity>(all);
        return all;
    }

    /***
     * 使用了分页技术
     *  查询出所有活动而且活动对象 不包含 活动详情页面的所有图片
     *
     *  pagehelper的使用放在 dao mapper层还是其他层都可以 只要是在执行sql语句之前就可以
     * @param pagenum
     * @param pagesize
     * @return
     */
    @Override
    public List<Activity> listAllPage2(int pagenum,int pagesize) {
        PageHelper.startPage(pagenum,pagesize);
        List<Activity> all = activityMapper.findAllPage2();

        //获得分页中相关参数 需要使用PageInfo
        //类的第一个泛型参数就是执行分页来查询数据库后得到的数据类型 最后一个参数是在执行分页后查询数据后得到的对象
        PageInfo<Activity> pageInfo=new PageInfo<Activity>(all);
        return all;
    }

    /***
     * 查询出某个活动id对应的活动而且包含 活动详情页面的所有图片
     * @param aid
     * @return
     */
    @Override
    public Activity listAandImgById(int aid) {
        Activity aandImgById = activityMapper.findAandImgById(aid);
        return aandImgById;
    }


    /***
     * 搜索活动的业务方法 关键字活动名字或者内容或者组织者姓名或者时间
     * 此处需要对参数字符串进行加工1 让字符串的首位都包含%字符串为了 mapper层的模糊查询
     *
     * 此处实现而不在mapper层sql语句的拼接是为了防止sql注入
     *
     * 搜索仍然需要使用分页技术
     * @param inputString
     * @return
     */
    @Override
    public List<Activity> searchAct(String inputString,int pagenum,int pagesize) {
        StringBuffer stringBuffer=new StringBuffer(inputString);//字符串的索引1 表示i 字符  下标从1开始 。

        stringBuffer.insert(0,"%");//插入的时候 往所在位置的后面插入字符 0 表示在i的前面
        stringBuffer.append("%");
//        String condition="%"+stringBuffer+"%";
        String condition= stringBuffer.toString();

        PageHelper.startPage(pagenum,pagesize);
        List<Activity> activityList = activityMapper.searchAct(condition);

        return activityList;

    }


}
