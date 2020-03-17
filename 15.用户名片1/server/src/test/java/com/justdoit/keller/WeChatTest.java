package com.justdoit.keller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.justdoit.keller.common.HttpsClientRequestFactory;
import com.justdoit.keller.common.util.Console;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WeChatTest {

    private RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new FastJsonHttpMessageConverter());

        return restTemplate;

    }
    private String appid = "wx36f2488857062cf2";
    private String appSecret = "0aa13f9fe640f23c85e9c25a80b4490d";


    private String templateId = "6VqrdswPyA4_Ez-MMSVVLtEFbmY-sxd1EtyNIOQR6jM";

    private String openId = "o1VE85NT4GTHyN86amSufGEJpGjw";

    private String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";


    private String sendMessageUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send";


    private String accessToken = "31_mApa3Ovb_0VMgE-ciNvL1jlP78Fb2dlOwCW3mbp8poTDk9vlXyx34Mvh0LmvR0nz6fo2yAEGEpaT5vf3BPvyZOfKNSvQrDLxq0maO9EMaoWwylq_AasvwgivqHmsIx2EWXyKswlVanrR9q3nAHKfAIAMTW";
    /**
     * <200,{"access_token":"31_8TPnAaOqr2hf6pCTAmyoSyAPKKqnmRRlZ1OGndIeIVneM6n3n3YSyVK9DCsCYYm8B5M8kwL28xVds0xU_DgSDn_FO9AwIC5Lok1ogIK3gcZeNs4hZ_N7mIMsV2WrBOpYGTztn-ojZJJM9wBKSIZcAHAGWN","expires_in":7200},
     * [Connection:"keep-alive", Content-Type:"application/json; encoding=utf-8", Date:"Tue, 17 Mar 2020 00:39:11 GMT", Content-Length:"194"]>
     */
    @Test
    public void getAccessToken(){
        String url = getTokenUrl +
                "?grant_type=client_credential&appid="+ appid
                + "&secret=" + appSecret;
        ResponseEntity responseEntity = restTemplate().getForEntity(url,String.class);
        Console.println("token",responseEntity);
    }

    /**
     * <200,{"errcode":43101,"errmsg":"user refuse to accept the msg hint: [Ldbzca0529shc1]"},
     * [Connection:"keep-alive", Content-Type:"application/json; encoding=utf-8", Date:"Tue, 17 Mar 2020 01:45:29 GMT", Content-Length:"81"]>
     */
    @Test
    public void sendMessage(){
        StringBuilder builder = new StringBuilder();
        builder.append(sendMessageUrl)
                .append("?access_token=").append(accessToken);

//                .append("&touser=").append(openId)
//                .append("&template_id=").append(templateId)
//                .append("&page=").append("index");
        String url = builder.toString();
        RestTemplate restTemplate = restTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType type=MediaType.parseMediaType("application/json;charset=UTF-8");
        httpHeaders.setContentType(type);

        Map<String,String> map = new HashMap<>();
        map.put("touser",openId);
        map.put("template_id",templateId);
        map.put("data.time1.value","2015年01月05日");
        map.put("data",getJson());

        HttpEntity<Map<String,String>> httpEntity = new HttpEntity<>(map,httpHeaders);

        ResponseEntity responseEntity = restTemplate().postForEntity(url,httpEntity,String.class);
        Console.println("sendMessage",responseEntity);
    }


    public String getJson(){
        JSONObject jsonObject =  new JSONObject();
        JSONObject time = new JSONObject();
        time.put("value","2015年01月05日");
        JSONObject dataJson = new JSONObject();
        dataJson.put("time1",time);
        JSONObject phrase = new JSONObject();
        phrase.put("value","2015年01月05日");
        dataJson.put("phrase2",phrase);

        jsonObject.put("data",dataJson);
        Console.println("getJson",jsonObject.toString());

        return jsonObject.toString();
    }



    public String getArgsJSONStr() {
        JSONObject args = new JSONObject();
        args.put("touser", "");
        args.put("template_id", "");
        args.put("url", "");
        //得到的关于DATA的参数数据转化成JSON
        JSONObject data = JSON.parseObject("");
        //把每个详细数据转化成JSON,fisrt,keyword1,keyword2....,remark
        Set<String> keySet = data.keySet();
        for(String key:keySet)
        {
            //吧具体数据转化成JSON然后重新放回去
            JSONObject keyvalue = JSON.parseObject(data.get(key).toString());
            data.put(key,keyvalue);
        }
        args.put("data", data);
        //最后整体数据进行转化为JSON格式用于传递给微信使用
        String argsJSONStr = JSON.toJSONString(args);
        return argsJSONStr;
    }
}
