package io.github.xbeeant.eoffice.service.storage;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.exception.FileSaveFailedException;
import io.github.xbeeant.eoffice.model.ContentStorage;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.Storage;
import io.github.xbeeant.eoffice.service.IConfigService;
import io.github.xbeeant.eoffice.service.IContentStorageService;
import io.github.xbeeant.eoffice.service.IStorageService;
import io.github.xbeeant.eoffice.util.FileHelper;
import io.github.xbeeant.http.Requests;
import io.github.xbeeant.spring.web.SpringContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaobiao
 * @date 2022/1/6
 */
@Component("databaseStorageService")
public class DatabaseStorageServiceImpl implements AbstractStorageService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseStorageServiceImpl.class);

    @Autowired
    private IContentStorageService contentStorageService;

    @Autowired
    private IStorageService storageService;

    @Override
    public Storage save(Object value, String filename, String uid) {
        String content = (String) value;
        String md5;
        try {
            md5 = FileHelper.md5(content);
        } catch (NoSuchAlgorithmException e) {
            throw new FileSaveFailedException();
        }
        long fileSize = content.length();
        String extension = FileHelper.extension(filename);

        // MD5比较，如果文件已经存在，直接返回已经存在的文件信息，避免重复存储
        Storage example = new Storage();
        example.setMd5(md5);
        ApiResponse<Storage> existStorage = storageService.selectOneByExample(example);
        Storage storage;
        if (!existStorage.getSuccess()) {
            String storageName = String.valueOf(IdWorker.getId());
            String fileStoragePath = FileHelper.getStoragePath() + storageName;
            if (logger.isInfoEnabled()) {
                logger.info("开始保存文档 {} 存储路径 {}", uid, fileStoragePath);
            }
            Long sid = IdWorker.getId();
            // 保存
            ContentStorage contentStorage = new ContentStorage();
            contentStorage.setSid(sid);
            contentStorage.setContent(content);
            contentStorageService.insertSelective(contentStorage);

            // 写入数据库
            storage = new Storage();
            storage.setSid(sid);
            storage.setMd5(md5);
            storage.setSize(fileSize);
            storage.setExtension(extension);
            storage.setPath(storageName);
            storage.setName(filename);
            storage.setCreateBy(uid);
            storage.setUpdateBy(uid);
            storageService.insertSelective(storage);
        } else {
            storage = existStorage.getData();
        }
        return storage;
    }

    @Override
    public void download(Storage storage, Resource resource, HttpServletResponse response, HttpServletRequest request) {
        ApiResponse<ContentStorage> contentStorageResponse = contentStorageService.selectByPrimaryKey(storage.getSid());
        Requests.writeJson(response, contentStorageResponse.getData().getContent());
    }

    @Override
    public Storage add(String type, Long fid, String uid) {
        IConfigService configService = SpringContextProvider.getBean(IConfigService.class);
        String value = configService.valueOf("template", type);
        if (null == value) {
            value = "";
        }


        return save(value, "新建文档." + type, uid);
    }
}
