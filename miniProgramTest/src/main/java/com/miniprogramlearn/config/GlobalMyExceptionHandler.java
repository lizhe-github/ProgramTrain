package com.miniprogramlearn.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
//全局处理异常
public class GlobalMyExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody//最后的返回值会返回到前端  即便这里返回的是一个对象，springboot会自动转换为json格式
    public Map<String,Object> handlerException(HttpServletRequest request,
                                        Exception e){
        Map<String,Object> errinfo=new HashMap<>(8);
        errinfo.put("errmsg",e.getLocalizedMessage());
        errinfo.put("status",-1);
        System.out.println(e);
//        System.out.println("异常处理》");
        return errinfo;

    }
}
