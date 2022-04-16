package com.miniprogramlearn.service.imply;

import com.github.pagehelper.PageHelper;
import com.miniprogramlearn.entity.Activity;
import com.miniprogramlearn.entity.ParamUserAct;
import com.miniprogramlearn.entity.User;
import com.miniprogramlearn.entity.UserAct;
import com.miniprogramlearn.mapper.ActivityMapper;
import com.miniprogramlearn.mapper.UserMapper;
import com.miniprogramlearn.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImply implements UserService {

    @Resource
    private  UserMapper userMapper;

    @Resource
    private ActivityMapper activityMapper;

    /***
     * 插入新的用户
     * @param user
     */
    @Override
    public int save(User user) {
        int res = userMapper.insert(user);
        return res;
    }

    /***
     * 通过openid查出用户的信息
     * @param openid
     * @return
     */
    @Override
    public User queryByOpid(String openid) {
        User user=null;
        user= userMapper.selectByOpid(openid);
        return user;
    }

    /***
     * login登录业务的实现
     * @param openid
     * @param nickName
     * @param avatarUrl
     * @param session_key
     * @return
     */
    @Override
    public User login(String openid,String nickName,String avatarUrl,String session_key) {
        User user1=null;
        int result=0;
        if(openid!=null){//请求微信接口并成功获取到openid
            User user=new User();
            User user2 = userMapper.selectByOpid(openid);
            if(user2==null){//判断数据库中是否存在该用户，没有存在则新建该用户

                user.setOpenid(openid);
                user.setNickName(nickName);
                user.setAvatarUrl(avatarUrl);
                user.setSessionKey(session_key);
                user.setVoTime(0);
                user.setVoScore(0);
                result=userMapper.insert(user);
                if (result==1)//插入成功才返回该用户
                    user1=user;
            }else{//已经存在该用户 返回该用户的user
                user1=user2;//获取该用户的id作为登录态 并且返回给前端
            }
        }
        return user1;
    }

    /***
     * 根据userid找到用户参加的所有活动
     *  在这里需要拼接字符串 给mapper作为参数 防止sql注入
     *
     *  使用了分页技术
     * @return
     */
    @Override
    public List<Activity> findAllActOfUser(ParamUserAct paramUserAct) {

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

        List<Activity> activityList = userMapper.selectAByUserid(paramUserAct);

        return activityList;
    }



    /***
     * 根据useid查询对应对应的用户
     * @param userid
     * @return
     */
    @Override
    public User findUserByUserid(int userid) {
        User user = userMapper.selectByUserid(userid);

        return user;
    }

    /***
     * 用户报名新的活动
     * @param userAct
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED,readOnly = false,timeout = -1)
    public int signAct(UserAct userAct){
        int i=-1;//没有到报名时间
        Date submitDate = userAct.getSubmitDate();
        int aid = userAct.getAid();


        Activity activity = activityMapper.selectActByAid(aid);//获取活动的报名时间范围 判断 报名的用户的提交的时间是否在该范围内 只有在该范围内才可以允许报名

        Date signtimefrom = activity.getSigntimefrom();
        Date signtimeto = activity.getSigntimeto();


        if(signtimefrom.before(submitDate) && signtimeto.after(submitDate)){  //只有在规定的时间范围内才可以报名

            i= userMapper.insertUserAct(userAct);

        }

        return i;
    }

    /***
     * 判断用户是否报名了该活动
     * @param userAct
     * @return
     */
    @Override
    public UserAct IssignAct(UserAct userAct){

        UserAct userAct1 = userMapper.selectUAByUidAid(userAct);

        return userAct1;

    }

    /***
     * 用户取消报名该活动
     * @param userAct
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED,readOnly = false,timeout = -1)
    public int cancelSignedAct(UserAct userAct){
        int i=-1;//没有到报名时间
        Date submitDate = userAct.getSubmitDate();//获取用户报名该活动的时间 这个前端传入
        int aid = userAct.getAid();//aid前端传入

        Activity activity = activityMapper.selectActByAid(aid);//获取活动的报名时间范围 判断 报名的用户的提交的时间是否在该范围内 只有在该范围内才可以允许报名

        Date signtimefrom = activity.getSigntimefrom();
        Date signtimeto = activity.getSigntimeto();

        if(signtimefrom.before(submitDate) && signtimeto.after(submitDate)){  //只有在规定的时间范围内才可以取消报名
            //检查是否已经报过名了
            i = userMapper.deleteUAByUidAid(userAct);  //取消报名 成功会返回1  否则返回0 返回0是因为数据表中没有该数据 用户没有报名该活动
//            if(userAct1==null){
//                i=2;//没有报过名不可以取消报名
//            }else{
//                i=2;//用户报名过该活动
//            }
        }

        return i;
    }


    /***
     * 根据组织者id找到组织者组织的所有活动
     * @param oid
     * @return
     */
//    @Override
//    public List<Activity> findActOfOrg(int oid) {
//        List<Activity> activityList = userMapper.selectAByOid(oid);
//
//        return activityList;
//    }

    /***
     * 判断用户是否是组织者 根据userid 扎到对应的oid 为0则表示不是组织者
     * @param userid
     * @return
     */
//    @Override
//    public int judgeUserIsOrg(int userid) {
//        User user = userMapper.selectByUserid(userid);
//        int oid = user.getOid();
//
//        return oid;
//    }


}
