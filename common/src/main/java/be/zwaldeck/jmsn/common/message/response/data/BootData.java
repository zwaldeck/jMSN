package be.zwaldeck.jmsn.common.message.response.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BootData implements Serializable {

    private static final long serialVersionUID = -5976471788306821280L;

    private List<UserData> contactList;
    private UserData user;
}
