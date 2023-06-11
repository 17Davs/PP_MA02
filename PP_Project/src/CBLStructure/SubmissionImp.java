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
import org.json.simple.JSONObject;
import pack.ParticipantImp;
import pack.StudentImp;

public class SubmissionImp implements Submission {

    /**
     * Variable that defines date of submission.
     */
    private LocalDateTime date;
    /**
     * The student that submitted the work.
     */
    private Student student;
    /**
     * The description of the submission/work.
     */
    private String text;

    /**
     * This is the constructor method for Submission.
     * 
     * @param student The student that submitted the work.
     * @param text The description of the submission/work
     */
    public SubmissionImp(Student student, String text) {
        this.date = LocalDateTime.now();
        this.student = student;
        this.text = text;
    }

    /**
     * This is one of the constructor methods for Submission. This one is used
     * when we want to import form the json file a submission that was created 
     * previously.
     * @param student The student that submitted the work.
     * @param text The description of the submission/work
     * @param date The submissions date
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
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Submission sbmsn) {
        if (this == sbmsn) {
            return 0;
        }
        return date.compareTo(sbmsn.getDate());
    }

    /**
     * This method is used to export to a JSON file, information of a submission.
     * @return A Json Object with the submission's information
     */
    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", text);
        jsonObject.put("date", date.toString());
        jsonObject.put("student", ((StudentImp) student).toJsonObj());

        return jsonObject;
    }

    /**
     * This method is used to import information about a submission from a JSON
     * file.
     * @param jsonObject The JSON Object containing the information to be retrived
     * @return A submission creted based on the information retrived
     */
    public static SubmissionImp fromJsonObj(JSONObject jsonObject) {
        String text = (String) jsonObject.get("text");
        LocalDateTime date = LocalDateTime.parse((String) jsonObject.get("date"));
        Student student = (Student) ParticipantImp.fromJsonObj((JSONObject) jsonObject.get("student"));

        return new SubmissionImp(student, text, date);
    }

}
