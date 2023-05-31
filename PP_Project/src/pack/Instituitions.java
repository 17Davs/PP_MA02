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



/**
 *
 * @author David Santos
 */
public class Instituitions implements Instituition {

    private String name, email, website, description;
    private Contact contact;
    private InstituitionType type;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getWebsite() {
        return website;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Contact getContact() {
        return contact;
    }

    @Override
    public InstituitionType getType() {
        return type;
    }

    @Override
    public void setWebsite(String string) {
        this.website = string;
    }

    @Override
    public void setDescription(String string) {
        this.description = string;
    }

    @Override
    public void setContact(Contact cntct) {
        this.contact = cntct;
    }

    @Override
    public void setType(InstituitionType it) {
        this.type = it;
    }

}
