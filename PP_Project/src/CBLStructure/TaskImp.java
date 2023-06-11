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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TaskImp implements Task {

    /**
     * 
     * @param title 
     * @param description
     * @param start
     * @param end
     * @param duration
     * @param numberOfSubmissions
     * @param submissions[] 
     */
    private String title, description;
    private LocalDate start, end;
    private int duration, numberOfSubmissions;
    private Submission submissions[];

    /**
     * This is the constructor method for Task.
     * 
     * @param title Task title.
     * @param description Task description.
     * @param start Task start date.
     * @param end Task end date.
     * @param duration Task duration.
     */
    public TaskImp(String title, String description, LocalDate start, LocalDate end, int duration) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.submissions = new Submission[2];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getStart() {
        return start;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDuration() {
        return duration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getEnd() {
        return end;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfSubmissions() {
        return numberOfSubmissions;
    }

    /**
     * This method checks if a submission exists in the list.
     * 
     * @param sbmsn Submission to be checked.
     * @return true if submission exists.
     * @return false if submission doesn't exist.
     */
    private boolean hasSubmission(Submission sbmsn) {
        for (Submission s : submissions) {
            if (s != null && s.compareTo(sbmsn) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method adds more space to Submissions list.
     */
    private void reallocSubmissions() {
        Submission[] temp = new Submission[submissions.length * 2];
        int i = 0;
        for (Submission s : submissions) {
            temp[i++] = s;
        }
        submissions = temp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSubmission(Submission sbmsn) {
        if (sbmsn == null) {
            throw new IllegalArgumentException("Null submission!");
        }
        if (this.numberOfSubmissions == submissions.length) {
            reallocSubmissions();
        }

        if (!hasSubmission(sbmsn)) {
            submissions[numberOfSubmissions++] = sbmsn;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void extendDeadline(int days) {
        if (days < 0) {
            throw new IllegalArgumentException("Negative value");
        }
        LocalDate temp = end.plusDays(days);
        this.end = temp;
    }

    /**
     * {@inheritDoc}
     */
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

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("description", description);
        jsonObject.put("start", start.toString());
        jsonObject.put("end", end.toString());
        jsonObject.put("duration", duration);
        jsonObject.put("numberOfSubmissions", numberOfSubmissions);

        JSONArray submissionsArray = new JSONArray();
        for (int i = 0; i < numberOfSubmissions; i++) {
            submissionsArray.add(((SubmissionImp) submissions[i]).toJsonObj());
        }
        jsonObject.put("submissions", submissionsArray);

        return jsonObject;
    }

    public static Task fromJsonObj(JSONObject jsonObject) {
        String title = (String) jsonObject.get("title");
        String description = (String) jsonObject.get("description");
        LocalDate start = LocalDate.parse((String) jsonObject.get("start"));
        LocalDate end = LocalDate.parse((String) jsonObject.get("end"));
        int duration = ((Long) jsonObject.get("duration")).intValue();

        TaskImp task = new TaskImp(title, description, start, end, duration);

        JSONArray submissionsArray = (JSONArray) jsonObject.get("submissions");
        
        for (int i = 0; i < submissionsArray.size(); i++) {
            JSONObject submissionJson = (JSONObject) submissionsArray.get(i);
            task.addSubmission(SubmissionImp.fromJsonObj(submissionJson));
        }

        return task;
    }

    @Override
    public String toString() {
        return "Task {" + "title=" + title + ", description=" + description + ", start=" + start + ", end=" + end + ", duration=" + duration + ", numberOfSubmissions=" + numberOfSubmissions;
    }

}
