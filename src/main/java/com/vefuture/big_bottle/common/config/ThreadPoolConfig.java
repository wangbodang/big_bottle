package com.vefuture.big_bottle.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;
/**
 * @author wangb
 * @date 2025/3/26
 * @description 配置线程池
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(value = "threadPoolExecutor")
    public ExecutorService threadPoolExecutor() {
        int corePoolSize = 5;                         // 核心线程数
        int maximumPoolSize = 10;                     // 最大线程数
        long keepAliveTime = 60;                      // 空闲线程存活时间
        TimeUnit unit = TimeUnit.SECONDS;             // 时间单位（秒）
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(300); // 任务队列，容量为100

        //ThreadFactory threadFactory = Executors.defaultThreadFactory();     // 默认线程工厂
        //加上自定义的 ThreadFactory，为线程命名，便于日志排查
        ThreadFactory threadFactory = r -> new Thread(new ThreadGroup("bottle-pool"), r, "bottle-thread-" + r.hashCode());

        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy(); // 拒绝策略

        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );
    }
}
