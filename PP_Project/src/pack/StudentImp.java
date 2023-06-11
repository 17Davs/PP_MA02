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
import org.json.simple.JSONObject;

public class StudentImp extends ParticipantImp implements Student {

    /**
     * @param counter This counter increases Student's number.
     * @param number Student's number.
     */
    private static int counter = 0;
    private int number;

    /**
     * This is the constructor method for Student.
     * 
     * @param name Student name.
     * @param email Student email.
     * @param contact Student contact.
     * @param instituition Student institution.
     */
    public StudentImp(String name, String email, Contact contact, Instituition instituition) {
        super(name, email, contact, instituition);
        this.number = ++counter;
    }

    @Override
    public int getNumber() {
        return number;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("typeOfParticipant", this.getClass().getSimpleName());
        jsonObject.put("name", super.getName());
        jsonObject.put("email", super.getEmail());
        jsonObject.put("contact", ((ContactImp) super.getContact()).toJsonObj());
        jsonObject.put("instituition", ((InstituitionImp) super.getInstituition()).toJsonObj());
        jsonObject.put("number", number);

        return jsonObject;
    }

}
