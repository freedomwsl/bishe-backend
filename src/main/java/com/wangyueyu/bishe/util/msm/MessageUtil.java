package com.wangyueyu.bishe.util.msm;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class MessageUtil {
    /**
     * 给远程第三方短信接口发送请求把验证码发送到用户手机上
     * @param host		短信接口调用的URL地址
     * @param path		具体发送短信功能的地址
     * @param method	请求方式
     * @param phoneNum	接收短信的手机号
     * @param appCode	用来调用第三方短信API的AppCode
     * @param sign		签名编号
     * @param skin		模板编号
     * @return 返回调用结果是否成功
     * 	成功：返回验证码
     * 	失败：返回失败消息
     * 	状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
     */
    public static Boolean sendCodeByShortMessage(

            String host,

            String path,

            String method,

            String phoneNum,

            String appCode,

            String sign,

            String skin,
            String code) {

        Map<String, String> headers = new HashMap<String, String>();

        // 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);

        // 封装其他参数
        Map<String, String> querys = new HashMap<String, String>();



        // 要发送的验证码，也就是模板中会变化的部分
        querys.put("param", code);

        // 收短信的手机号
        querys.put("phone", phoneNum);

        // 签名编号
        querys.put("sign", sign);

        // 模板编号
        querys.put("skin", skin);
        // JDK 1.8示例代码请在这里下载： http://code.fegine.com/Tools.zip

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(EntityUtils.toString(response.getEntity()));
            StatusLine statusLine = response.getStatusLine();

            // 状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            int statusCode = statusLine.getStatusCode();

            String reasonPhrase = statusLine.getReasonPhrase();

            if(statusCode == 200) {

                // 操作成功，把生成的验证码返回
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    public static Boolean send(String phoneNum,String code){
        String host = "https://fesms.market.alicloudapi.com";
        String path = "/sms/";
        String method = "GET";
        String appcode = "149169cfb6064caeba3d5bc7a7d656e4";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("code", code);
        querys.put("phone", phoneNum);
        querys.put("skin", "1");
        querys.put("sign", "175622");
        //JDK 1.8示例代码请在这里下载：  http://code.fegine.com/Tools.zip
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString());如不输出json, 请打开这行代码，打印调试头部状态码。
            //状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
            StatusLine statusLine = response.getStatusLine();
            // 状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            int statusCode = statusLine.getStatusCode();
            if(statusCode==200) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
