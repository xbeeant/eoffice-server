package io.github.xbeeant.eoffice.service.render;

import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.rest.vo.ResourceVo;
import io.github.xbeeant.eoffice.service.render.office.OfficeConfig;
import io.github.xbeeant.eoffice.service.render.office.OnlyOfficeConfiguration;
import io.github.xbeeant.eoffice.util.Base64Helper;
import io.github.xbeeant.eoffice.util.FileHelper;
import io.github.xbeeant.spring.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiaobiao
 * @version 2022/2/12
 */
@Component
public class OfficeRenderServiceImpl implements AbstractRenderService {

    @Autowired
    private OnlyOfficeConfiguration onlyOfficeConfiguration;

    @Override
    public void setExtra(ResourceVo resource, String mode, SecurityUser<User> user) {
        String documentUrl;

        Long rid = resource.getRid();

        String type = resource.getExtension();
        String name = resource.getName();
        String md5 = resource.getStorage().getMd5();

        documentUrl = FileHelper.documentUrl(rid, resource.getSid(), user);
        OfficeConfig officeConfig = new OfficeConfig();
        officeConfig.setDocumentType(type);

        OfficeConfig.Document officeDocument = new OfficeConfig.Document();
        officeConfig.setDocument(officeDocument);

        officeDocument.setUrl(documentUrl);
        officeDocument.setFileType(type);
        // key的构成由资源ID（rid) + MD5值，保证每次编辑的文件都不是同一份，而多人协作时编辑的文件又是同一份

        officeDocument.setKey(rid + "_" + md5);
        officeDocument.setTitle(name);

        OfficeConfig.Document.Info info = new OfficeConfig.Document.Info();
        info.setFolder("");
        info.setOwner(resource.getOwner().getNickname());

        info.setUploaded(resource.getCreateBy());
        officeDocument.setInfo(info);

        // 对于历史文档，暂时支持这几个操作
        OfficeConfig.Document.Permissions permissions = new OfficeConfig.Document.Permissions();
        permissions.setComment(resource.getPerm().getComment());
        permissions.setCopy(resource.getPerm().getEdit());
        permissions.setEdit(resource.getPerm().getEdit());
        permissions.setDownload(resource.getPerm().getDownload());
        permissions.setPrint(resource.getPerm().getPrint());
        officeConfig.getDocument().setPermissions(permissions);

        // 编辑器配置
        OfficeConfig.EditorConfig editorConfig = new OfficeConfig.EditorConfig();
        editorConfig.setCallbackUrl(onlyOfficeConfiguration.getCallbackUrlPrefix() + "?token=" + Base64Helper.base64Encode(user.getUserId()));
        editorConfig.setUser(new OfficeConfig.EditorConfig.User(user));

        officeConfig.setEditorConfig(editorConfig);

        officeConfig.getEditorConfig().setMode(mode);

        resource.setExtra(officeConfig);
    }
}
