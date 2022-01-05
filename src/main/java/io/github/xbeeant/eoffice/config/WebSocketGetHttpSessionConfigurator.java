package io.github.xbeeant.eoffice.config;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 * @author xiaobiao
 * @date 2022/1/5
 */
public class WebSocketGetHttpSessionConfigurator extends Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        if (request.getHttpSession() != null) {
            HttpSession httpSession = (HttpSession) request.getHttpSession();
            sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
        }
    }
}
