/*
 * Nome: Carolina Bonito Queiroga De Almeida
 * Número: 8180091 
 * Turna: LSIRCT1
 *
 * Nome: David Leandro Spencer Conceição dos Santos
 * Número: 8220651
 * Turna: LSIRCT1
 */
package CBLStructure;

import java.time.LocalDateTime;
import ma02_resources.participants.Student;
import ma02_resources.project.Submission;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pack.ParticipantImp;
import pack.StudentImp;

public class SubmissionImp implements Submission {

    /**
     * 
     * @param date Date of submission.
     * @param student A student that participates in the challenge.
     * @param text 
     */
    private LocalDateTime date;
    private Student student;
    private String text;

    /**
     * This is the constructor method for Submission.
     * 
     * @param student A student that participates in the challenge.
     * @param text 
     */
    public SubmissionImp(Student student, String text) {
        this.date = LocalDateTime.now();
        this.student = student;
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    public SubmissionImp(Student student, String text, LocalDateTime date) {
        this.date = date;
        this.student = student;
        this.text = text;
    }
        
    /**
     * {@inheritDoc}
     */
    @Override
    public Student getStudent() {
        return student;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * This method compares a 
     * 
     * @param sbmsn Submission.
     * @return date
     */
    @Override
    public int compareTo(Submission sbmsn) {
        if (this == sbmsn) {
            return 0;
        }
        return date.compareTo(sbmsn.getDate());
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", text);
        jsonObject.put("date", date.toString());
        jsonObject.put("student", ((StudentImp) student).toJsonObj());

        return jsonObject;
    }

    public static SubmissionImp fromJsonObj(JSONObject jsonObject) {
        String text = (String) jsonObject.get("text");
        LocalDateTime date = LocalDateTime.parse((String) jsonObject.get("date"));
        Student student = (Student) ParticipantImp.fromJsonObj((JSONObject) jsonObject.get("student"));

        return new SubmissionImp(student, text, date);
    }

}
