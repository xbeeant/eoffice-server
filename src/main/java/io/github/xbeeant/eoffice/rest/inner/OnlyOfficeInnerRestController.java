package io.github.xbeeant.eoffice.rest.inner;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.JsonHelper;
import io.github.xbeeant.eoffice.enums.ActionAuditEnum;
import io.github.xbeeant.eoffice.model.Resource;
import io.github.xbeeant.eoffice.model.Storage;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.service.IResourceService;
import io.github.xbeeant.eoffice.service.render.office.DocumentStatus;
import io.github.xbeeant.eoffice.service.render.office.OnlyOfficeCallback;
import io.github.xbeeant.eoffice.service.render.office.OnlyOfficeCallbackResponse;
import io.github.xbeeant.eoffice.service.storage.StorageFactory;
import io.github.xbeeant.eoffice.util.FileHelper;
import io.github.xbeeant.eoffice.util.LogHelper;
import io.github.xbeeant.eoffice.util.SecurityHelper;
import io.github.xbeeant.eoffice.util.UrlHelper;
import io.github.xbeeant.spring.security.SecurityUser;
import io.github.xbeeant.spring.web.utils.file.FileMultipartFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URL;

/**
 * onlyoffice document server配套使用的api服务
 *
 * @author xiaobiao
 * @version 2021/7/1
 */
@Api(tags = "onlyoffice服务模块")
@RestController
@RequestMapping("api/office")
public class OnlyOfficeInnerRestController {
    private static final Logger logger = LoggerFactory.getLogger(OnlyOfficeInnerRestController.class);

    @Autowired
    private IResourceService resourceService;

    /**
     * onlyoffice 回调处理
     *
     * @param callback 回调JSON内容数据
     * @param token    onlyoffice的鉴权token
     * @return {@link OnlyOfficeCallbackResponse}
     * @see OnlyOfficeCallbackResponse
     */
    @RequestMapping(value = "callback", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    @ApiOperation(value = "事件回调接口")
    public OnlyOfficeCallbackResponse callback(@RequestBody OnlyOfficeCallback callback, String token, HttpServletRequest request) {
        if (logger.isInfoEnabled()) {
            logger.info("token {} callback  {}", token, JsonHelper.toJsonString(callback));
        }
        SecurityUser<User> userSecurityUser = SecurityHelper.tokenToUser(request);

        try {
            if (null == callback) {
                return OnlyOfficeCallbackResponse.success();
            }
            // 需要保存时写出文件
            if (callback.getStatus() == DocumentStatus.READY_FOR_SAVING.getCode() || callback.getStatus() == DocumentStatus.BEING_EDITED_STATE_SAVED.getCode()) {
                String url = callback.getUrl();
                String documentKey = callback.getKey();

                UrlHelper.UrlEntity parse = UrlHelper.parse(url);
                String filename = parse.params.get("filename");
                String[] keys = documentKey.split("_");
                Long lRid = Long.valueOf(keys[0]);
                String type = FileHelper.extension(filename);
                // 项目打包成jar包所在的根路径
                String storageName = documentKey + "." + type;
                String fileStoragePath = FileHelper.getStoragePath() + storageName;
                File storageFile = new File(fileStoragePath);
                FileUtils.copyURLToFile(new URL(url), storageFile);
                String extension = FileHelper.extension(filename);
                try {
                    File input = new File(storageFile.getPath());
                    FileMultipartFile file = new FileMultipartFile("file", input, storageFile.getName(), "text/plain");
                    ApiResponse<Resource> resourceApiResponse = resourceService.selectByPrimaryKey(lRid);
                    Resource resource = resourceApiResponse.getData();
                    String resourceName = resource.getName();
                    String newResourceName = resourceName.substring(0, resourceName.lastIndexOf(".") + 1) + extension;

                    Storage storage = StorageFactory.getStorage(extension).save(file, newResourceName, userSecurityUser.getUserId());
                    resourceService.overwriteResource(lRid, userSecurityUser.getUserId(), storage);
                    LogHelper.save(lRid, ActionAuditEnum.UPDATE, userSecurityUser);
                } catch (Exception e) {
                    logger.error("资源保存失败", e);
                    return OnlyOfficeCallbackResponse.failure();
                }
            }

            return OnlyOfficeCallbackResponse.success();
        } catch (Exception e) {
            logger.error("", e);
            return OnlyOfficeCallbackResponse.failure();
        }
    }
}
