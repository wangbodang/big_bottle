package com.vefuture.big_bottle.common.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.vefuture.big_bottle.common.manager.AsyncManager;
import com.vefuture.big_bottle.common.manager.factory.AsyncFactory;
import com.vefuture.big_bottle.common.util.ExceptionUtil;
import com.vefuture.big_bottle.common.util.BlockUtils;
import com.vefuture.big_bottle.common.util.StringUtils;
import com.vefuture.big_bottle.common.util.http.ServletUtils;
import com.vefuture.big_bottle.common.util.ip.AddressUtils;
import com.vefuture.big_bottle.common.util.ip.IpUtils;
import com.vefuture.big_bottle.common.util.spring.SpringUtils;
import com.vefuture.big_bottle.common.util.text.Convert;
import com.vefuture.big_bottle.web.system.entity.OperLog;
import com.vefuture.big_bottle.web.system.service.OperLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author wangb
 * @date 2025/4/23
 * @description TODO: 类描述
 */
@Slf4j
@Component
public class AccessLogInterceptor implements HandlerInterceptor {

    /** 计算操作消耗时间 */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");
    //
    private static final ThreadLocal<Long> OPER_LOG_ID = new NamedThreadLocal<Long>("oper_log_id");



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
        // 1) 预取一个 sequence 值
        Long fakeId = SpringUtils.getBean(OperLogService.class).nextSequenceId();
        OPER_LOG_ID.set(fakeId);

        //log.info("--------------------------------------->>>>>>>>> 经过日志拦截器:[{}]", this.getClass().getName());
        String ipAddr = IpUtils.getIpAddr(request);
        //String locationAddr = AddressUtils.getRealAddressByIP(ipAddr);
        String locationAddr = "";
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();

        OperLog operLog = new OperLog();
        //用库里的ID
        operLog.setId(fakeId);
        operLog.setOperIp(ipAddr);
        operLog.setOperLocation(locationAddr);
        operLog.setOperUrl(requestURI);
        operLog.setOperTime(LocalDateTime.now());
        //异步执行存到数据库
        AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
        //存储日志ID
        /*
        log.info("----> 生成的Log id:{}", BlockUtils.getBlock(operLog.getId()));
        OPER_LOG_ID.set(operLog.getId());
        */
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        try {
            OperLog operLog = new OperLog();
            Long beginTimm = TIME_THREADLOCAL.get();
            Long operLogId = OPER_LOG_ID.get();
            //log.info("---> 在ThreadLocal里存储的operLogId:{}", BlockUtils.getBlock(operLogId));
            operLog.setId(operLogId);

            if (ex != null){
                //操作状态（0正常 1异常）
                operLog.setStatus((short)1);
                operLog.setErrorMsg(StringUtils.substring(Convert.toStr(ex.getMessage(), ExceptionUtil.getExceptionMessage(ex)), 0, 2000));
                // 设置请求方式
                operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
                // 处理设置注解上的参数
                //getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            }
            // 设置消耗时间
            operLog.setCostTime(System.currentTimeMillis() - beginTimm);

            if(ObjectUtil.isEmpty(operLogId)){
                //log.info("---> 执行save");
                //异步执行存到数据库
                AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
            }else {
                //log.info("---> 执行update");
                //异步执行存到数据库
                AsyncManager.me().execute(AsyncFactory.updateOper(operLog));
            }

        } catch (Exception exp){
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        } finally {
            TIME_THREADLOCAL.remove();
            OPER_LOG_ID.remove();
        }
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
