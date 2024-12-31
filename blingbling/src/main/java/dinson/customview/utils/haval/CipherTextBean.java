package dinson.customview.utils.haval;

public class CipherTextBean {

    private String aesSecretKey;
    private String aesVector;
    private String content;

    public String getAesSecretKey() {
        return aesSecretKey;
    }

    public void setAesSecretKey(String aesSecretKey) {
        this.aesSecretKey = aesSecretKey;
    }

    public String getAesVector() {
        return aesVector;
    }

    public void setAesVector(String aesVector) {
        this.aesVector = aesVector;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
