package dinson.customview.utils.steganography.model;

public class MessageDecodingStatus {
    private String message;
    private boolean ended;

    public MessageDecodingStatus() {
        message = "";
        ended = false;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}