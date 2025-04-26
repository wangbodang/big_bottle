package com.vefuture.big_bottle.common.util;

import okhttp3.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.*;

/**
 * @author wangb
 * @date 2025/3/15
 * @description TODO: 类描述
 */
public class OkHttpUtil {

    /// 全局唯一 OkHttpClient 实例，线程安全，推荐单例使用
    private static final OkHttpClient CLIENT;

    static {
        // 线程池
        ExecutorService threadPool = new ThreadPoolExecutor(
                10, 200, // 核心线程数 & 最大线程数
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        );

        // Dispatcher 控制并发
        Dispatcher dispatcher = new Dispatcher(threadPool);
        dispatcher.setMaxRequests(256); // 允许 256 个并发请求
        dispatcher.setMaxRequestsPerHost(100); // 单个主机支持 100 并发

        // 连接池优化
        ConnectionPool connectionPool = new ConnectionPool(100, 1, TimeUnit.MINUTES);

        // 自定义 DNS（避免缓存 DNS，适用于负载均衡）
        Dns customDns = hostname -> Arrays.asList(InetAddress.getAllByName(hostname));

        // 构建优化后的 OkHttpClient
        CLIENT = new OkHttpClient.Builder()
                //.protocols(Arrays.asList(Protocol.HTTP_1_1))
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .dns(customDns) // 关闭 DNS 缓存，适用于负载均衡
                .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1)) // 支持 HTTP/2
                .connectTimeout(10, TimeUnit.SECONDS)  // 连接超时
                .readTimeout(30, TimeUnit.SECONDS)     // 读取超时
                .writeTimeout(30, TimeUnit.SECONDS)    // 写入超时
                .addInterceptor(chain -> { // 启用 Gzip 压缩 & Keep-Alive
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            //.header("Accept-Encoding", "gzip") //这里启用压缩提商效率, 但处理起来麻烦
                            .header("Connection", "keep-alive")
                            .build();
                    return chain.proceed(request);
                })
                .build();
    }

    /**
     * 获取client
     *
     * @param
     * @return 返回值说明
     */

    public static OkHttpClient getClient() {
        return CLIENT;
    }

    /**
     * 发送同步 GET 请求
     */
    public static String getSync(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = CLIENT.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : null;
        }
    }

    /**
     * 发送异步 GET 请求
     */
    public static void getAsync(String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        CLIENT.newCall(request).enqueue(callback);
    }

    /**
     * 发送同步 POST 请求
     */
    public static String postSync(String url, RequestBody body) throws IOException {
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = CLIENT.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : null;
        }
    }

    /**
     * 发送异步 POST 请求
     */
    public static void postAsync(String url, RequestBody body, Callback callback) {
        Request request = new Request.Builder().url(url).post(body).build();
        CLIENT.newCall(request).enqueue(callback);
    }
}
