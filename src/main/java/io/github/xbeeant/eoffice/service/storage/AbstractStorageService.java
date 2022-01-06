package io.github.xbeeant.eoffice.service.storage;

import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.Storage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiaobiao
 * @date 2022/1/6
 */
public interface AbstractStorageService {

    /**
     * 保存
     *
     * @param file     文件
     * @param filename 文件名
     * @param uid      uid
     * @return {@link Storage}
     * @see Storage
     */
    Storage save(Object file, String filename, String uid);

    /**
     * 下载
     *
     * @param storage  存储
     * @param resource 资源
     * @param request  请求
     * @param response 响应
     */
    void download(Storage storage, Resource resource, HttpServletResponse response, HttpServletRequest request);
}