package com.miniprogramlearn.entity;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
        //@PropertySource(value = "classpath:appvalue.yml")
public class ApplicationValue {

    @Value("${http_pool.max_total}")//读取properties中的值，并且与对应的java对象的字段映射
    private int maxTotal;

    @Value("${http_pool.default_max_per_route}")
    private int maxPerRoute;

    @Value("${http_pool.connect_timeout}")
    private int connTimeOut;

    @Value("${http_pool.connection_request_timeout}")
    private int connReqTimeOut;

    @Value("${http_pool.socket_timeout}")
    private int socketTimeout;

    @Value("${http_pool.validate_after_inactivity}")
    private int inactivity;

    @Value("${mini.appid}")
    private String appid;

    @Value("${mini.app_secret}")
    private String appSecret;

    @Value("${mini.grant_type}")
    private String grantType;

}
