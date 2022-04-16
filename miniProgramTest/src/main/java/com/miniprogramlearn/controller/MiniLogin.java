package com.miniprogramlearn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniprogramlearn.entity.ApplicationValue;
import com.miniprogramlearn.util.AESUtil;
import com.miniprogramlearn.util.ReqUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MiniLogin {

    @Resource
    private ReqUtil reqUtil;

    @Resource
    private ApplicationValue appValue;

   /* @PostMapping("/login")
    @ResponseBody
    public String login(String  code){
        System.out.println(code);=
        return "success code";
    }*/

    /**
     * 登录获取openid等不过
     * 前端获取到的userinfo 解密后得到的仍然只有昵称和图片不会有城市等其它信息，尽管可以有openid但是官方不建议使用解密方式获取openid而是使用wx,login
     * */
    @PostMapping("/loginTest2")
    @ResponseBody
    public String login(String  code,String encryptedData,String iv){
        System.out.println(code);
        System.out.println(encryptedData);
        System.out.println(iv);


        Map<String,String> param=new HashMap<>(8);
        param.put("appid",appValue.getAppid());
        param.put("secret",appValue.getAppSecret());
        param.put("js_code",code);
        param.put("grant_type",appValue.getGrantType());

        String responseResult = reqUtil.sendGet("https", "api.weixin.qq.com/sns/jscode2session", param);

        ObjectMapper objectMapper=new ObjectMapper();
        Map maoReadValue=null;
        try {
            maoReadValue=objectMapper.readValue(responseResult, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String session_key =(String)maoReadValue.get("session_key");
        String openid =(String)maoReadValue.get("openid");
        System.out.println("session_key="+session_key);
        System.out.println("openid="+openid);

        byte[] content = AESUtil.decodeBase(encryptedData);
        byte[] aesKey = AESUtil.decodeBase(session_key);
        byte[] ivByte = AESUtil.decodeBase(iv);

        byte[] decrypt = AESUtil.decrypt(content,aesKey,ivByte );

        String encodeStr = null;
        try {
            encodeStr = AESUtil.encodeStr(decrypt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(encodeStr);

        return "success code";
    }

}
