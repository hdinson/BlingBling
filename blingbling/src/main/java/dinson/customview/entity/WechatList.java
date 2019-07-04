package dinson.customview.entity;

public class WechatList {
    private int id;
    private String nick;
    private String chat;
    private String time;

    public WechatList(int id, String nick, String chat, String time) {
        this.id = id;
        this.nick = nick;
        this.chat = chat;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
