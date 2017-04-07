package be.zwaldeck.msn.server.domain;

import be.zwaldeck.msn.common.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author Wout Schoovaerts
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", length = 500)
    private String email;

    @Column(name = "password", length = 150)
    private String password;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "ip", length = 45)
    private String ip;

    @Column(name = "nickname", length = 1000)
    private String nickname;

    @Column(name = "subNickname", length = 1000)
    private String subNickname;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSubNickname() {
        return subNickname;
    }

    public void setSubNickname(String subNickname) {
        this.subNickname = subNickname;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}