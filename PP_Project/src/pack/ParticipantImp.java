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
import org.json.simple.JSONObject;

public class ParticipantImp implements Participant {

    /**
     * @param name Participant's name.
     * @param email Participant's email.
     * @param contact Participant's contact.
     * @param instituition Participant's institution.
     */
    private String name, email;
    private Contact contact;
    private Instituition instituition;

    /**
     * This is the constructor method for Participant. 
     * 
     * @param name Participant's name.
     * @param email Participant's email.
     * @param contact Participant's contact.
     * @param instituition Participant's institution.
     */
    public ParticipantImp(String name, String email, Contact contact, Instituition instituition) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.instituition = instituition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInstituition(Instituition instituition) {
        this.instituition = instituition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmail() {
        return this.email;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact getContact() {
        return this.contact;
    }

    /**
     * {@inheritDoc}
     */
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

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("typeOfParticiant", this.getClass().getSimpleName());
        jsonObject.put("name", name);
        jsonObject.put("email", email);
        jsonObject.put("contact", ((ContactImp) contact).toJsonObj());
        jsonObject.put("instituition", ((InstituitionImp) instituition).toJsonObj());

        return jsonObject;
    }

    public static Participant fromJsonObj(JSONObject jsonObject) {

        String name = (String) jsonObject.get("name");
        String email = (String) jsonObject.get("email");

        JSONObject contactJson = (JSONObject) jsonObject.get("contact");
        Contact contact = ContactImp.fromJsonObj(contactJson);

        JSONObject instituitionJson = (JSONObject) jsonObject.get("instituition");
        Instituition instituition = InstituitionImp.fromJsonObj(instituitionJson);

        String type = (String) jsonObject.get("typeOfParticipant");

        if (type.equals("StudentImp")) {
            return new StudentImp(name, email, contact, instituition);

        } else if (type.equals("FacilitatorImp")) {
            String areaOfExpertise = (String) jsonObject.get("areaOfExpertise");
            return new FacilitatorImp(areaOfExpertise, name, email, contact, instituition);

        } else if (type.equals("PartnerImp")) {
            String vat = (String) jsonObject.get("vat");
            String website = (String) jsonObject.get("website");
            return new PartnerImp(vat, website, name, email, contact, instituition);

        } else {
            return new ParticipantImp(name, email, contact, instituition);
        }
    }

}
