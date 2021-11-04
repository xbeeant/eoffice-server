package io.github.xbeeant.eoffice.service;

import io.github.xbeeant.eoffice.model.Storage;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author mybatis code generator
 * @version Thu Nov 04 17:29:30 CST 2021
 */
public interface IStorageService extends IMybatisPageHelperService<Storage, Long> {
    /**
     * 插入
     *
     * @param file 文件
     * @param uid uid
     * @return {@link Storage}
     * @see Storage
     */
    Storage insert(MultipartFile file, String uid) throws NoSuchAlgorithmException, IOException;
}