package com.miniprogramlearn.config;

import com.miniprogramlearn.entity.ApplicationValue;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**使用该注解表示它是一个配置类，需要在主配置类中再次进行导入这个配置类@Import
 * 但是可以在主配置类中引入xml的配置文件@Properties*/
@Configuration
//@ComponentScan(basePackages = "com.myresttemplate.config")
public class RestTemplateConfig {
    @Autowired//自动注入上面我们封装好的 配置文件中的信息
    private ApplicationValue appValue;

    /**设置日志的打印工具类，使用的是LoggerFactory来给  指定的类  创建日志
     * 这个日志打印工具是，springboot还是哪个依赖中所具有的一个日志打印*/
    private static final Logger logger= LoggerFactory.getLogger(RestTemplateConfig.class);

    @Bean(value = "restTemplate")//调用构造方法在容器框架中注入RestTemplate实例
    public RestTemplate restTemplate(){
        //return new RestTemplate(httpRequestFactory());
        //使用构造方法 创建restTemplate实例，参数是HttpClient，该类型可以使用默认的jdk也可以使用apache的httpClient（需要进行相应的配置
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());

        // 添加内容转换器,使用默认的内容转换器
        // 设置编码格式为UTF-8
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        //使用restTemplate获取所有的消息转换器HttpMessageConverter<?>（这个泛型类是所有消息转换器的超类）

        HttpMessageConverter<?> converterTarget = null;//声明一个临时的消息转换器

        for (HttpMessageConverter<?> item : converterList) {
            if (item.getClass() == StringHttpMessageConverter.class) {
                //在所有的restTemplate的自带的消息转换器中找到string的消息转换器StringHttpMessageConverter
                /**也就是是说，接收到的响应  或者  请求的报文和body都将在传输去和接收到的时候设置为UTF-8*/
                converterTarget = item;
                break;
            }
        }
        if (converterTarget != null) {
            converterList.remove(converterTarget);//在集合中去除默认的StringHttpMessageConverter就是上面代码中的converterTarget
        }

        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);  //声明自己的StringHttpMessageConverter，设置编码方式为UTF-8

        converterList.add(1,converter); //在集合中添加自定义的UTF-8的StringHttpMessageConverter


        //LOGGER.info("-----restTemplate-----初始化完成");

        //setMessageConverters 不过好像在return之前，并没有调用resttemplate的该set方法来将改变过的httpMessageConverter加入restTemplate中，这里应该代码写少了一句
        restTemplate.setMessageConverters(converterList);
        return restTemplate;
    }

    //调用构造方法在容器框架中注入ClientHttpRequestFactory实例(这是一个接口)一般用的是它的实现类来创建，我们一般使用HttpComponentsClientHttpRequestFactory
    //其实是因为改变了底层的客户端为apache的，所以需要将这个改变后的HttpComponentsClientHttpRequestFactory实例给容器代替之前容器内部默认的jdk的http客户端构建工厂，
    @Bean
    public ClientHttpRequestFactory httpRequestFactory(){
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }


    //调用在容器框架中注入HttpClient实例，
    //其实是因为改变了底层的客户端为apache的，所以需要将这个改变后的HttpClient实例给容器代替之前容器内部默认的jdk的http客户端构建工厂，
    @Bean
    public HttpClient httpClient(){
        /**第一步 registry注册器用于创建连接池*/
        /*public static <I> RegistryBuilder<I> create() {  //调用泛型的方法，需要在方法名前面使用泛型传入你需要的Java类类型名称
        return new RegistryBuilder();
    }*/
       // RegistryBuilder.build()生成 具有证书的Registry
        Registry<ConnectionSocketFactory> registry= RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();


        /**第二步*/
        /**使用具有证书的Registry 在PoolingHttpClientConnectionManager构造方法中生成连接池管理器connectionManager*/
        PoolingHttpClientConnectionManager connectionManager=new PoolingHttpClientConnectionManager(registry);
        /**
         *还有可以不用registry 如果不需要ssl证书的话，直接使用构造函数，传入对应参数建立连接池，如下：
         *长连接保持30秒  使用对应的参数new一个连接池管理器PoolingHttpClientConnectionManager
         *PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);*/



        //下面使用连接池管理器PoolingHttpClientConnectionManager set设置连接池的相关参数
        //这些参数的值从刚才将properties文件中的值封装在的java对象中获取
        /**设置整个连接池最大连接数 根据自己的场景决定*/
        connectionManager.setMaxTotal(appValue.getMaxTotal());
        /**同路由的并发数,路由是对maxTotal的细分*/
        connectionManager.setDefaultMaxPerRoute(appValue.getMaxPerRoute());
        connectionManager.setValidateAfterInactivity(appValue.getInactivity());
        //上面这些具体的时设置什么参数下去再次查阅学习

        /**第三步*/
        RequestConfig requestConfig=RequestConfig.custom()
        //服务器返回数据(response)的时间，超过抛出read timeout
                .setSocketTimeout(appValue.getSocketTimeout())
        //连接上服务器(握手成功)的时间，超出抛出connect timeout （握手就是与后端建立连接）
                .setConnectTimeout(appValue.getConnTimeOut())
        //从连接池中获取连接的超时时间，超时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
                .setConnectionRequestTimeout(appValue.getConnReqTimeOut())
                .build();

        //最后调用HttpClientBuilder.create()方法并且使用set设置相应的参数，创建httpclient实例并返回


        /**可以在httpclient建立的时候设置默认的请求头方式*/
        //headers
        List<Header> header = new ArrayList<>();
        header.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        header.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        header.add(new BasicHeader("Accept-Language", "zh-CN"));
        header.add(new BasicHeader("Connection", "Keep-Alive"));
        //headers.add(new BasicHeader("Content-type", "application/json;charset=UTF-8"));





        /**最后*/
        //最后调用HttpClientBuilder.create()方法并且使用set设置相应的参数，创建httpclient实例并返回
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .setDefaultHeaders(header)/**设置默认的	请求头*/
                /**保持长连接配置，需要在头中添加Keep-Alive   还要在此处set这个默认的new DefaultConnectionKeepAliveStrategy()*/
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                /**重试次数，默认是3次，没有开启new DefaultHttpRequestRetryHandler(2,true)*/
                .setRetryHandler(new DefaultHttpRequestRetryHandler(2,true))
                .build();

    }
}
