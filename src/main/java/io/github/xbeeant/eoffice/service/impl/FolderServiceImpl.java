package io.github.xbeeant.eoffice.service.impl;

import io.github.xbeeant.antdesign.MenuItem;
import io.github.xbeeant.antdesign.TreeNode;
import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.IdWorker;
import io.github.xbeeant.eoffice.config.AbstractSecurityMybatisPageHelperServiceImpl;
import io.github.xbeeant.eoffice.mapper.FolderMapper;
import io.github.xbeeant.eoffice.model.Folder;
import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.UserGroup;
import io.github.xbeeant.eoffice.po.FullPerm;
import io.github.xbeeant.eoffice.po.PermType;
import io.github.xbeeant.eoffice.service.*;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

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

    @Autowired
    private IGroupService groupService;

    @Autowired
    private IUserGroupService userGroupService;

    @Override
    public IMybatisPageHelperDao<Folder, Long> getRepositoryDao() {
        return this.folderMapper;
    }

    @Override
    public void setDefaults(Folder folder) {
        if (folder.getFid() == null) {
            folder.setFid(IdWorker.getId());
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
                throw new ResourceAccessException("????????????????????????????????????????????????");
            }
            record.setPath(folder.getPath() + record.getPath());
        }

        Resource resource = new Resource();
        resource.setExtension("folder");
        resource.setRid(record.getFid());
        resource.setFid(record.getPfid());
        resource.setName(record.getName());
        resource.setPath(record.getPath());
        resource.setSid(0L);
        resource.setCreateBy(record.getCreateBy());
        resource.setUpdateBy(record.getCreateBy());
        resourceService.insertSelective(resource);

        // ??????????????????
        Perm perm = new FullPerm();
        perm.setRid(record.getFid());
        perm.setType(PermType.FOLDER.getType());
        perm.setTargetId(uid);
        perm.setCreateBy(record.getCreateBy());
        perm.setUpdateBy(record.getCreateBy());
        permService.insertSelective(perm);

        return super.insertSelective(record);
    }

    @Override
    public MenuItem menuItem(Long fid) {
        List<Folder> parents = folderMapper.parents(fid);
        // ???????????????
        Set<Folder> cleanedFolders = new TreeSet<>(Comparator.comparing(Folder::getFid));
        cleanedFolders.addAll(parents);

        return toTreeMenu(cleanedFolders).get(0);
    }

    @Override
    public List<Folder> breadcrumb(Long fid) {
        return folderMapper.parents(fid);
    }

    @Override
    public void updateFolderSize(Long fid, Long size) {
        List<Folder> parents = folderMapper.parents(fid);
        List<Long> parentIds = new ArrayList<>(parents.size());
        for (Folder folder : parents) {
            parentIds.add(folder.getFid());
        }
        if (!CollectionUtils.isEmpty(parentIds)) {
            if (size > 0) {
                folderMapper.increaseSize(parentIds, size);
            } else {
                folderMapper.decreaseSize(parentIds, Math.abs(size));
            }
        }
    }

    @Override
    public void updateFolderAndResourceSize(Long fid, Long resourceOldSize, Long resourceNewSize) {
        List<Folder> parents = folderMapper.parents(fid);
        List<Long> parentIds = new ArrayList<>(parents.size());
        for (Folder folder : parents) {
            parentIds.add(folder.getFid());
        }
        if (!CollectionUtils.isEmpty(parentIds)) {
            folderMapper.updateResourceSize(parentIds, resourceOldSize, resourceNewSize);
        }
    }

    @Override
    public List<MenuItem> hasPermissionMenus(String userId) {
        // ??????ID??????????????????ID??????
        List<Folder> folders = permedFolders(userId);
        // ???????????? ?????????list
        if (CollectionUtils.isEmpty(folders)) {
            return Collections.emptyList();
        }

        List<Folder> permedFolders = new ArrayList<>(folders);

        // ?????????????????????????????????????????????
        List<Folder> subFolders;
        for (Folder folder : folders) {
            subFolders = subFolders(folder.getFid());
            if (CollectionUtils.isNotEmpty(subFolders)) {
                permedFolders.addAll(subFolders);
            }
        }

        // ???????????????
        Set<Folder> cleanedFolders = new TreeSet<>(Comparator.comparing(Folder::getFid));
        cleanedFolders.addAll(permedFolders);

        return toTreeMenu(cleanedFolders);
    }

    @Override
    public List<TreeNode> hasPermissionFolders(String userId) {
        List<Folder> folders = permedFolders(userId);
        // ???????????? ?????????list
        if (CollectionUtils.isEmpty(folders)) {
            return Collections.emptyList();
        }

        List<Folder> permedFolders = new ArrayList<>(folders);

        // ?????????????????????????????????????????????
        List<Folder> subFolders;
        for (Folder folder : folders) {
            subFolders = subFolders(folder.getFid());
            if (CollectionUtils.isNotEmpty(subFolders)) {
                permedFolders.addAll(subFolders);
            }
        }

        // ???????????????
        Set<Folder> cleanedFolders = new TreeSet<>(Comparator.comparing(Folder::getFid));
        cleanedFolders.addAll(permedFolders);

        return toTreeNode(cleanedFolders);
    }

    private List<Folder> permedFolders(String userId) {
        UserGroup userGroupExample = new UserGroup();
        userGroupExample.setUid(Long.valueOf(userId));
        ApiResponse<List<UserGroup>> listApiResponse = userGroupService.selectAllByExample(userGroupExample);
        Set<Long> gids = new HashSet<>();
        if (listApiResponse.getSuccess()) {
            for (UserGroup item : listApiResponse.getData()) {
                List<Long> ids = groupService.parentIds(item.getGid());
                gids.addAll(ids);
            }
        }
        return folderMapper.hasPermissionFolders(userId, gids);
    }

    private List<TreeNode> toTreeNode(Set<Folder> cleanedFolders) {
        // ????????????
        List<TreeNode> treeNodes = new ArrayList<>();
        TreeNode treeNode;
        for (Folder folder : cleanedFolders) {
            treeNode = new TreeNode();
            treeNode.setKey(String.valueOf(folder.getFid()));
            treeNode.setpKey(String.valueOf(folder.getPfid()));
            treeNode.setTitle(folder.getName());
            treeNode.setValue(String.valueOf(folder.getFid()));
            treeNodes.add(treeNode);
        }

        // ??????????????????
        for (TreeNode node : treeNodes) {
            for (TreeNode subNode : treeNodes) {
                if (subNode.getpKey().equals(node.getKey())) {
                    subNode.setIsLeaf(false);
                    node.addChildren(subNode);
                }
            }
        }

        treeNodes.removeIf(node -> !("0".equals(node.getpKey())));
        return treeNodes;
    }

    private List<MenuItem> toTreeMenu(Set<Folder> cleanedFolders) {
        // ????????????
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

        // ??????????????????
        for (MenuItem node : treeNodes) {
            for (MenuItem subNode : treeNodes) {
                if (subNode.getpKey().equals(node.getKey())) {
                    subNode.setIsLeaf(false);
                    subNode.setParentExist(true);
                    node.addChildren(subNode);
                }
            }
        }

        for (MenuItem node : treeNodes) {
            if (!node.getParentExist()) {
                node.setpKey("0");
            }
        }

        treeNodes.removeIf(node -> !"0".equals(node.getpKey()));
        return treeNodes;
    }

    @Override
    public List<Folder> subFolders(Long fid) {
        return folderMapper.subFolders(fid);
    }

    @Override
    public List<Folder> parentFolders(Long fid) {
        return folderMapper.parents(fid);
    }
}