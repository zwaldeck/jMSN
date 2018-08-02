package be.zwaldeck.jmsn.server.domain;

import be.zwaldeck.jmsn.common.Status;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "user_tbl")
@Data
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Column(name = "email", length = 500, unique = true)
    private String email;

    @Column(name = "password", length = 500)
    private String password;

    @Column(name = "nickname", length = 500)
    private String nickname;

    @Column(name = "ip", length = 45)
    private String ip;

    @Column(name = "sub_nickname", length = 500)
    private String subNickname;

    @Enumerated(EnumType.STRING)
    private Status status;
}
