package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.antdesign.MenuItem;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.enums.PermConstant;
import io.github.xbeeant.eoffice.exception.FolderMissingException;
import io.github.xbeeant.eoffice.mapper.FolderMapper;
import io.github.xbeeant.eoffice.model.Folder;
import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.po.FullPerm;
import io.github.xbeeant.eoffice.service.IFolderService;
import io.github.xbeeant.eoffice.service.IPermService;
import io.github.xbeeant.eoffice.service.IResourceService;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * eoffice_folder
 */
@Service
public class FolderServiceImpl extends AbstractSecurityMybatisPageHelperServiceImpl<Folder, Long> implements IFolderService {
    /**
     * folder mapper
     */
    @Autowired
    private FolderMapper folderMapper;

    /**
     * perm service
     */
    @Autowired
    private IPermService permService;

    /**
     * resource service
     */
    @Autowired
    private IResourceService resourceService;

    @Override
    public IMybatisPageHelperDao<Folder, Long> getRepositoryDao() {
        return this.folderMapper;
    }

    @Override
    public void setDefaults(Folder record) {
        if (record.getFid() == null) {
            record.setFid(IdWorker.getId());
        }
    }

    @Override
    public ApiResponse<Folder> insertSelective(Folder record) {
        Long uid = Long.valueOf(record.getCreateBy());

        record.setFid(IdWorker.getId());
        record.setPath("/" + record.getName());
        if (null != record.getPfid() && 0 != record.getPfid()) {
            Folder folder = folderMapper.selectByPrimaryKey(record.getPfid());
            if (null == folder) {
                throw new FolderMissingException();
            }
            record.setPath(folder.getPath() + record.getPath());
        }

        Resource resource = new Resource();
        resource.setExtension("folder");
        resource.setRid(record.getFid());
        resource.setFid(record.getPfid());
        resource.setName(record.getName());
        resource.setPath(record.getPath());
        resource.setCreateBy(record.getCreateBy());
        resource.setUpdateBy(record.getCreateBy());
        resourceService.insertSelective(resource);

        // 设置权益信息
        Perm perm = new FullPerm();
        perm.setTargetId(record.getFid());
        perm.setType(PermConstant.FOLDER);
        perm.setUid(uid);
        perm.setCreateBy(record.getCreateBy());
        perm.setUpdateBy(record.getCreateBy());
        permService.insertSelective(perm);

        return super.insertSelective(record);
    }

    @Override
    public List<MenuItem> hasPermissionFolders(String userId) {

        List<Folder> folders = folderMapper.hasPermissionFolders(userId);
        // 没有数据 返回空list
        if (CollectionUtils.isEmpty(folders)) {
            return Collections.emptyList();
        }

        List<Folder> permedFolders = new ArrayList<>(folders);

        // 遍历有权限的目录，获取其子目录
        List<Folder> subFolders;
        for (Folder folder : folders) {
            subFolders = subFolders(folder.getFid());
            if (CollectionUtils.isNotEmpty(subFolders)) {
                permedFolders.addAll(subFolders);
            }
        }

        // 移除重复的
        Set<Folder> cleanedFolders = new TreeSet<>(Comparator.comparing(Folder::getFid));
        cleanedFolders.addAll(permedFolders);

        // 对象转换
        List<MenuItem> treeNodes = new ArrayList<>();
        MenuItem treeNode;
        for (Folder folder : cleanedFolders) {
            treeNode = new MenuItem();
            treeNode.setKey(String.valueOf(folder.getFid()));
            treeNode.setpKey(String.valueOf(folder.getPfid()));
            treeNode.setName(folder.getName());
            treeNode.setIcon(folder.getIcon());
            treeNode.setPath("/res/" + folder.getFid());
            treeNodes.add(treeNode);
        }

        // 树形结构处理
        for (MenuItem node : treeNodes) {
            for (MenuItem subNode : treeNodes) {
                if (subNode.getpKey().equals(node.getKey())) {
                    subNode.setParentExist(true);
                    node.addChildren(subNode);
                }
            }
        }
        for (MenuItem node : treeNodes) {
            if (!node.isParentExist()) {
                node.setpKey("0");
            }
        }
        treeNodes.removeIf(node -> !"0".equals(node.getpKey()));
        return treeNodes;
    }

    private List<Folder> subFolders(Long fid) {
        return folderMapper.subFolders(fid);
    }
}