package io.github.xbeeant.eoffice.service.storage;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.eoffice.model.Config;
import io.github.xbeeant.eoffice.service.IConfigService;
import io.github.xbeeant.spring.web.SpringContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobiao
 * @date 2022/1/6
 */
public class StorageFactory {

    private static final Map<String, AbstractStorageService> SERVICE_MAP = new HashMap<>();

    public static final Logger logger = LoggerFactory.getLogger(StorageFactory.class);

    private static AbstractStorageService getService(String extension) {
        if (SERVICE_MAP.isEmpty()) {
            IConfigService configService = SpringContextProvider.getBean(IConfigService.class);
            Config example = new Config();
            example.setModule("storage");

            ApiResponse<List<Config>> serviceRst = configService.selectAllByExample(example);
            if (serviceRst.getSuccess()) {
                List<Config> data = serviceRst.getData();
                for (Config config : data) {
                    try {
                        SERVICE_MAP.put(config.getCkey(), SpringContextProvider.getBean(config.getCvalue()));
                    } catch (Exception e) {
                        logger.error("写入存储服务map异常", e);
                    }
                }
            }
        }
        return SERVICE_MAP.get(extension);
    }

    private StorageFactory() {
        // do nothing
    }

    public static AbstractStorageService getStorage(String extension) {
        AbstractStorageService bean = getService(extension);
        if (null == bean) {
            return SpringContextProvider.getBean(FileStorageServiceImpl.class);
        }

        return bean;
    }
}
