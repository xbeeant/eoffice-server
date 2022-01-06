package io.github.xbeeant.eoffice.service.storage;

import io.github.xbeeant.spring.web.SpringContextProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobiao
 * @date 2022/1/6
 */
public class StorageFactory {

    private static final Map<String, AbstractStorageService> SERVICE_MAP = new HashMap<>();

    static {
        SERVICE_MAP.put("pdf", SpringContextProvider.getBean(FileStorageServiceImpl.class));
        SERVICE_MAP.put("markdown", SpringContextProvider.getBean(DatabaseStorageServiceImpl.class));
        SERVICE_MAP.put("sheet", SpringContextProvider.getBean(DatabaseStorageServiceImpl.class));
    }

    private StorageFactory() {
        // do nothing
    }

    public static AbstractStorageService getStorage(String extension) {
        AbstractStorageService bean = SERVICE_MAP.get(extension);
        if (null == bean) {
            return SpringContextProvider.getBean(FileStorageServiceImpl.class);
        }

        return bean;
    }
}
