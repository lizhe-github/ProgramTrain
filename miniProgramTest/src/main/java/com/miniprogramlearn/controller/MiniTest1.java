package com.miniprogramlearn.controller;

import org.springframework.stereotype.Controller;

@Controller
public class MiniTest1 {
//
//    @GetMapping("/hello")
//    @ResponseBody
//    public String hello(Model model){
//        model.addAttribute("mm","hh");
//
//        return "modelAndView";
//
//    }
//
//    @GetMapping("/logintest")
//    @ResponseBody
//    public String login(int code, String name, Model model, HttpServletRequest request, HttpServletResponse response){
//        /*request.setAttribute("remed","request 域 值");
//        request.setAttribute("User-Agent","useragent域");
//        response.setHeader("Content-Type","login=123");*/
//        model.addAttribute("mm","get login");
//        System.out.println(name);
//        return String.valueOf(code)+name+"hh啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈h";
//
//    }
//
//
//    @PostMapping("/post")
//    @ResponseBody
//    public String post(int code, String name,Model model,HttpServletRequest request,HttpServletResponse response){
//        String code1 = request.getParameter("code");//获取前端请求erl时候传递过来的 &连接的K:V参数
//        /*request.setAttribute("remed","request 域 值");
//        request.setAttribute("User-Agent","useragent域");
//        response.setHeader("Content-Type","login=123");*/
//        //model.addAttribute("me","post form");   //add和setAttribute的作用往往是页面进行转发的时候，往另一个页面存储数据进行渲染，
//        // 可以将数据放在request域中，不知道重定向可以不可以
//        System.out.println(name);
//        return String.valueOf(code)+name+"  "+code1;
//
//    }
//
//    @PostMapping("/post1")
//    @ResponseBody
//    public String post1(@RequestBody User user,Model model, HttpServletRequest request){//,
//        String code1 = request.getParameter("code");
//        /*model.addAttribute("me","post json");*/
//        System.out.println(user.getName());
//        return user.toString();//+"postJson  "+code1
//        //返回的时一个普通的java字符产，不是json字符串，前端接收的时候时不能按照json的反序列化，将该json数据转换为java对象的
//
//    }
//
//    @PostMapping("/post2")
//    @ResponseBody
//    public String post2(@RequestBody User user,Model model, HttpServletRequest request){//,
//        String code1 = request.getParameter("code");
//        /*model.addAttribute("me","post json");*/
//        System.out.println(user.getName());
//        return "{\"code\" : \"003\" , \"name\" : \"你好啊 java11异步请求\"}";//+"postJson  "+code1
//        //return "{code='003', name='你好啊 java11异步请求'}";
//
//    }
//
//    @PostMapping("/post3")
//    @ResponseBody
//    public User post3(@RequestBody User user,Model model, HttpServletRequest request){//,
//        String code1 = request.getParameter("code");
//        /*model.addAttribute("me","post json");*/
//        System.out.println(user.getName());
//        //return "{\"code\" : \"003\" , \"name\" : \"你好啊 java11异步请求\"}";//+"postJson  "+code1
//        return user;//springboot内部集成了jackson的自动转换，若有@ResponseBody 那么即便返回的是一个java对象，其实内部会自动转换为json字符串进行传输
//
//    }

}
