package io.github.xbeeant.eoffice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobiao
 * @version 2021/7/3
 */
public class UrlHelper {
    private UrlHelper() {
        // do nothing
    }

    private static final Logger logger = LoggerFactory.getLogger(UrlHelper.class);

    public static String getServerDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String domain = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
        logger.debug("request domain {}", domain);
//        return "http://10.113.10.2:8080/";
        return domain;
    }

    /**
     * 解析url
     *
     * @param url url
     * @return {@link UrlEntity}
     * @see UrlEntity
     */
    public static UrlEntity parse(String url) {
        UrlEntity entity = new UrlEntity();
        if (url == null) {
            return entity;
        }
        url = url.trim();
        if ("".equals(url)) {
            return entity;
        }
        String[] urlParts = url.split("\\?");
        entity.baseUrl = urlParts[0];
        //没有参数
        if (urlParts.length == 1) {
            return entity;
        }
        //有参数
        String[] params = urlParts[1].split("&");
        entity.params = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            entity.params.put(keyValue[0], keyValue[1]);
        }

        return entity;
    }

    public static class UrlEntity {
        /**
         * 基础url
         */
        public String baseUrl;
        /**
         * url参数
         */
        public Map<String, String> params;
    }
}
