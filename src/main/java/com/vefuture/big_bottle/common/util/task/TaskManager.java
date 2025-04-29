package com.vefuture.big_bottle.common.util.task;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
/**
 * @author wangb
 * @date 2025/4/29
 * @description 任务管理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskManager {

    private final Map<String, TaskInfo> taskStatusMap = new ConcurrentHashMap<>();
    private final Map<String, Future<?>> taskFutureMap = new ConcurrentHashMap<>();
    @Qualifier(value = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor executor;

    //自己创建一个线程池
    /*
    @PostConstruct
    public void init() {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("TaskManager-");
        executor.initialize();
    }
    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }
    */

    /**
     * 提交一个异步任务（自动生成taskId）
     */
    public String submitTask(Runnable task) {
        String taskId = UUID.randomUUID().toString();
        return submitTask(taskId, task);
    }

    /**
     * 提交一个异步任务（指定taskId）
     */
    public String submitTask(String taskId, Runnable task) {
        taskStatusMap.put(taskId, new TaskInfo(TaskStatus.PENDING, null));
        Future<?> future = executor.submit(() -> {
            try {
                updateStatus(taskId, TaskStatus.RUNNING, null);
                task.run();
                updateStatus(taskId, TaskStatus.SUCCESS, null);
            } catch (Exception e) {
                updateStatus(taskId, TaskStatus.FAILED, e.getMessage());
                log.error("Task {} execution failed", taskId, e);
            }
        });
        taskFutureMap.put(taskId, future);
        return taskId;
    }

    /**
     * 查询任务状态
     */
    public TaskInfo queryTask(String taskId) {
        return taskStatusMap.get(taskId);
    }

    /**
     * 取消任务
     */
    public boolean cancelTask(String taskId) {
        Future<?> future = taskFutureMap.get(taskId);
        if (future != null) {
            boolean cancelled = future.cancel(true);
            if (cancelled) {
                updateStatus(taskId, TaskStatus.CANCELLED, null);
            }
            return cancelled;
        }
        return false;
    }

    //更新任务状态
    private void updateStatus(String taskId, TaskStatus status, String errorMessage) {
        TaskInfo info = taskStatusMap.get(taskId);
        if (info != null) {
            info.setStatus(status);
            info.setErrorMessage(errorMessage);
        }
    }

    //任务状态信息
    @Data
    public static class TaskInfo {
        private TaskStatus status;
        private String errorMessage;

        public TaskInfo(TaskStatus status, String errorMessage) {
            this.status = status;
            this.errorMessage = errorMessage;
        }
    }
    //任务状态枚举码
    public enum TaskStatus {
        PENDING, RUNNING, SUCCESS, FAILED, CANCELLED
    }
}
