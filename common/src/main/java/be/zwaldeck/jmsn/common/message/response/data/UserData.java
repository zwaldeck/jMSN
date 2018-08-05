package be.zwaldeck.jmsn.common.message.response.data;

import be.zwaldeck.jmsn.common.Status;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserData implements Serializable {

    private static final long serialVersionUID = -4929778268561660404L;
    
    private String id;
    private String ip;
    private Status status;
    private String nickname;
    private String subNickname;
}
