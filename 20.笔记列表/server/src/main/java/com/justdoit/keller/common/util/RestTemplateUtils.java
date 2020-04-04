package com.justdoit.keller.common.util;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.justdoit.keller.common.HttpsClientRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author yangkaile
 * @date 2020-03-19 09:33:03
 */
public class RestTemplateUtils {

    public static RestTemplate getHttpsTemplate(){
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        return init(restTemplate);
    }
    public static RestTemplate getTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        return init(restTemplate);
    }

    private static RestTemplate init(RestTemplate restTemplate){
        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new FastJsonHttpMessageConverter());
        return restTemplate;
    }
}
