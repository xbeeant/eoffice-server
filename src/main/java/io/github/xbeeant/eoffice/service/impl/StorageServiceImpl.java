package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.StorageMapper;
import io.github.xbeeant.eoffice.model.Storage;
import io.github.xbeeant.eoffice.service.IStorageService;
import io.github.xbeeant.eoffice.service.storage.StorageFactory;
import io.github.xbeeant.eoffice.util.FileHelper;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * eoffice_storage
 */
@Service
public class StorageServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<Storage, Long> implements IStorageService {

    private static final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Autowired
    private StorageMapper storageMapper;

    @Override
    public IMybatisPageHelperDao<Storage, Long> getRepositoryDao() {
        return this.storageMapper;
    }

    @Override
    public void setDefaults(Storage storage) {
        if (storage.getSid() == null) {
            storage.setSid(IdWorker.getId());
        }
    }

    @Override
    public Storage insert(String value, String filename, String uid) throws NoSuchAlgorithmException, IOException {
        String extension = FileHelper.extension(filename);
        return StorageFactory.getStorage(extension).save(value, filename, uid);
    }

    @Override
    public Storage insert(MultipartFile file, String uid) throws NoSuchAlgorithmException, IOException {
        String filename = file.getOriginalFilename();
        if (StringUtils.isEmpty(filename)) {
            filename = "";
        }
        String extension = FileHelper.extension(filename);
        logger.debug("文件写入 类型 {}", extension);
        return StorageFactory.getStorage(extension).save(file, filename, uid);
    }
}
