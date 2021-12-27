package io.github.xbeeant.eoffice.util;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.xbeeant.core.JavaBeansHelper;
import io.github.xbeeant.core.JsonHelper;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author xiaobiao
 * @version 2021/11/20
 */
public class AntDesignUtil {

    private static final Type MAP_TYPE = new TypeReference<Map<String, String>>() {
    }.getType();

    public static String translateOrder(String sorter) {
        if (StringUtils.isEmpty(sorter) || "{}".equals(sorter)) {
            return "";
        }

        Map<String, String> map = JsonHelper.toObject(sorter, MAP_TYPE);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> item : map.entrySet()) {
            sb.append(JavaBeansHelper.toUnderline(item.getValue()));
            sb.append("_");
            sb.append("ascend".equals(map.get(item.getValue())) ? "asc" : "desc");
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
