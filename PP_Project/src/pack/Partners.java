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

import java.util.Objects;
import ma02_resources.participants.Contact;
import ma02_resources.participants.Instituition;
import ma02_resources.participants.Partner;

/**
 *
 * @author David Santos
 */
public class Partners extends Participants implements Partner {
    
    private String vat, website;

    public Partners(String vat, String website, String name, String email, Contact contact, Instituition instituition) {
        super(name, email, contact, instituition);
        this.vat = vat;
        this.website = website;
    }

    @Override
    public String getVat() {
        return this.vat;
    }

    @Override
    public String getWebsite() {
        return this.website;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Partners other = (Partners) obj;
        if (!Objects.equals(this.vat, other.vat)) {
            return false;
        }
        return Objects.equals(this.website, other.website);
    }

    
    
    
}
