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
public class StudentImp extends ParticipantImp implements Student {
    
    private int number;

    public StudentImp(int number, String name, String email, Contact contact, Instituition instituition) {
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
        if (!(obj instanceof Student)) {
            return false;
        }
        final Student other = (Student) obj;
        return this.number == other.getNumber();
    }
     
    
}
