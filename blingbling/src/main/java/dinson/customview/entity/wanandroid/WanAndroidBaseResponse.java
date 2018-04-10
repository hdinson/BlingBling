package dinson.customview.entity.wanandroid;

/**
 * 玩安卓基础统一返回Response
 */
public class WanAndroidBaseResponse {
    /**
     * data : {"collectIds":[],"email":"","icon":"","id":3924,"password":"Aa123456","type":0,"username":"dinson"}
     * errorCode : 0
     * errorMsg :
     */
    private int errorCode;
    private String errorMsg;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
