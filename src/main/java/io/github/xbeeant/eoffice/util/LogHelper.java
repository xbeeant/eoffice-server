package io.github.xbeeant.eoffice.util;

import com.alibaba.fastjson.JSON;
import io.github.xbeeant.core.JsonHelper;
import io.github.xbeeant.eoffice.model.*;
import io.github.xbeeant.eoffice.rest.vo.ResourceVo;
import io.github.xbeeant.eoffice.service.IUserLogService;
import io.github.xbeeant.http.Requests;
import io.github.xbeeant.spring.security.SecurityUser;
import io.github.xbeeant.spring.web.SpringContextProvider;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xiaobiao
 * @version 2022/3/3
 */
public class LogHelper {

    private static IUserLogService userLogService;

    public static IUserLogService getUserLogService() {
        if (null == userLogService) {
            userLogService = SpringContextProvider.getBean(IUserLogService.class);
        }

        return userLogService;
    }

    public static void save(Folder data, String action, SecurityUser<User> user) {
        UserLog userLog = initialUserLog(action, user);

        userLog.setRid(data.getFid());
        userLog.setObjectType("eoffice_folder");
        userLog.setObjectKey("fid");
        userLog.setLog(JsonHelper.toJsonString(data));

        getUserLogService().insertSelective(userLog);
    }

    public static void save(ResourceVo data, String action, SecurityUser<User> user) {
        UserLog userLog = initialUserLog(action, user);

        userLog.setRid(data.getRid());
        userLog.setTarget(String.valueOf(data.getSid()));
        userLog.setObjectType("eoffice_resource");
        userLog.setObjectKey("rid");
        userLog.setLog(JsonHelper.toJsonString(data));

        getUserLogService().insertSelective(userLog);
    }

    public static void save(Resource data, String action, SecurityUser<User> user) {
        UserLog userLog = initialUserLog(action, user);

        userLog.setRid(data.getRid());
        userLog.setTarget(String.valueOf(data.getSid()));
        if ("folder".equals(data.getExtension())) {
            userLog.setObjectType("eoffice_folder");
            userLog.setObjectKey("fid");
        } else {
            userLog.setObjectType("eoffice_resource");
            userLog.setObjectKey("rid");
        }

        userLog.setLog(JsonHelper.toJsonString(data));

        getUserLogService().insertSelective(userLog);
    }

    private static UserLog initialUserLog(String action, SecurityUser<User> user) {
        UserLog userLog = new UserLog();
        userLog.setUsername(user.getUserId());
        userLog.setNickname(user.getUserNickname());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        userLog.setIp(Requests.getIp(request));
        userLog.setUserAgent(Requests.getUserAgent(request));
        userLog.setBehavior(action);
        return userLog;
    }

    public static void save(Long rid, String action, SecurityUser<User> user) {
        UserLog userLog = initialUserLog(action, user);

        userLog.setRid(rid);
        userLog.setObjectType("eoffice_resource");
        userLog.setObjectKey("rid");

        getUserLogService().insertSelective(userLog);
    }

    public static void save(Perm data, String action, SecurityUser<User> user) {
        UserLog userLog = initialUserLog(action, user);

        userLog.setRid(data.getRid());
        userLog.setTarget(String.valueOf(data.getTargetId()));
        userLog.setObjectType("eoffice_resource");
        userLog.setObjectKey("rid");

        getUserLogService().insertSelective(userLog);
    }

    public static void save(Long rid, List<Long> targetIds, String action, SecurityUser<User> user) {
        for (Long targetId : targetIds) {
            UserLog userLog = initialUserLog(action, user);

            userLog.setRid(rid);
            userLog.setTarget(String.valueOf(targetId));
            userLog.setObjectType("eoffice_resource");
            userLog.setObjectKey("rid");

            getUserLogService().insertSelective(userLog);
        }
    }

    public static void save(Share data, List<Long> targetIds, List<String> perm, String action, SecurityUser<User> user) {
        for (Long targetId : targetIds) {
            UserLog userLog = initialUserLog(action, user);

            userLog.setRid(targetId);
            userLog.setTarget(String.valueOf(targetId));
            userLog.setExtra(JSON.toJSONString(perm));
            userLog.setObjectType("eoffice_resource");
            userLog.setObjectKey("rid");

            getUserLogService().insertSelective(userLog);
        }
    }
}
