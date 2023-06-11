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
import ma02_resources.participants.InstituitionType;
import org.json.simple.JSONObject;

public class InstituitionImp implements Instituition {

    /**
     * @param name Institution's name.
     * @param email Institution's email.
     * @param website Institution's website.
     * @param description Institution's description.
     * @param contact Institution's contact.
     * @param type Institution's type.
     */
    private String name, email, website, description;
    private Contact contact;
    private InstituitionType type;

    /**
     * This is the constructor for Institution.
     * @param name Institution's name.
     * @param email Institution's email.
     * @param website Institution's website.
     * @param description Institution's description.
     * @param contact Institution's contact.
     * @param type Institution's type.
     */
    public InstituitionImp(String name, String email, String website, String description, Contact contact, InstituitionType type) {
        this.name = name;
        this.email = email;
        this.website = website;
        this.description = description;
        this.contact = contact;
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWebsite() {
        return website;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact getContact() {
        return contact;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstituitionType getType() {
        return type;
    }

    @Override
    public void setWebsite(String string) {
        this.website = string;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDescription(String string) {
        this.description = string;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContact(Contact cntct) {
        this.contact = cntct;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setType(InstituitionType it) {
        this.type = it;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Instituition)) {
            return false;
        }
        final Instituition other = (Instituition) obj;
        return this.name.equals(other.getName());
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("email", email);
        jsonObject.put("website", website);
        jsonObject.put("description", description);
        jsonObject.put("contact", ((ContactImp) contact).toJsonObj());
        
        //Error if its InstituitionType.UNIVERSITY, the toString given is "universitary", it causes problems when importing
        if (this.type == InstituitionType.UNIVERSITY){
            jsonObject.put("type", "University");
        } else {
            jsonObject.put("type", type.toString());
        }

        return jsonObject;
    }

    public static Instituition fromJsonObj(JSONObject jsonObject) {

        String name = (String) jsonObject.get("name");
        String email = (String) jsonObject.get("email");
        String website = (String) jsonObject.get("website");
        String description = (String) jsonObject.get("description");
        
        JSONObject contactJson = (JSONObject) jsonObject.get("contact");
        Contact contact = ContactImp.fromJsonObj(contactJson);
        
        InstituitionType type = InstituitionType.valueOf(((String) jsonObject.get("type")).toUpperCase());
        
        Instituition instituition = new InstituitionImp(name, email, website, description, contact, type);
        
        return instituition;
    }

}
