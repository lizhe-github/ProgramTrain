package com.miniprogramlearn.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.miniprogramlearn.entity.*;
import com.miniprogramlearn.service.UserService;
import com.miniprogramlearn.util.ReqUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private ReqUtil reqUtil;

    @Resource
    private ApplicationValue appValue;

    @Resource
    private UserService userService;

    private ObjectMapper objectMapper=new ObjectMapper();

    /**
     * 登录获取openid等不过
     * 前端获取到的userinfo 解密后得到的仍然只有昵称和图片不会有城市等其它信息，尽管可以有openid但是官方不建议使用解密方式获取openid而是使用wx,login
     * 解密代码：
     *  byte[] content = AESUtil.decodeBase(encryptedData);
     *         byte[] aesKey = AESUtil.decodeBase(session_key);
     *         byte[] ivByte = AESUtil.decodeBase(iv);
     *
     *         byte[] decrypt = AESUtil.decrypt(content,aesKey,ivByte );
     *
     *         String encodeStr = null;
     *         try {
     *             encodeStr = AESUtil.encodeStr(decrypt, "UTF-8");
     *         } catch (UnsupportedEncodingException e) {
     *             e.printStackTrace();
     *         }
     *         System.out.println(encodeStr);
     * */
    @PostMapping("/login")
    @ResponseBody//返回值是一个对象，但是springboot自动配置了一个转换，让其转换为json
    public Map<String,Object> login(String  code, String nickName, String avatarUrl, HttpSession httpSession) throws Exception{
        Map<String,String> param=new HashMap<>(8);
        param.put("appid",appValue.getAppid());
        param.put("secret",appValue.getAppSecret());
        param.put("js_code",code);
        param.put("grant_type",appValue.getGrantType());

        String responseResult = reqUtil.sendGet("https", "api.weixin.qq.com/sns/jscode2session", param);


        Map maoReadValue=null;//将微信接口请求返回的字符串转换为map散列表

        maoReadValue=objectMapper.readValue(responseResult, Map.class);

        String session_key =(String)maoReadValue.get("session_key");
        String openid =(String)maoReadValue.get("openid");
        String errcode=(String) maoReadValue.get("errcode");
        String errmsg=(String) maoReadValue.get("errmsg");

//        到后面 大致可以把所有的响应信息封装成一个类 而不是如下使用map
        Map<String,Object> info=new HashMap<>(8);//返回给前端的信息
        info.put("errcode",errcode);//请求微信接口成功，但没有得到openid等信息，这些是需要返回给前端的值,如果得到了这些值为null
        info.put("errmsg",errmsg);

        //请求微信接口失败，默认的返回给前端的json字符串
        info.put("status",-103);
        info.put("msg","登录失败");


        //请求微信接口并成功获取到openid
        User user1 = userService.login(openid,nickName,avatarUrl,session_key);//获取该用户的id作为登录态 并且返回给前端

        if(user1!=null){ //登录成功

            maoReadValue.put("userid",user1.getUserid());//存入userid

//            httpSession.setAttribute(String.valueOf(user1.getUserid()),maoReadValue);
            httpSession.setAttribute("userid",maoReadValue);

//            info.put("userid",user1.getUserid());
            info.put("JSessionId",httpSession.getId());//将sessionId和userid返回给前端
            info.replace("status",101);
            info.replace("msg","登录成功");
        }

//        int i=1/0;
        return info;
    }

    /**查看用户志愿的信息，如志愿时长和积分
     * 还可以用来验证用户的登录态
     * @param
     * @param httpSession
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/voInfo")
    @ResponseBody
    public Map<String,Object> voInfo(HttpSession httpSession) throws JsonProcessingException {
        Map attribute = (Map) httpSession.getAttribute("userid");
        String openid = (String) attribute.get("openid");
        User user = userService.queryByOpid(openid);
        System.out.println(user);


        Map<String,Object> info=new HashMap<>(2);
        if(user!=null){
            info.put("voTime",user.getVoTime());
            info.put("voScore",user.getVoScore());
            //info.put("oid",user.getOid());//组织者id
            info.put("status",101);
            info.put("msg","获取成功");
        }else {
            info.put("status",-103);
            info.put("msg","获取失败");
        }



        return info;

    }


    /***
     * 根据userid找到用户参加的所有活动
     * @param
     * @param httpSession
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/findUserAct")
    @ResponseBody
    public Map<String,Object> findUserAct(ParamUserAct paramUserAct, HttpSession httpSession) throws JsonProcessingException {
        Map attribute = (Map) httpSession.getAttribute("userid");
        int userid1 = (int) attribute.get("userid");

        paramUserAct.setUserid(userid1);

        List<Activity> allActOfUser = userService.findAllActOfUser(paramUserAct);

        PageInfo<Activity> pageInfo=new PageInfo<Activity>(allActOfUser);


        Map<String,Object> info=new HashMap<>(2);


        info.put("userAlist",allActOfUser);//不用组织者id
        info.put("status",101);
        info.put("msg","获取成功");
        info.put("totalnum",pageInfo.getTotal());

//        if(allActOfUser!=null){
//            info.put("userAlist",allActOfUser);//组织者id
//            info.put("status",101);
//            info.put("msg","获取成功");
//        }else{
//            info.put("status",-103);
//            info.put("msg","获取失败");
//        }

        return info;

    }

    /***
     * userid 后端查 amode后端赋值    aid前端给 dateSubmit前端给，后两个需要前端封装带对象中
     * 用户报名活动
     *
     * @RequestBody只有加上请求体 前端放在body中的json数据才会自动给转换为你指定的参数 一旦制定了参数是请求体中的内容spring才会自动给转换
     * @param userAct
     * @param httpSession
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping("/signAct")
    @ResponseBody
    public Map<String,Object> signAct(UserAct userAct, HttpSession httpSession) throws JsonProcessingException {
        System.out.println(userAct);
        Map attribute = (Map) httpSession.getAttribute("userid");

        int userid1 = (int) attribute.get("userid");
        userAct.setMode("已报名");
        userAct.setUserid(userid1);


        int res = userService.signAct(userAct);


        Map<String,Object> info=new HashMap<>(2);


        info.put("status",-103);
        info.put("msg","报名失败");  //默认返回消息

        switch (res){
            case 1:
                info.put("status",101);
                info.put("msg","报名成功");
                break;
            case -1:
                info.put("status",-103);
                info.put("msg","报名失败，没有在报名时间范围内");
                break;
                //其它情况失败的原因可能是已经报名过该活动
        }

       /* if(res==1){
            info.put("status",1);
            info.put("msg","sign success");
        }else if(res==-1){
            info.put("status",3);
            info.put("msg","还没有到报名时间");
        }else if(res == 0){
            info.put("status",2);
            info.put("msg","sign failed");
        }else if(res==2){
            info.put("status",5);
            info.put("msg","you have signed this activity");
        }*/

        return info;

    }

    /**
     * 判断用户是否报名过该活动，报名过返回相关信息
     * @param userAct
     * @param httpSession
     * @return
     */
    @PostMapping("/isSignAct")
    @ResponseBody
    public Map<String,Object> isSignAct(UserAct userAct,HttpSession httpSession){
        System.out.println(userAct);
        Map attribute = (Map) httpSession.getAttribute("userid");

        int userid1 = (int) attribute.get("userid");

        userAct.setUserid(userid1);

        Map<String,Object> info=new HashMap<>(2);

        UserAct userAct1 = userService.IssignAct(userAct);
        if(userAct1!=null){
            info.put("status",101);
            info.put("msg","已报名该活动");
            info.put("actMode",userAct1.getMode());
        }else{
            info.put("status",-103);
            info.put("msg","没有报名该活动");
        }
        return info;
    }


    /***
     * userid 后端查 aid前端给 dateSubmit前端给
     * 用户取消报名活动
     * @param userAct
     * @param httpSession
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping("/cancelSignedAct")
    @ResponseBody
    public Map<String,Object> cancelSignedAct( UserAct userAct,HttpSession httpSession) throws JsonProcessingException {
        Map attribute = (Map) httpSession.getAttribute("userid");

        int userid1 = (int) attribute.get("userid");

        userAct.setUserid(userid1);

        int res = userService.cancelSignedAct(userAct);

        Map<String,Object> info=new HashMap<>(2);


        if(res==1){
            info.put("status",101);
            info.put("msg","取消报名成功");
        }else if(res==-1){
            info.put("status",-103);
            info.put("msg","失败，不在取消报名的时间范围内");
        }else {//res==0
            info.put("status",-103);
            info.put("msg","失败，你没有报名过该活动");
        }

        return info;

    }






}
