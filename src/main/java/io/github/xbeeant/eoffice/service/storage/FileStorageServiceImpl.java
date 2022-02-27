package io.github.xbeeant.eoffice.service.storage;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.exception.FileSaveFailedException;
import io.github.xbeeant.eoffice.model.DocTemplate;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.Storage;
import io.github.xbeeant.eoffice.service.IDocTemplateService;
import io.github.xbeeant.eoffice.service.IStorageService;
import io.github.xbeeant.eoffice.util.FileHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaobiao
 * @date 2022/1/6
 */
@Component
public class FileStorageServiceImpl implements AbstractStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    @Autowired
    private IStorageService storageService;

    @Autowired
    private IDocTemplateService docTemplateService;

    @Override
    public Storage save(Object file, String filename, String uid) {
        MultipartFile multipartFile;
        if (file instanceof String) {
            multipartFile = FileHelper.toMultipart((String ) file, filename);
        } else  {
            multipartFile = (MultipartFile) file;
        }

        if (StringUtils.isEmpty(filename)) {
            filename = "";
        }
        long fileSize = multipartFile.getSize();
        String extension = FileHelper.extension(filename);
        String md5;
        try {
            md5 = FileHelper.md5(multipartFile);
        } catch (NoSuchAlgorithmException e) {
            throw new FileSaveFailedException();
        }
        // MD5比较，如果文件已经存在，直接返回已经存在的文件信息，避免重复存储
        Storage example = new Storage();
        example.setMd5(md5);
        example.setExtension(extension);
        ApiResponse<Storage> existStorage = storageService.selectOneByExample(example);
        Storage storage;
        if (!existStorage.getSuccess()) {
            String storageName = String.valueOf(IdWorker.getId());
            String fileStoragePath = FileHelper.getStoragePath() + storageName;
            if (logger.isInfoEnabled()) {
                logger.info("开始保存文档 {} 存储路径 {}", uid, fileStoragePath);
            }
            File storageFile = new File(fileStoragePath);
            // 保存
            try {
                multipartFile.transferTo(storageFile);
            } catch (IOException e) {
                throw new FileSaveFailedException();
            }
            // 写入数据库
            storage = new Storage();
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
        FileHelper.download(storage, resource, response, request);
    }

    @Override
    public Storage add(String type, Long fid, Long cid, String uid) {
        MultipartFile value = null;
        if (null != cid) {
            ApiResponse<DocTemplate> docTemplateApiResponse = docTemplateService.selectByPrimaryKey(cid);
            Long sid = docTemplateApiResponse.getData().getSid();
            ApiResponse<Storage> storageApiResponse = storageService.selectByPrimaryKey(sid);

            value = FileHelper.readAsMultipart(storageApiResponse.getData());
        }

        return save(value, "新建文档." + type, uid);
    }
}
