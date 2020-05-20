package com.keller.common.http;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

/**
 * @author yangkaile
 * @date 2020-05-18 14:21:54
 * 组件路由
 */
public class ServerRouter {

    /**
     * 用户服务
     */
    public static final String USER = "User";

    /**
     * 代理服务
     */
    public static final String PROXY = "Proxy";

    /**
     * 笔记服务
     */
    public static final String NOTE = "Note";

    /**
     * 日志服务
     */
    public static final String LOG = "Log";


    /**
     * 获取服务的 Url
     * @param loadBalancer
     * @param service
     * @return
     */
    public static String getServeUrl(LoadBalancerClient loadBalancer,String service){
        ServiceInstance serviceInstance = null;

        switch (service){
            case USER : serviceInstance = loadBalancer.choose(USER);break;
            case PROXY : serviceInstance = loadBalancer.choose(PROXY);break;
            case NOTE : serviceInstance = loadBalancer.choose(NOTE);break;
            case LOG : serviceInstance = loadBalancer.choose(LOG);break;
            default: break;
        }
        if(serviceInstance == null){
            return null;
        }
        return serviceInstance.getUri().toString();
    }


}
