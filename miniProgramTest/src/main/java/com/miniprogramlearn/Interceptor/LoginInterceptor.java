package com.miniprogramlearn.Interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {

    private ObjectMapper objectMapper=new ObjectMapper();

    /**自定义拦截器
     * 创建类实现SpringMVC框架的HandlerInterceptor接口  ctrl+i查看实现接口的哪些子方法
     * 实现了拦截其接口后 需要在配置MVC配置文件中配置注册  但是在springboot中一实现WebMvcConfigurer接口代替了MVC.xml的配置*/
    @Override
    /*Object handler参数是被拦截的 控制器对象*/
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //String userid = request.getParameter("userid");
        HttpSession session = request.getSession();
        Map attribute = (Map) session.getAttribute("userid");
        System.out.println("拦截");
        if (attribute == null) {

            response.setContentType("text/html;charset=UTF-8");//响应类型为html或文本格式
//            response.setCharacterEncoding("UTF-8");//设置编码方式
//            response.setContentType("text/javascript;charset=UTF-8");//响应数据类型json数据格式

            PrintWriter writer = response.getWriter();

            Map<String,Object> errinfo=new HashMap<>(8);
            errinfo.put("errmsg","还没有登录");
            errinfo.put("status",-101);


            writer.println(objectMapper.writeValueAsString(errinfo));
            return false;
        }
        return true;
    }
}
