package com.miniprogramlearn.util;


import com.miniprogramlearn.entity.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class ReqUtil {

    @Resource
    private RestTemplate restTemplate;


    public void test(){
        System.out.println(restTemplate);
    }


    /**
     * 生成post请求的JSON请求参数
     * 请求示例:
     * {
     * "id":1,
     * "name":"张耀烽"
     * }
     *
     * @return
     */
//封装该方法配置的是restTemplate的相关配置，如httpHeaders等
//封装该方法是接收post请求的Map类型，之后内部配置相对应的请求头信息，并且使用map和请求头生成对应的HttpEntity<Map<String, String>> json请求参数 一般用在post请求中
    public HttpEntity<Map<String,String>> generatePostJson(Map<String,String> jsonMap){
        //如果需要其它的请求头信息、都可以在这里追加 httpHeaders.add()
        HttpHeaders headers=new HttpHeaders();

        //使用MediaType给请求头设置ContentType
        MediaType type=MediaType.parseMediaType("application/json;charset=UTF-8");


        /**public void setContentType(@Nullable MediaType mediaType)
         * //使用MediaType给请求头设置ContentType*/
        headers.setContentType(type);

        /**使用map和请求头生成对应的HttpEntity<Map<String, String>> json请求参数 */
        HttpEntity<Map<String,String>> httpEntity=new HttpEntity<>(jsonMap,headers);
        /**构造函数的第一个参数是什么类型，返回值的HttpEntity<>泛型就是什么类型*/

        return httpEntity;

    }

    /**
     * post请求、请求参数为json
     *
     * @return
     */

    public String sendPost(String url,Map<String ,String> jsonMap){
        /**发送restTemplate.postForEntity请求，返回的是ResponseEntity<String>实例
         * ResponseEntity<String>中的泛型类性根据postForEntity中的第三个响应体的类型决定*/

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, generatePostJson(jsonMap), String.class);

        /*ResponseEntity<String> responseEntity = restTemplate.postForEntity
                (
                        url,//请求url
                        generatePostJson(jsonMap),//传入body中的httpEntity参数(该参数设置了相应的信息)
                        String.class //响应body类型
                );*/

        //从响应中获取响应体 并返回 由于上面制定了响应体的类型，所以这里不需要强制进行转换为String
        return stringResponseEntity.getBody();
    }

    /** 这个是接收返回来的那个json数据并且自动转换  springboot内部本来就集成了jackson的json子哦对那个转换*/
    public User sendPostU(String url, Map<String ,String> jsonMap){
        /**发送restTemplate.postForEntity请求，返回的是ResponseEntity<String>实例
         * ResponseEntity<String>中的泛型类性根据postForEntity中的第三个响应体的类型决定*/

        ResponseEntity<User> stringResponseEntity = restTemplate.postForEntity(url, generatePostJson(jsonMap), User.class);

        /*ResponseEntity<String> responseEntity = restTemplate.postForEntity
                (
                        url,//请求url
                        generatePostJson(jsonMap),//传入body中的httpEntity参数(该参数设置了相应的信息)
                        String.class //响应body类型
                );*/

        //从响应中获取响应体 并返回 由于上面制定了响应体的类型，所以这里不需要强制进行转换为String
        return stringResponseEntity.getBody();
    }









    /**
     * 生成get参数请求url
     * 示例：https://0.0.0.0:80/api?u=u&o=o
     * 示例：https://0.0.0.0:80/api
     *
     * @param protocol 请求协议 示例: http 或者 https
     * @param uri      请求的uri 示例: 0.0.0.0:80
     * @param param   请求参数
     * @return
     */
    //封装该方法就是利用协议，主机端口号，和map类型的get参数生成一个带有参数的url  一般用在get请求中
    public String generateRequestParam(String protocol,String uri,Map<String,String> param){
        StringBuilder sb=new StringBuilder(protocol).append("://").append(uri);


        //使用ToolUtil.isNotEmpty()判断map类型的参数是否为空
        /**ToolUtil 工具下去学习查询*/
        if(!param.isEmpty()){
            sb.append("?");
            /**Map.Entry接口表示的是map键值对entry类性
             * params.entrySet()//返回的是map对应的
             * 所有entry键值对的一个集合，一个键值对中有key和value，获取方式如下getKey() getValue()
            *entry是一个集合，对应的是map集合*/
            for (Map.Entry map: param.entrySet()){
                sb.append(map.getKey())
                        .append("=")
                        .append(map.getValue())
                        .append("&");
            }
            uri=sb.substring(0,sb.length() - 1);
            return uri;//返回携带参数的url
        }
        return  sb.toString();//没有参数直接返回
    }

    /**
     * get请求、请求参数为?拼接形式的
     * <p>
     * 最终请求的URI如下：
     * <p>
     * http://127.0.0.1:80/?name=张耀烽&sex=男
     *
     * @return
     */
    //get请求方法
    public String sendGet(String protocol,String uri,Map<String,String> param ){

        /**<String>
         * 在ResponseEntity没有指出泛型类型，之后获取到的响应体是Object需要进行强制转换为String*/
        ResponseEntity forEntity = restTemplate.getForEntity
                (
                        generateRequestParam(protocol, uri, param),//第一个参数url
                        String.class//响应体的类型
                );
        return (String)forEntity.getBody();
    }


}
