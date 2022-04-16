package com.miniprogramlearn.controller;

import com.miniprogramlearn.mapper.SwiperMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/public/index")
public class PublicController {

    @Resource
    private SwiperMapper swiperMapper;

    @GetMapping("/getSwiperImg")
    @ResponseBody()
    public Map<String,Object> getSwiperImg(){
        List<String> swiperImg = swiperMapper.selectSwiperImg();

        Map<String,Object> result=new HashMap<>();
        result.put("status",101);
        result.put("imglist",swiperImg);
        return result;
    }
}
