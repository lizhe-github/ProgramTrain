package com.miniprogramlearn.config;

import com.miniprogramlearn.Interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyAppMvcConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //创建拦截器对象
        HandlerInterceptor interceptor = new LoginInterceptor();//这个是自己写的那个实现拦截器接口的对象

        //指定拦截的请求uri地址
        String path []= {"/user/**"};
        //指定不拦截的地址
        String excludePath  [] = {"/user/login"};
        registry.addInterceptor(interceptor)/*它的返回值是一个registration对象 可以进行拦截器的相关配置如下*/
                .addPathPatterns(path)/*参数可以是集合也可以是 单个对象 下面相同*/
                .excludePathPatterns(excludePath);
//        //指定拦截的请求uri地址
//        String path2 []= {"/user/organizer/**"};
//        //指定不拦截的地址
//        String excludePath2  [] = {"/user/organizer/applyOrg"};
//        HandlerInterceptor interceptor2=new OrgActInterceptor();
//
//        registry.addInterceptor(interceptor2)
//                .addPathPatterns(path2)
//                .excludePathPatterns(excludePath2);
    }
}
