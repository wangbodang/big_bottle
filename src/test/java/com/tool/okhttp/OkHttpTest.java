package com.tool.okhttp;

import com.alibaba.fastjson.JSONObject;
import com.vefuture.big_bottle.common.util.OkHttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author wangb
 * @date 2025/3/15
 * @description : 测试 OKHttp
 */
public class OkHttpTest {


    /**
     * 两个免费接口
     * https://reqres.in/api/users/2
     * https://jsonplaceholder.typicode.com/users/10
     *
     * @param
     * @return  void
     */

    @Test
    public void testOkHttp(){
        // 创建 OkHttpClient
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OkHttpUtil.getClient();

        // 定义请求
        Request request = new Request.Builder()
                .url("https://jsonplaceholder.typicode.com/users/1") // API 地址
                .get() // GET 请求
                .build();

        // 执行请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Unexpected code: " + response);
                return;
            }
            //有压缩 content-encoding: gzip
            System.out.println("Response Headers: " + response.headers());

            /*ResponseBody body = response.body();
            if (body != null) {
                String contentEncoding = response.header("Content-Encoding");
                String responseBody;
                if ("gzip".equalsIgnoreCase(contentEncoding)) {
                    responseBody = new GZIPInputStream(body.byteStream()).readAllBytes().toString();
                } else {
                    responseBody = body.string();
                }
                System.out.println("Response Body: " + responseBody);
            }*/

            // 解析 JSON 响应
            String responseBody = response.body().string();
            JSONObject json = JSONObject.parseObject(responseBody);

            // 输出结果
            System.out.println("User ID: " + json.getInteger("id"));
            System.out.println("Name: " + json.getString("name"));
            System.out.println("Email: " + json.getString("email"));
        } catch (IOException e) {
            //e.printStackTrace();
        }

    }
}
