package com.miniprogramlearn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/download")
public class DownloadController {

    /***
     * //文件下载 设置了响应头为application/force-download所以
     * 不能加上响应体注解  返回值是java对象的 springboot无法识别将其转换为转换为application/json中的json格式
     * 所以不能返回java对象自动转换 会报错  即便返回string前端收不到 可以把信息加载响应头里面
     *
     * OutputStream os = response.getOutputStream();//获取响应的输出流
     *  // 此处记住不是获取writer之前打印在浏览器的时候需要获取writer尽心输出打印  writer是为了获取字符流返回PrintWriter是为了打印字符
     *  //但是 返回的响应获取的输出流 只能使用一个 否则两个同时在一个response中使用会出错
     * @param filename
     * @param response
     * @throws Exception
     */
    @GetMapping("/actImg")
//    @ResponseBody
    public  void Download(String filename, HttpServletResponse response) throws Exception{
//        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String imgFilePath = this.getClass().getResource("/static/uploadimg/").getPath();  //类路径的根目录下的static中的文件夹

        Map<String,Object> resInfoMap=new HashMap<>(9);

        if(filename!=null){
            File file = new File(imgFilePath + filename); //将本地文件赋值给file

            if (!file.exists()) {
                resInfoMap.put("status",3);
                resInfoMap.put("msg","文件不存在");
            }else{
                response.setContentType("application/force-download"); //必须设置该响应头才可以下载  // 设置强制下载不打开
//                response.setContentType("application/json");
                response.addHeader("Content-Disposition", "attachment;fileName=" + filename);// 设置文件名


                byte[] bufferByte = new byte[1024]; //设置每一次下载多少字节 1024B=1KB

                try (FileInputStream byteInputStream = new FileInputStream(file);//给file创建 字节输入流进行读取
                     BufferedInputStream byteBufferStream = new BufferedInputStream(byteInputStream)) {  //将字节输入流转换为字节缓冲输入流。
                    // java1.7后try()中的资源六在执行完代码块后会自动关闭不要在finanlly中手动关闭

                    OutputStream os = response.getOutputStream();//获取响应的输出流
                    // 此处记住不是获取writer之前打印在浏览器的时候需要获取writer尽心输出打印  writer是为了获取字符流返回PrintWriter是为了打印字符
                    //但是 返回的响应获取的输出流 只能使用一个 否则两个同时在一个response中使用会出错

                    int i = byteBufferStream.read(bufferByte); //在缓冲流中读取字节  进入bufferByte字节数组，该字节数组初始化长度为1024
                    //返回的是读取到字节数组中的真实字节数 满的话就是字节数组长度  数据读取完毕返回-1

                    while (i != -1) {  //还有数据 继续以字节进行读取
                        os.write(bufferByte, 0, i);//将bufferByte数组中的字节进行输出但并非全部输出 而是输出初始偏移量到i这一段 不包括i 包括初始

                        i = byteBufferStream.read(bufferByte);//继续以1024字节长度进行读取
                    }
                    resInfoMap.put("status",1);
                    resInfoMap.put("msg","下载成功");
                    resInfoMap.put("downFile",filename);

                }catch(Exception e) {//会自动关闭资源 异常抛出给外层方法了不用catch 但是此处捕获一下
                    resInfoMap.put("status",2);
                    resInfoMap.put("msg","下载失败");
                }
            }
        }
//        return resInfoMap;

    }
}
