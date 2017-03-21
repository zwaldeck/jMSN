package be.zwaldeck.msn.common.messages.data;

import be.zwaldeck.msn.common.Status;

import java.io.Serializable;

/**
 * @Author Wout Schoovaerts
 */
public class UserData implements Serializable {

    private int id;
    private String nickname;
    private Status status;
    private String ip;
    private String subNickname;

    public UserData(int id, String nickname, Status status, String ip, String subNickname) {
        this.id = id;
        this.nickname = nickname;
        this.status = status;
        this.ip = ip;
        this.subNickname = subNickname;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public Status getStatus() {
        return status;
    }

    public String getIp() {
        return ip;
    }

    public String getSubNickname() {
        return subNickname;
    }
}
