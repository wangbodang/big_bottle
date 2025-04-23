package com.vefuture.big_bottle.common.manager.factory;








//import com.ruoyi.system.domain.SysLogininfor;
//import com.ruoyi.system.domain.SysOperLog;
//import com.ruoyi.system.service.ISysLogininforService;
//import com.ruoyi.system.service.ISysOperLogService;
import com.vefuture.big_bottle.common.constants.Constants;
import com.vefuture.big_bottle.common.util.BlockUtils;
import com.vefuture.big_bottle.common.util.StringUtils;
import com.vefuture.big_bottle.common.util.http.ServletUtils;
import com.vefuture.big_bottle.common.util.ip.AddressUtils;
import com.vefuture.big_bottle.common.util.ip.IpUtils;
import com.vefuture.big_bottle.common.util.spring.SpringUtils;
import com.vefuture.big_bottle.web.system.entity.LoginInfo;
import com.vefuture.big_bottle.web.system.entity.OperLog;
import com.vefuture.big_bottle.web.system.service.LoginInfoService;
import com.vefuture.big_bottle.web.system.service.OperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 * 
 * @author ruoyi
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     * 
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
            final Object... args)
    {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(BlockUtils.getBlock(ip));
                s.append(address);
                s.append(BlockUtils.getBlock(username));
                s.append(BlockUtils.getBlock(status));
                s.append(BlockUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                LoginInfo logininfor = new LoginInfo();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
                {
                    logininfor.setStatus(Constants.SUCCESS);
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(LoginInfoService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 操作日志记录
     * 
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final OperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                //operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(OperLogService.class).insertOperlog(operLog);
            }
        };
    }

    public static TimerTask updateOper(final OperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                //operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(OperLogService.class).updateOperlogById(operLog);
            }
        };
    }
}
