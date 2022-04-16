package com.miniprogramlearn.Interceptor;

import com.miniprogramlearn.service.OrganizerService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class OrgActInterceptor implements HandlerInterceptor {
    @Resource
    private OrganizerService organizerService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Map sessionMapData = (Map) session.getAttribute(request.getParameter("userid"));
        System.out.println("再拦截");
        int userid = (int) sessionMapData.get("userid");
        boolean flag = organizerService.judgeUserIsOrg(userid);
        if(flag){
            //request.setAttribute("oid",oid);
            return true;
        }
        Map<String,Object> errinfo=new HashMap<>(8);
        errinfo.put("status",-102);//用户不是组织者
        errinfo.put("msg","请先申请成为组织者");
        //response.setContentType("text/html;charset=UTF-8");
        response.setContentType("text/html;charset=UTF-8");//响应类型为html或文本格式
        response.getWriter().println(errinfo);
        return false;
    }
}
