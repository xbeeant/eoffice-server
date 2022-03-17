package io.github.xbeeant.eoffice.aspect;

import io.github.xbeeant.core.ApiResponse;
import io.github.xbeeant.core.BaseModelObject;
import io.github.xbeeant.eoffice.aspect.annotation.ResourceOwner;
import io.github.xbeeant.eoffice.exception.InvalidActionException;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.util.SecurityHelper;
import io.github.xbeeant.spring.mybatis.pagehelper.IMybatisPageHelperService;
import io.github.xbeeant.spring.security.SecurityUser;
import io.github.xbeeant.spring.web.SpringContextProvider;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2022/1/18
 */
@Aspect
@Component
public class ResourceOwnerVerify {
    private static final Logger logger = LoggerFactory.getLogger(ResourceOwnerVerify.class);
    /**
     * 转义字符切面
     */
    @Pointcut("@annotation(io.github.xbeeant.eoffice.aspect.annotation.ResourceOwner)")
    public void resourceOwnerVerify() {
        // 定义切面annotation注解方法
    }

    /**
     * 环绕通知
     *
     * @param joinPoint {@link JoinPoint}
     */
    @Before("resourceOwnerVerify()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 日志服务获取
        ResourceOwner annotation = method.getAnnotation(ResourceOwner.class);
        if (null == annotation) {
            return;
        }
        String idKey = annotation.id();

        // 获取输入参数
        Object[] arg = joinPoint.getArgs();
        // 参数名
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();

        List<Long> idValues = new ArrayList<>();
        for (int i = 0; i < argNames.length; i++) {
            if (idKey.equals(argNames[i])) {
                if (arg[i] instanceof Long) {
                    idValues.add((Long) arg[i]);
                }
                if (arg[i] instanceof List) {
                    idValues = (List<Long>) arg[i];
                }
                break;
            }
        }

        SecurityUser<User> userSecurityUser = SecurityHelper.currentUser();
        IMybatisPageHelperService service = (IMybatisPageHelperService) SpringContextProvider.getBean((annotation).selectService());
        for (Long value : idValues) {
            ApiResponse<BaseModelObject> apiResponse = service.selectByPrimaryKey(value);
            if (apiResponse.getSuccess()) {
                BaseModelObject data = apiResponse.getData();
                if (!userSecurityUser.getUserId().equals(data.getCreateBy())) {
                    logger.warn("资源 {} 分享失败 当前用户{} 资源作者 {}", value, userSecurityUser.getUserId(), data.getCreateBy());
                    throw new InvalidActionException("非所有者无法进行此操作");
                }
            }
        }


    }
}
