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
import ma02_resources.participants.Partner;
import org.json.simple.JSONObject;

public class PartnerImp extends ParticipantImp implements Partner {

    /**
     * @param vat Partner's VAT number.
     * @param website Partner's website.
     */
    private String vat, website;

    /**
     * This is the constructor for Partner.
     * 
     * @param vat Partner's VAT number.
     * @param website Partner's website.
     * @param name Partner's name.
     * @param email Partner's email.
     * @param contact Partner's contact.
     * @param instituition Partner's institution.
     */
    public PartnerImp(String vat, String website, String name, String email, Contact contact, Instituition instituition) {
        super(name, email, contact, instituition);
        this.vat = vat;
        this.website = website;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVat() {
        return this.vat;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWebsite() {
        return this.website;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("typeOfParticipant", this.getClass().getSimpleName());
        jsonObject.put("name", super.getName());
        jsonObject.put("email", super.getEmail());
        jsonObject.put("contact", ((ContactImp) super.getContact()).toJsonObj());
        jsonObject.put("instituition", ((InstituitionImp) super.getInstituition()).toJsonObj());
        jsonObject.put("vat", vat);
        jsonObject.put("website", website);
        return jsonObject;
    }

}
