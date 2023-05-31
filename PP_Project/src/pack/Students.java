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
import ma02_resources.participants.Student;


/**
 *
 * @author David Santos
 */
public class Students extends Participants implements Student {
    
    private int number;

    public Students(int number, String name, String email, Contact contact, Instituition instituition) {
        super(name, email, contact, instituition);
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
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
        final Students other = (Students) obj;
        return this.number == other.number;
    }
    
    
    
}
