package io.github.xbeeant.eoffice.service.render.office;

/**
 * @author xiaobiao
 * @version 2021/7/1
 */
public class OnlyOfficeCallbackResponse {
    private int error;

    public static OnlyOfficeCallbackResponse success(){
        return new OnlyOfficeCallbackResponse(0);
    }

    public static OnlyOfficeCallbackResponse failure(){
        return new OnlyOfficeCallbackResponse(1);
    }

    public OnlyOfficeCallbackResponse(int error) {
        this.error = error;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
