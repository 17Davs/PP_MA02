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

/**
 *
 * @author David Santos
 */
public class ContactImp implements Contact {
    private String street, city, state, zipCode, country, phone;

    public ContactImp(String street, String city, String state, String zipCode, String country, String phone) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.phone = phone;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String getPhone() {
        return phone;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Contact)) {
            return false;
        }
        final Contact other = (Contact) obj;
        if (!this.street.equals(other.getStreet())) {
            return false;
        }
        if (!this.city.equals(other.getCity())) {
            return false;
        }
        if (!this.state.equals(other.getState())) {
            return false;
        }
        if (!this.zipCode.equals(other.getZipCode())) {
            return false;
        }
        if (!this.country.equals(other.getCountry())) {
            return false;
        }
        return this.phone.equals(other.getPhone());
    }
    
   
     
    
    
}
