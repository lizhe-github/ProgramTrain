package com.miniprogramlearn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/upload")
public class UploadFileController {

    @PostMapping("/actImg")
    @ResponseBody
    public Map<String,Object> actImg(List<MultipartFile> imgFilez) throws Exception {
//        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String imgFilePath = this.getClass().getResource("/static/uploadimg/").getPath();  //类路径的根目录下的static中的文件夹

        List<String> imgList=new LinkedList<String>();
        List<Integer> existNum=new LinkedList<>();

        Map<String,Object> resInfoMap=new HashMap<>(9);

        for (int i = 0; i < imgFilez.size(); i++) {
            MultipartFile file = imgFilez.get(i);
            if (file==null||file.isEmpty()) {

                resInfoMap.put("status",3);
                resInfoMap.put("errmsg","上传第" + (i+1) + "个文件失败");
                return resInfoMap;
            }

            String fileName = file.getOriginalFilename();
//            System.out.println(fileName);
            File imgDest = new File(imgFilePath + fileName);

            if (imgDest.exists()) { //判断文件是否存在，存在则不上传
                existNum.add(i+1);
                resInfoMap.put("warnmsg","上传第" + existNum.toString() + "个文件已存在");
                imgList.add("http://localhost:8088/mini/uploadimg/"+fileName);
//                System.out.println("存在");
            }else{
                try {
                    file.transferTo(imgDest);
                    imgList.add("http://localhost:8088/mini/uploadimg/"+fileName);
                    //LOGGER.info("第" + (i + 1) + "个文件上传成功");
                } catch (IOException e) {
                    //LOGGER.error(e.toString(), e);
                    resInfoMap.put("status",3);
                    resInfoMap.put("errmsg","上传第" + (i+1) + "个文件失败");
                    return resInfoMap;
                }
            }
        }
        resInfoMap.put("status",1);
        resInfoMap.put("msg","上传成功");
        resInfoMap.put("imgList",imgList);

//        下面注释是上传单个文件
        /*if (file == null || file.isEmpty()) {
            throw new Exception("未选择需上传的日志文件");
        }

        File fileUpload = new File(filePath);
        if (!fileUpload.exists()) {
            fileUpload.mkdirs(); //创建目录
        }

        fileUpload = new File(filePath, file.getOriginalFilename());
        if (fileUpload.exists()) {
            throw new Exception("上传的日志文件已存在");
        }

        try {
            file.transferTo(fileUpload);

            return
        } catch (IOException e) {
            throw new Exception("上传日志文件到服务器失败：" + e.toString());
        }*/

        return resInfoMap;
    }

}
