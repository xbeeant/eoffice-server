package io.github.xbeeant.eoffice.socket;

import com.alibaba.fastjson.JSON;
import io.github.xbeeant.eoffice.util.PakoGzipHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaobiao
 * @date 2022/1/5
 */
@ServerEndpoint(value = "/api/socket/lucksheet/{rid}")
@Component
public class LucksheetWebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(LucksheetWebSocketServer.class);

    /**
     * 静态变量，用来记录当前连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger();

    /**
     * concurrent线程安全set，用来存放每个客户端对应的MyWebSocketServer对象
     */
    private static CopyOnWriteArraySet<LucksheetWebSocketServer> webSockets = new CopyOnWriteArraySet<>();
    private static ConcurrentHashMap<String, LucksheetWebSocketServer> tokenMap = new ConcurrentHashMap<>();
    /**
     * 与每个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /***
     * 唯一标识
     */
    private String userId;

    /**
     * 连接成功调用的方法
     * org.springframework.boot.web.servlet.server.Session requestSession,
     *
     * @param session 可选的参数。与某个客户端的连接会话
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("rid") String rid) {
        //正常情况下，可以用登录的用户名或者token来作为userId
//        如下可以获取到httpSession，与当前的session(socket)不是一样的
//        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
//        userId = String.valueOf(httpSession.getAttribute("你的token key"));
        userId = rid;
        webSockets.add(this);
        if (tokenMap.get(userId) == null) {
            onlineCount.incrementAndGet();
        }
        tokenMap.put(userId, this);
        this.session = session;
        logger.info("{} 建立了连接！", userId);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSockets.remove(this);
        tokenMap.remove(userId);

        onlineCount.decrementAndGet();
        logger.info("有一连接关闭！当前连接总数为{}", onlineCount.get());
    }

    @OnMessage
    public void onMessage(String message) {
        // rub代表心跳包
        if ("rub".equals(message)) {
            return;
        }
        String uncompress = PakoGzipHelper.uncompress(message);

        for (String key : tokenMap.keySet()) {
//            if (!key.equals(userId)) {
            LucksheetWebSocketServer socketServer = tokenMap.get(key);
            LucksheetWsVo wsResultBean = new LucksheetWsVo();
            wsResultBean.setData(uncompress);
            wsResultBean.setStatus(0);
            wsResultBean.setUsername(userId);
            wsResultBean.setId(wsResultBean.getUsername());
            wsResultBean.setReturnMessage("success");
            wsResultBean.setCreateTime("");

//                DBObject bson = null;
//                try {
//                    bson = (DBObject) JsonHelper.toObject(wsResultBean.getData());
//                } catch (Exception ex) {
//                    return;
//                }
//                if (bson != null) {
//                    if (bson.get("t").equals("mv")) {
//                        更新选区显示
//                        wsResultBean.setType(3);
//                    } else {
//                        更新数据
//                        wsResultBean.setType(2);
//                    }
//                }
            socketServer.sendMessage(wsResultBean, socketServer.session);
        }
//        }
    }


    public void onLineMessage(String message) {

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
        LucksheetWebSocketServer that = (LucksheetWebSocketServer) o;
        return Objects.equals(session, that.session);
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(LucksheetWsVo wsResultBean, Session toSession) {
        try {
            logger.info("服务端给客户端[{}]发送消息", toSession.getId());
            toSession.getBasicRemote().sendText(JSON.toJSONString(wsResultBean));
        } catch (Exception e) {
            logger.error("服务端发送消息给客户端失败", e);
        }
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        for (LucksheetWebSocketServer webSocket : webSockets) {
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                logger.error("lucksheet websocket 异常", e);
            }
        }
    }

    /**
     * 全部踢下线
     */
    public void clear() {
        for (Map.Entry<String, LucksheetWebSocketServer> item : tokenMap.entrySet()) {
            LucksheetWebSocketServer socketServer = item.getValue();
            try {
                socketServer.session.close();
            } catch (IOException e) {
                logger.error("lucksheet websocket 关闭异常", e);
            }
        }
        logger.info("全部断开！剩余{}个", onlineCount.get());

    }


    @Override
    public int hashCode() {
        return Objects.hash(session);
    }
}
