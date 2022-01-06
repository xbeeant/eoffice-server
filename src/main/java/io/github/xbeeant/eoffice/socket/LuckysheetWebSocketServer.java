package io.github.xbeeant.eoffice.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.xbeeant.eoffice.config.WebSocketGetHttpSessionConfigurator;
import io.github.xbeeant.eoffice.model.User;
import io.github.xbeeant.eoffice.util.PakoGzipHelper;
import io.github.xbeeant.spring.security.SecurityUser;
import io.github.xbeeant.spring.web.SpringContextProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaobiao
 * @date 2022/1/5
 */
@ServerEndpoint(value = "/api/socket/sheet/{session}/{timestamp}", configurator = WebSocketGetHttpSessionConfigurator.class)
@Component
public class LuckysheetWebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(LuckysheetWebSocketServer.class);

    /**
     * 静态变量，用来记录当前连接数
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger();

    /**
     * concurrent线程安全set，用来存放每个客户端对应的MyWebSocketServer对象
     */
    private static final CopyOnWriteArraySet<LuckysheetWebSocketServer> WEB_SOCKETS = new CopyOnWriteArraySet<>();
    private static final ConcurrentHashMap<String, LuckysheetWebSocketServer> SESSION_MAP = new ConcurrentHashMap<>();
    /**
     * 与每个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * websocket id
     */
    private String id;

    /**
     * session id
     */
    private String sid;

    /**
     * 文件资源ID
     */
    private String rid;

    /**
     * 用户信息
     */
    private SecurityUser<User> principal;

    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return Map
     */
    public static Map<String, Object> getUrlParams(String param) {
        Map<String, Object> map = new HashMap<>(0);
        if (StringUtils.isBlank(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (String s : params) {
            String[] p = s.split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 连接成功调用的方法
     * org.springframework.boot.web.servlet.server.Session requestSession,
     *
     * @param session 可选的参数。与某个客户端的连接会话
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("session") String sessionId, @PathParam("timestamp") String timestamp) {
        WEB_SOCKETS.add(this);
        String queryString = session.getQueryString();
        Map<String, Object> urlParams = getUrlParams(queryString);
        id = sessionId + timestamp;
        sid = sessionId;
        rid = String.valueOf(urlParams.get("g"));
        if (SESSION_MAP.get(id) == null) {
            ONLINE_COUNT.incrementAndGet();
        }
        SESSION_MAP.put(id, this);
        this.session = session;

        FindByIndexNameSessionRepository<org.springframework.session.Session> sessionRepository = SpringContextProvider.getBean("sessionRepository");
        org.springframework.session.Session securitySession = sessionRepository.findById(sid);
        SecurityContextImpl securityContext = securitySession.getAttribute("SPRING_SECURITY_CONTEXT");
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) securityContext.getAuthentication();
        principal = (SecurityUser<User>) authentication.getPrincipal();

        logger.info("{} 加入,在线人数 = {}", principal.getUserNickname(), ONLINE_COUNT.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        WEB_SOCKETS.remove(this);
        SESSION_MAP.remove(id);

        ONLINE_COUNT.decrementAndGet();
        logger.info("{} 离开,在线人数 = {}", principal.getUserNickname(), ONLINE_COUNT.get());
    }

    @OnMessage
    public void onMessage(String message) {
        // rub代表心跳包
        if ("rub".equals(message)) {
            return;
        }
        String uncompress = PakoGzipHelper.uncompress(message);
        logger.info("{} 解压后的数据[{}]", rid, uncompress);
        for (Map.Entry<String, LuckysheetWebSocketServer> item : SESSION_MAP.entrySet()) {
            if (!item.getKey().equals(id)) {
                LuckysheetWebSocketServer socketServer = item.getValue();
                LucksheetWsVo wsResultBean = new LucksheetWsVo();
                wsResultBean.setData(uncompress);
                wsResultBean.setStatus(0);
                wsResultBean.setUsername(principal.getUserNickname());
                // id
                wsResultBean.setId(principal.getUserId());
                wsResultBean.setReturnMessage("success");
                wsResultBean.setCreateTime(String.valueOf((new Date()).getTime()));

                JSONObject bson;
                try {
                    bson = JSON.parseObject(wsResultBean.getData());
                } catch (Exception ex) {
                    return;
                }
                if (bson != null) {
                    if ("mv".equals(bson.get("t"))) {
                        // 更新选区显示
                        wsResultBean.setType(3);
                    } else {
                        // 更新数据
                        wsResultBean.setType(2);
                    }
                }
                socketServer.sendMessage(wsResultBean, socketServer.session);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("WebSocket接收消息错误{},sessionId为{}", error.getMessage(), session.getId());
        error.printStackTrace();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LuckysheetWebSocketServer that = (LuckysheetWebSocketServer) o;
        return Objects.equals(session, that.session);
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(LucksheetWsVo wsResultBean, Session toSession) {
        try {
            logger.info("服务端给客户端[{}]发送消息", toSession.getRequestURI().getPath());
            toSession.getBasicRemote().sendText(JSON.toJSONString(wsResultBean));
        } catch (Exception e) {
            logger.error("服务端发送消息给客户端失败", e);
        }
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        for (LuckysheetWebSocketServer webSocket : WEB_SOCKETS) {
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                logger.error("luckysheet websocket 异常", e);
            }
        }
    }

    /**
     * 全部踢下线
     */
    public void clear() {
        for (Map.Entry<String, LuckysheetWebSocketServer> item : SESSION_MAP.entrySet()) {
            LuckysheetWebSocketServer socketServer = item.getValue();
            try {
                socketServer.session.close();
            } catch (IOException e) {
                logger.error("luckysheet websocket 关闭异常", e);
            }
        }
        logger.info("全部断开！剩余{}个", ONLINE_COUNT.get());

    }


    @Override
    public int hashCode() {
        return Objects.hash(session);
    }
}
