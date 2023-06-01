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

/**
 *
 * @author David Santos
 */
public class SubmissionImp implements Submission {

    private LocalDateTime date;
    private Student student;
    private String text;

    public SubmissionImp(Student student, String text) {
        this.date = LocalDateTime.now();
        this.student = student;
        this.text = text;
    }

    @Override
    public Student getStudent() {
        return student;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public int compareTo(Submission sbmsn) {
        if (this == sbmsn) {
            return 0;
        } 
        return date.compareTo(sbmsn.getDate());
    }


}
