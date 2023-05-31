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

/**
 *
 * @author David Santos
 */
public class Contacts implements Contact {
    private String street, city, state, zipCode, country, phone;

    public Contacts(String street, String city, String state, String zipCode, String country, String phone) {
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
    
    
    /**
     * Comparação de contactos  pelo número de telefone (perguntar stor)
     * @param o
     * @return 
     */
    @Override
     public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || !(o instanceof Contact)) return false;
        Contact c = (Contact) o;
        return this.getPhone().equals(c.getPhone());
     }
    
    
}
