package com.miniprogramlearn.service.imply;

import com.github.pagehelper.PageHelper;
import com.miniprogramlearn.entity.Activity;
import com.miniprogramlearn.entity.ActivityImg;
import com.miniprogramlearn.entity.Organizer;
import com.miniprogramlearn.entity.ParamUserAct;
import com.miniprogramlearn.mapper.ActivityMapper;
import com.miniprogramlearn.mapper.OrganizerMapper;
import com.miniprogramlearn.service.OrganizerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class OrganizerServiceImply implements OrganizerService {
    @Resource
    private OrganizerMapper organizerMapper;

    @Resource
    private ActivityMapper activityMapper;

    /***
     * 根据userid 找到对应的organizer 判断用户是否是组织者  为null或者mode!=成功 则表示不是组织者 返回值组织者
     * @param userid
     * @return
     */
    @Override
    public boolean judgeUserIsOrg(int userid) {
        Organizer organizer = organizerMapper.selectOByUserid(userid);
        if(organizer!=null){
            if(organizer.getMode().equals("成功")){
                return true;
            }
        }
        return false;
    }

    @Override
    public Organizer findOrgByUserid(int userid) {
        Organizer organizer = organizerMapper.selectOByUserid(userid);

        return organizer;

    }



    /***
     * 根据组织者id找到组织者组织的所有活动
     * @param oid
     * @return
     */
    @Override
    public List<Activity> findActOfOrgByOid(int oid) {

        List<Activity> activityList = organizerMapper.selectOAByOid(oid);

        return activityList;
    }


    /***
     * 根据userid查找组织者组织过的所有活动
     * @param
     * @return
     */
    @Override
    public List<Activity> findActOfOrgByUserid(ParamUserAct paramUserAct) {

        String inputString=paramUserAct.getInputString();

        String condition=null;

        if(inputString!=null){
            StringBuffer buffer=new StringBuffer(inputString);
            buffer.insert(0,"%");
            buffer.append("%");
            condition=new String(buffer);
        }

        System.out.println(condition);

        paramUserAct.setInputString(condition);

        PageHelper.startPage(paramUserAct.getPagenum(),paramUserAct.getPagesize());

        List<Activity> activityList = organizerMapper.selectOAByUserid(paramUserAct);

        return activityList;
    }

    /***
     * 新建 新的组织者
     * @param organizer
     * @return
     */
    @Override
    public int saveOrg(Organizer organizer) {
        int result = organizerMapper.insertOrg(organizer);
        return result;
    }

    /***
     * 还需要解决一个问题就是前端上传的图片找个 存储位置并且把存储的地址 赋值给activity中的字段
     * 在数据库中新建一个活动到活动表和 活动和组织者表 需要检验一下报名和活动时间是否符合逻辑
     * @param activity
     * @return
     */
    @Override //增加事务控制
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED,readOnly = false,timeout = -1)
    public int orgSaveAct(Activity activity,int oid) {

        int result = activityMapper.insertAct(activity); //还需要插入org_act表吗，没有完成

        int aid = activity.getAid();

        List<ActivityImg> activityImgList = activity.getActivityImgList();
        activityImgList.forEach(item->{
            activityMapper.insertActImg(aid,item.getAimgz());
        });
//        上面两个方法调用activity mapper将活动插入新的活动，将活动详情页面的图片集合插入 act_img表
//        下面将活动组织者id 和活动id差人org_act表
//        int m=1/0;
        int result1 = organizerMapper.insertActToOA(oid, aid);

        return result1;
    }


}
