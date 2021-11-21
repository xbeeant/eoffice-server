package io.github.xbeeant.eoffice.util;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaobiao
 * @version 2021/11/20
 */
public class AntDesignUtil {
    private static final Gson gson = new Gson();
    private static Pattern p = Pattern.compile("[A-Z]");

    public static String translateOrder(String sorter) {
        if (StringUtils.isEmpty(sorter) || "{}".equals(sorter)) {
            return "";
        }

        Map<String, String> map = gson.fromJson(sorter, Map.class);
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            sb.append(camel4underline(key));
            sb.append("_");
            sb.append("ascend".equals(map.get(key)) ? "asc" : "desc");
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String camel4underline(String param) {
        if (param == null || "".equals(param)) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }

        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }
}
