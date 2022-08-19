package com.open.onebyte.ratelimiter.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * NetUtils
 *
 * @author yangqk
 * @date 2020/11/18 14:52
 */
public class NetUtils {

    private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);

    private static final String UNKNOWN = "unknown";
    private static final String DELIMITER = ",";

    /**
     * 通过request对象获取IP地址
     *
     * @return ip
     */
    public static String getClientIpAddressByServlet() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return getClientIpAddress(request);
    }

    /**
     * 获取客户端的IP地址<br/>
     * 读取顺序：yuntai-real-ip, wl-proxy-client-ip, x-true-ip, X-Original-Forwarded-For,x-real-ip , X-Forwarded-For
     *
     * @return String 真实IP地址
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        // Yuntai-Real-IP
        String headerName = "yuntai-real-ip";
        String ip = request.getHeader(headerName);
        if (!isUnknown(ip)) {
            ip = request.getHeader(headerName);
        }
        if (isUnknown(ip)) {
            headerName = "wl-proxy-client-ip";
            ip = request.getHeader(headerName);
        }
        if (isUnknown(ip)) {
            headerName = "x-true-ip";
            ip = request.getHeader(headerName);
        }
        if (isUnknown(ip)) {
            headerName = "X-Original-Forwarded-For";
            ip = getMultistageReverseProxyIp(request.getHeader(headerName));
        }
        if (isUnknown(ip)) {
            headerName = "x-real-ip";
            ip = request.getHeader(headerName);
        }
        if (isUnknown(ip)) {
            headerName = "X-Forwarded-For";
            ip = getMultistageReverseProxyIp(request.getHeader(headerName));
        }
        if (isUnknown(ip)) {
            headerName = "Proxy-Client-IP";
            ip = request.getHeader(headerName);
        }
        if (isUnknown(ip)) {
            ip = request.getRemoteAddr();
        }
        logger.info("The ClientIp IP is " + ip + ", From headerName = " + headerName);
        return ip;
    }

    /**
     * 从多级反向代理中获得第一个非unknown IP地址
     *
     * @param ip 获得的IP地址
     * @return 第一个非unknown IP地址
     */
    public static String getMultistageReverseProxyIp(String ip) {
        // 多级反向代理检测
        if (StringUtils.isNotBlank(ip) && ip.indexOf(DELIMITER) > 0) {
            final String[] ips = ip.trim().split(DELIMITER);
            for (String subIp : ips) {
                if (!isUnknown(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 检测给定字符串是否为未知，多用于检测HTTP请求相关<br>
     *
     * @param checkString 被检测的字符串
     * @return 是否未知
     */
    public static boolean isUnknown(String checkString) {
        return StringUtils.isBlank(checkString) || UNKNOWN.equalsIgnoreCase(checkString);
    }
}
