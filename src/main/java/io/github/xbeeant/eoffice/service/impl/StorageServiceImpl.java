package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.StorageMapper;
import io.github.xbeeant.eoffice.model.Storage;
import io.github.xbeeant.eoffice.service.IStorageService;
import io.github.xbeeant.eoffice.util.FileHelper;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * eoffice_storage
 */
@Service
public class StorageServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<Storage, Long> implements IStorageService {
    @Autowired
    private StorageMapper storageMapper;

    @Override
    public IMybatisPageHelperDao<Storage, Long> getRepositoryDao() {
        return this.storageMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Override
    public void setDefaults(Storage record) {
        if (record.getAid() == null) {
            record.setAid(IdWorker.getId());
        }
    }

    @Override
    public Storage insert(MultipartFile file, String uid) throws NoSuchAlgorithmException, IOException {
        String filename = file.getOriginalFilename();
        if (StringUtils.isEmpty(filename)) {
            filename = "";
        }

        long fileSize = file.getSize();
        String extension = FileHelper.extension(filename);
        String md5 = FileHelper.md5(file);
        // MD5比较，如果文件已经存在，直接返回已经存在的文件信息，避免重复存储
        Storage example = new Storage();
        example.setMd5(md5);
        ApiResponse<Storage> existStorage = selectOneByExample(example);
        Storage storage;
        if (!existStorage.getSuccess()) {
            String storageName = String.valueOf(IdWorker.getId());
            String fileStoragePath = FileHelper.getStoragePath() + storageName;
            if (logger.isInfoEnabled()) {
                logger.info("开始保存文档 {} 存储路径 {}", uid, fileStoragePath);
            }
            File storageFile = new File(fileStoragePath);
            // 保存
            file.transferTo(storageFile);
            // 写入数据库
            storage = new Storage();
            storage.setMd5(md5);
            storage.setSize(fileSize);
            storage.setExtension(extension);
            storage.setPath(storageName);
            storage.setName(filename);
            storage.setCreateBy(uid);
            storage.setUpdateBy(uid);
            insertSelective(storage);
        } else {
            storage = existStorage.getData();
        }

        return storage;
    }
}