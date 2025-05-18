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
 * submitTask的时候附带超时参数（比如超5分钟自动取消）；
 *
 * submitTask返回一个【异步监听器】，任务完成后自动回调处理；
 *
 * 如何在页面（前端）上实时轮询任务进度（比如拿taskId查询状态）？
 */
@Slf4j
@Component

public class TaskManager {

    private final Map<String, TaskInfo> taskStatusMap = new ConcurrentHashMap<>();
    private final Map<String, Future<?>> taskFutureMap = new ConcurrentHashMap<>();
    @Autowired
    @Qualifier(value = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor executor;
    @Autowired
    @Qualifier(value = "singleThreadExecutor") // 引用单线程池
    private ExecutorService serialExecutor;

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
     * 提交一个“顺序执行”的任务（顺序线程池）
     */
    public String submitSerialTask(String taskId, Runnable task) {
        taskStatusMap.put(taskId, new TaskInfo(TaskStatus.PENDING, null));
        Future<?> future = serialExecutor.submit(() -> {
            try {
                updateStatus(taskId, TaskStatus.RUNNING, null);
                task.run();
                updateStatus(taskId, TaskStatus.SUCCESS, null);
            } catch (Exception e) {
                updateStatus(taskId, TaskStatus.FAILED, e.getMessage());
                log.error("Serial Task {} 执行失败", taskId, e);
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
