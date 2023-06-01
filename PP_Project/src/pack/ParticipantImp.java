/*
 * Nome: Carolina Bonito Queiroga De Almeida
 * Número: 8180091 
 * Turna: LSIRCT1
 *
 * Nome: David Leandro Spencer Conceição dos Santos
 * Número: 8220651
 * Turna: LSIRCT1
 */
package pack;

import ma02_resources.participants.Contact;
import ma02_resources.participants.Instituition;
import ma02_resources.participants.Participant;

/**
 *
 * @author David Santos
 */
public class ParticipantImp implements Participant {

    private String name, email;
    private Contact contact;
    private Instituition instituition;

    public ParticipantImp(String name, String email, Contact contact, Instituition instituition) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.instituition = instituition;
    }
    
    @Override
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public void setInstituition(Instituition instituition) {
        this.instituition = instituition;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public Contact getContact() {
        return this.contact;
    }

    @Override
    public Instituition getInstituition() {
        return this.instituition;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Participant)) {
            return false;
        }
        final Participant other = (Participant) obj;
        
        return this.email.equals(other.getEmail());
    }

    
}
