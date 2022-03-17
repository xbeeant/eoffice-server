package io.github.xbeeant.eoffice.service.render.office;

import io.github.xbeeant.eoffice.util.UrlHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaobiao
 * @version 2021/7/1
 */
@Configuration
@EnableAutoConfiguration
public class OnlyOfficeConfiguration {
    @Resource
    private HttpServletRequest request;

    @Value("${onlyoffice.callback-url}")
    private String callbackUrlPrefix;

    @Value("${onlyoffice.changes-url}")
    private String changesPrefix;

    @Value("${onlyoffice.document-url}")
    private String documentUrlPrefix;

    public String getChangesPrefix() {
        return UrlHelper.getServerDomain(request) + changesPrefix;
    }

    public void setChangesPrefix(String changesPrefix) {
        this.changesPrefix = changesPrefix;
    }

    public String getDocumentUrlPrefix() {
        return UrlHelper.getServerDomain(request) + documentUrlPrefix;
    }

    public void setDocumentUrlPrefix(String documentUrlPrefix) {
        this.documentUrlPrefix = documentUrlPrefix;
    }

    public String getCallbackUrlPrefix() {
        return UrlHelper.getServerDomain(request) + callbackUrlPrefix;
    }

    public void setCallbackUrlPrefix(String callbackUrlPrefix) {
        this.callbackUrlPrefix = callbackUrlPrefix;
    }
}
