package be.zwaldeck.msn.server.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author Wout Schoovaerts
 */
@Entity
@Table(name = "contacts")
public class Contact implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Id
    @ManyToOne
    @JoinColumn(name = "contact_id")
    private User contact;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getContact() {
        return contact;
    }

    public void setContact(User contact) {
        this.contact = contact;
    }
}
