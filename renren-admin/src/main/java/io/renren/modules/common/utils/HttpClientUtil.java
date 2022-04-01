package io.renren.modules.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Date;


/**
 * @author cxy
 * @create 2022/3/30
 */
public class HttpClientUtil {
    private static Log logger = LogFactory.getLog(HttpClientUtil.class);

    public static void main(String[] args) {
        String imgFilePath = "D:\\ai_demo\\test_pic\\1.jpeg";
        String outImgFilePath = "D:\\ai_demo\\out1\\";
        Date picDate = new Date();
        Long deviceId = 1494600627670908929l;
        analysisImg(imgFilePath, outImgFilePath, deviceId, picDate);

    }



    /**
     * 图片分析处理 test
     * @param imgFilePath 图片地址
     * @param outImgFilePath 分析后图片保存地址
     * @param deviceId 设备编号
     * @param picDate 拍照时间
     */
//    @Async
    public static void analysisImg(String imgFilePath, String outImgFilePath, Long deviceId, Date picDate) {
        try {
            String interfacePath = "http://localhost:8080/renren-admin/discernConfig/kxdiscernconfighd/analysisImg";
            JSONObject parameters = new JSONObject();
            parameters.put("imgFilePath", imgFilePath);
            parameters.put("outImgFilePath", outImgFilePath);
            parameters.put("deviceId", deviceId);
            parameters.put("picDate", picDate);
            String str = HttpClientUtil.postMethod(interfacePath, parameters);
            System.err.println(str);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("error"+e);
        }
    }


    /**
     * @param interfacePath
     * @param parameters
     * @return
     */
    public static String postMethod(String interfacePath, JSONObject parameters) {
        String url = interfacePath;
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient client = builder.build();
        HttpPost post = new HttpPost(url);
        post.addHeader("Accept", "application/json");
        post.addHeader("Content-Type", "application/json;charset=utf-8");

        //设置请求连接超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(3000).setConnectTimeout(5000)
                .setSocketTimeout(20000).build();
        post.setConfig(requestConfig);

        StringEntity requestEntity = null;
        requestEntity = new StringEntity(parameters.toString(), "UTF-8");
        post.setEntity(requestEntity);
        CloseableHttpResponse response = null;
        String str = "";
        try {
            response = client.execute(post);
            HttpEntity entity = response.getEntity();
            str = EntityUtils.toString(entity, "utf8");
            logger.info("the response is : " + str);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }
}
