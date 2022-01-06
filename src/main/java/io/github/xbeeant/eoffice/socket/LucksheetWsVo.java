package io.github.xbeeant.eoffice.socket;

/**
 * lucksheet websocket 传输格式
 * <p>
 * <p>
 * {
 * createTime: 命令发送时间
 * data:{} 修改的命令
 * id: "7a"   websocket的id
 * returnMessage: "success"
 * status: "0"  0告诉前端需要根据data的命令修改  1无意义
 * type: 0：连接成功，1：发送给当前连接的用户，2：发送信息给其他用户，3：发送选区位置信息，999：用户连接断开
 * username: 用户名
 * }
 *
 * @author xiaobiao
 * @date 2022/1/5
 */
public class LucksheetWsVo {
    /**
     * create time
     */
    private String createTime;
    /**
     * 数据
     */
    private String data;
    /**
     * id
     */
    private String id;
    /**
     * return message
     */
    private String returnMessage;
    /**
     * 状态
     */
    private int status;
    /**
     * 类型， 0：连接成功，1.发送给发送信息用户，2.发送信息给其他用户，3.发送选区位置信息 999、用户连接断开
     */
    private int type;
    /**
     * 用户名
     */
    private String username;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
