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
import org.json.simple.JSONObject;

public class FacilitatorImp extends ParticipantImp implements Facilitator {

    /**
     * Area of expertise of a Facilitator.
     */
    private String areaOfExpertise;

    /**
     * This is the constructor method of Facilitator.
     *  
     * @param areaOfExpertise Area of expertise of a Facilitator.
     * @param name Facilitator's name.
     * @param email Facilitator's email.
     * @param contact Facilitator's contact.
     * @param instituition Facilitator's institution.
     */
    public FacilitatorImp(String areaOfExpertise, String name, String email, Contact contact, Instituition instituition) {
        super(name, email, contact, instituition);
        this.areaOfExpertise = areaOfExpertise;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAreaOfExpertise() {
        return this.areaOfExpertise;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAreaOfExpertise(String string) {
        this.areaOfExpertise = string;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("typeOfParticipant", this.getClass().getSimpleName());
        jsonObject.put("name", super.getName());
        jsonObject.put("email", super.getEmail());
        jsonObject.put("contact", ((ContactImp) super.getContact()).toJsonObj());
        jsonObject.put("instituition", ((InstituitionImp) super.getInstituition()).toJsonObj());
        jsonObject.put("areaOfExpertise", areaOfExpertise);
        return jsonObject;
    }
    
}
