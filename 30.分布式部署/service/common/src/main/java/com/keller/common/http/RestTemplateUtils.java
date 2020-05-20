package com.keller.common.http;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.keller.common.util.ErrorLogUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author yangkaile
 * @date 2020-03-19 09:33:03
 */
public class RestTemplateUtils {

    private static RestTemplate instance = null;

    private RestTemplateUtils(){
    }
    public static RestTemplate getInstance(){
        if(instance == null){
            synchronized (ErrorLogUtils.class){
                if(instance == null){
                    instance = new RestTemplate();
                    instance.getMessageConverters().clear();
                    instance.getMessageConverters().add(new FastJsonHttpMessageConverter());
                }
            }
        }
        return instance;
    }


    public static RestTemplate getHttpsTemplate(){
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        return init(restTemplate);
    }

    private static RestTemplate init(RestTemplate restTemplate){
        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new FastJsonHttpMessageConverter());
        return restTemplate;
    }
}
