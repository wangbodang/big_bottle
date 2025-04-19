package com.vefuture.big_bottle.common.util;

/**
 * @author wangb
 * @date 2025/4/19
 * @description IPUtils
 */
import javax.servlet.http.HttpServletRequest;

public class IpUtils {

    /**
     * 获取客户端真实 IP 地址。支持多级反向代理。
     * @param request HttpServletRequest
     * @return 客户端 IP
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = null;
        // 按优先级检查 Header
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",      // WebLogic
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR",
                "X-Real-IP"
        };
        for (String header : headers) {
            String headerValue = request.getHeader(header);
            if (headerValue != null && headerValue.length() != 0 && !"unknown".equalsIgnoreCase(headerValue)) {
                ip = headerValue;
                break;
            }
        }
        // 如果经过多级代理，X-Forwarded-For 可能是多个 IP，用第一个非 unknown 的
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        // 兜底
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 针对 IPv6 本地地址的兼容
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }
}

