package com.keller.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keller.common.http.RestTemplateUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author yangkaile
 * @date 2019-05-30 17:28:44
 * 阿里云地图API 工具
 */
public class AliyunMapUtils {
    /**
     * 阿里云地图API身份认证信息
     */
    private static final String APP_CODE = "c8b7ef0a794a4e7e96e6a309d150dd8c";

    /**
     * 阿里云地图API访问地址
     */
    private static final String URL = "https://regeo.market.alicloudapi.com/v3/geocode/regeo";

    /**
     * 阿里云地图API返回成功时的状态码
     */
    private static final String SUCCESS_CODE = "10000";
    /**
     * 返回的状态码关键字
     */
    private static final String INFO_CODE = "infocode";
    /**
     * 返回的消息内容关键字
     */
    private static final String REGEO_CODE = "regeocode";
    /**
     * 返回的具体地址关键字
     */
    private static final String FORMATTED_ADDRESS = "formatted_address";


    /**
     * 根据经纬度获取地址
     * @param longitude 经度 小数点后不超过6位 如 116.36486
     * @param latitude  纬度 小数点后不超过6位 如 40.084858
     * @return 地址 如：北京市昌平区霍营街道龙锦三街
     */
    private static String getAddress(float longitude, float latitude) {
        String url = URL + "?location=" + longitude + "," + latitude;
        String result = null;
        try {
            //Header中放入Authorization信息 ：AppCode
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "APPCODE " + APP_CODE);
            RestTemplate restTemplate = RestTemplateUtils.getInstance();
            //发送Get请求
            HttpEntity<String> requestEntity = new HttpEntity(null, httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            Console.println("http",response);
            if(response.getStatusCode() == HttpStatus.OK){
                result = getAddrByBody(response.getBody());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据经纬度获取地址
     * @param longitude 经度 如 116° 21' 53.5"
     * @param latitude  纬度 如 40° 5' 5.49"
     * @return 地址 如：北京市海淀区青龙桥街道颐和园
     */
    public static String getAddress(String longitude, String latitude ){
        return getAddress(pointToFloat(longitude),pointToFloat(latitude));
    }

    /**
     * 从返回的Body中解析出地址
     * {
     * 	"infocode":"10000",
     * 	"regeocode":{
     * 		"formatted_address":"北京市海淀区青龙桥街道颐和园",
     * 		"addressComponent":{
     * 			"businessAreas":[[]],
     * 			"country":"中国",
     * 			"province":"北京市",
     * 			"citycode":"010",
     * 			"city":[],
     * 			"adcode":"110108",
     * 			"streetNumber":{
     * 				"number":"甲1号",
     * 				"distance":"675.586",
     * 				"street":"六郎庄路",
     * 				"location":"116.278221,39.9850281",
     * 				"direction":"东南"},
     * 				"towncode":"110108013000",
     * 				"district":"海淀区",
     * 				"neighborhood":{
     * 					"name":"颐和园",
     * 					"type":"风景名胜;风景名胜;世界遗产"
     *                                },
     * 				"township":"青龙桥街道",
     * 				"building":{
     * 					"name":[],
     * 					"type":[]
     *                }
     * 		}
     * 	},
     * 	"status":"1",
     * 	"info":"OK"
     * }
     * @return 具体地址：如：北京市海淀区青龙桥街道颐和园
     */
    private static String getAddrByBody(String body){
        JSONObject jsonObject = JSON.parseObject(body);
        if(SUCCESS_CODE.equals(jsonObject.getString(INFO_CODE))){
            return jsonObject.getJSONObject(REGEO_CODE).getString(FORMATTED_ADDRESS);
        }else{
            return null;
        }
    }


    /**
     * 经纬度格式转换，从时分秒转换成float格式，保留小数点后6位
     * @param point 坐标点 格式：40° 5' 5.49"
     * @return 转换后的格式：40.084858
     */
    private static float pointToFloat (String point ) {
        Double du = Double.parseDouble(point.substring(0, point.indexOf("°")).trim());
        Double fen = Double.parseDouble(point.substring(point.indexOf("°")+1, point.indexOf("'")).trim());
        Double miao = Double.parseDouble(point.substring(point.indexOf("'")+1, point.indexOf("\"")).trim());
        Double duStr = du + fen / 60 + miao / 60 / 60 ;
        String str = duStr.toString();
        if(str.substring(str.indexOf(".") + 1).length() > 6){
            return Float.parseFloat(str.substring(0,str.indexOf(".") + 6 + 1));
        }
        return Float.parseFloat(str);
    }
}
