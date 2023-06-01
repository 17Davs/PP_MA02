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
import ma02_resources.participants.Facilitator;
import ma02_resources.participants.Instituition;



/**
 *
 * @author David Santos
 */
public class FacilitatorImp extends ParticipantImp implements Facilitator {

    private String areaOfExpertise;

    public FacilitatorImp(String areaOfExpertise, String name, String email, Contact contact, Instituition instituition) {
        super(name, email, contact, instituition);
        this.areaOfExpertise = areaOfExpertise;
    }

    @Override
    public String getAreaOfExpertise() {
        return this.areaOfExpertise;
    }

    @Override
    public void setAreaOfExpertise(String string) {
        this.areaOfExpertise = string;
    }

    
}
