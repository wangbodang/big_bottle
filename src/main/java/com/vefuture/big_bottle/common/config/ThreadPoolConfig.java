package com.vefuture.big_bottle.common.config;

import com.vefuture.big_bottle.common.config.prop.BigBottleProperties;
import com.vefuture.big_bottle.common.config.prop.ThreadPoolProperties;
import com.vefuture.big_bottle.common.util.ThreadUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * @author wangb
 * @date 2025/3/26
 * @description 配置线程池
 */
@Configuration
public class ThreadPoolConfig
{
    @Autowired
    private ThreadPoolProperties threadPoolProperties;
    // 核心线程池大小
    //private int corePoolSize = 50;
    private int corePoolSize = 5;
    // 最大可创建的线程数
    //private int maxPoolSize = 200;
    private int maxPoolSize = 10;
    // 队列最大长度
    private int queueCapacity = 1000;
    // 线程池维护线程所允许的空闲时间
    private int keepAliveSeconds = 300;

    /**
     * 普通线程池
     * @return
     */
    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     *
     * 单线程池
     * @return
     */
    @Bean(name = "singleThreadExecutor")
    public ExecutorService singleThreadExecutor() {
        return new ThreadPoolExecutor(
                1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(100),  // 可排队100个任务，超过将被拒绝
                r -> {
                    Thread t = new Thread(r);
                    t.setName("serial-executor-thread");
                    return t;
                },
                new ThreadPoolExecutor.AbortPolicy()  // 超过100个任务直接报错
        );
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService()
    {
        return new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy())
        {
            @Override
            protected void afterExecute(Runnable r, Throwable t)
            {
                super.afterExecute(r, t);
                ThreadUtils.printException(r, t);
            }
        };
    }
}

