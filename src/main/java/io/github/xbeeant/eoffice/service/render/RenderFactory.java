package io.github.xbeeant.eoffice.service.render;

import io.github.xbeeant.spring.web.SpringContextProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobiao
 * @date 2022/1/6
 */
public class RenderFactory {

    private static final Map<String, AbstractRenderService> SERVICE_MAP = new HashMap<>();

    static {
        SERVICE_MAP.put("office", SpringContextProvider.getBean(OfficeRenderServiceImpl.class));
    }

    private RenderFactory() {
        // do nothing
    }

    public static AbstractRenderService getRender(String type) {
        AbstractRenderService bean = SERVICE_MAP.get(type);
        if (null == bean) {
            return SpringContextProvider.getBean(DefaultRenderServiceImpl.class);
        }

        return bean;
    }
}
