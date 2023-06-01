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

import java.time.LocalDate;
import ma02_resources.project.Submission;
import ma02_resources.project.Task;

/**
 *
 * @author David Santos
 */
public class TaskImp implements Task {

    private String title, description;
    private LocalDate start, end;
    private int duration, numberOfSubmissions;
    private Submission submissions[];

    public TaskImp(String title, String description, LocalDate start, LocalDate end, int duration) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.submissions = new Submission[2];
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public LocalDate getStart() {
        return start;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public LocalDate getEnd() {
        return end;
    }

    @Override
    public Submission[] getSubmissions() {
        Submission temp[] = new Submission[numberOfSubmissions];
        int i = 0;
        for (Submission s : submissions) {
            if (s != null) {
                temp[i++] = s;
            }
        }
        return temp;
    }

    @Override
    public int getNumberOfSubmissions() {
        return numberOfSubmissions;
    }

    private boolean hasSubmission(Submission sbmsn) {
        for (Submission s : submissions) {
            if (s.compareTo(sbmsn) == 0) {
                return true;
            }
        }
        return false;
    }

    private void reallocSubmissions(){
        Submission[] temp = new Submission[submissions.length * 2];
        int i = 0;
        for (Submission s : submissions){
            temp[i++] = s;
        }
        submissions = temp;
    }
    @Override
    public void addSubmission(Submission sbmsn) {
        if (sbmsn == null) {
            throw new  IllegalArgumentException("Null submission!");
        }
        if (this.numberOfSubmissions == submissions.length) {
            reallocSubmissions();
        }
        
        if (!hasSubmission(sbmsn)) {
            submissions[numberOfSubmissions++] = sbmsn;
        }

    }

    @Override
    public void extendDeadline(int days) {
        if (days < 0) {
            throw new IllegalArgumentException("Negative value");
        }
        LocalDate temp = end.plusDays(days);
        this.end = temp;
    }

    @Override
    public int compareTo(Task task) {
        if (this == task) {
            return 0;
        }
        return this.start.compareTo(task.getStart());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Task)) {
            return false;
        }
        final Task other = (Task) obj;
        return this.title.equals(other.getTitle());

    }

}
