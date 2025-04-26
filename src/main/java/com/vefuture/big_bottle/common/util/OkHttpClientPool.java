package com.vefuture.big_bottle.common.util;

import okhttp3.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author wangb
 * @date 2025/4/26
 * @description TODO: 类描述
 */

/**
 * OkHttpClient 池化管理工具
 */
public class OkHttpClientPool {
    // 存储不同配置的 OkHttpClient
    private static final Map<String, OkHttpClient> CLIENT_POOL = new ConcurrentHashMap<>();

    // 默认 Client 的 key
    private static final String DEFAULT_KEY = "default";

    static {
        CLIENT_POOL.put(DEFAULT_KEY, createDefaultClient());
    }

    /**
     * 创建默认配置的 OkHttpClient
     */
    private static OkHttpClient createDefaultClient() {
        ExecutorService threadPool = new ThreadPoolExecutor(
                10, 200,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        );

        Dispatcher dispatcher = new Dispatcher(threadPool);
        dispatcher.setMaxRequests(256);
        dispatcher.setMaxRequestsPerHost(100);

        ConnectionPool connectionPool = new ConnectionPool(100, 1, TimeUnit.MINUTES);

        Dns customDns = hostname -> Arrays.asList(InetAddress.getAllByName(hostname));

        return new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .dns(customDns)
                .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Connection", "keep-alive")
                            .build();
                    return chain.proceed(request);
                })
                .build();
    }

    /**
     * 获取默认 Client
     */
    public static OkHttpClient getClient() {
        return getClient(DEFAULT_KEY);
    }

    /**
     * 按 key 获取指定 Client
     */
    public static OkHttpClient getClient(String key) {
        return CLIENT_POOL.get(key);
    }

    /**
     * 注册一个新的 OkHttpClient
     * 如果 key 已存在则覆盖
     */
    public static void registerClient(String key, OkHttpClient client) {
        CLIENT_POOL.put(key, client);
    }

    /**
     * 删除一个指定配置的 Client
     */
    public static void removeClient(String key) {
        if (!DEFAULT_KEY.equals(key)) {
            CLIENT_POOL.remove(key);
        }
    }

    /**
     * 发送同步 GET 请求（默认 Client）
     */
    public static String getSync(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = getClient().newCall(request).execute()) {
            return response.body() != null ? response.body().string() : null;
        }
    }

    /**
     * 发送异步 GET 请求（默认 Client）
     */
    public static void getAsync(String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        getClient().newCall(request).enqueue(callback);
    }

    // 同步 POST、异步 POST 也可以仿照 get 写

}
/*

// 直接用默认 Client
String result = OkHttpClientPool.getSync("https://example.com");

// 注册一个新的不同超时配置的 Client
OkHttpClient fastTimeoutClient = new OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build();
OkHttpClientPool.registerClient("fastTimeout", fastTimeoutClient);

// 用 fastTimeout 的 client 发送请求
OkHttpClient client = OkHttpClientPool.getClient("fastTimeout");
Request request = new Request.Builder().url("https://example.com").build();
try (Response response = client.newCall(request).execute()) {
    String body = response.body() != null ? response.body().string() : null;
    System.out.println(body);
}


*/

