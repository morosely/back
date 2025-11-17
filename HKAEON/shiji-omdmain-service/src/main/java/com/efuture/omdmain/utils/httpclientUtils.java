package com.efuture.omdmain.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class httpclientUtils {
    private final static Logger logger = LoggerFactory.getLogger(httpclientUtils.class);

    public static String post(String url, String params) {

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        HttpPost post = new HttpPost(url);
        String result = "";
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(30000).setConnectionRequestTimeout(30000)
                .setSocketTimeout(30000).build();
        post.setConfig(requestConfig);
        try (CloseableHttpClient closeableHttpClient = httpClientBuilder.build()) {
            HttpEntity entity = new StringEntity(params, "UTF-8");
            post.setEntity(entity);
            post.setHeader("Content-type", "application/json");
            HttpResponse resp = closeableHttpClient.execute(post);
            try {
                InputStream respIs = resp.getEntity().getContent();
                byte[] respBytes = IOUtils.toByteArray(respIs);
                result = new String(respBytes, Charset.forName("UTF-8"));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return result;
        } catch (IOException e) {
            logger.error("发送请求异常", e);
            return null;
            //logger.error(e.getMessage(),e);
        }
    }


}