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

import Exceptions.AlreadyExistsInArray;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import ma02_resources.participants.Facilitator;
import ma02_resources.participants.Participant;
import ma02_resources.participants.Partner;
import ma02_resources.participants.Student;
import ma02_resources.project.Project;
import ma02_resources.project.Task;
import ma02_resources.project.exceptions.IllegalNumberOfParticipantType;
import ma02_resources.project.exceptions.IllegalNumberOfTasks;
import ma02_resources.project.exceptions.ParticipantAlreadyInProject;
import ma02_resources.project.exceptions.TaskAlreadyInProject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pack.ParticipantImp;

/**
 *
 * @author David Santos
 */
public class ProjectImp implements Project {

    private String name, description;
    private int numberOfFacilitators, numberOfStudents, numberOfPartners, numberOfParticipants,
            numberOfTasks, maximumNumberOfTasks, maximumNumberOfStudents,
            maximumNumberOfPartners, maximumNumberOfFacilitators, numberOfTags;
    private long maximumNumberOfParticipants;
    private Task tasks[];
    private Participant participants[];
    private String[] tags;

//    public ProjectImp(String name, String description, int maximumNumberOfTasks, int maximumNumberOfStudents,
//            int maximumNumberOfPartners, int maximumNumberOfFacilitators, long maximumNumberOfParticipants) {
//        this.name = name;
//        this.description = description;
//        this.maximumNumberOfTasks = maximumNumberOfTasks;
//        this.maximumNumberOfStudents = maximumNumberOfStudents;
//        this.maximumNumberOfPartners = maximumNumberOfPartners;
//        this.maximumNumberOfFacilitators = maximumNumberOfFacilitators;
//        this.maximumNumberOfParticipants = maximumNumberOfParticipants;
//        this.numberOfFacilitators = numberOfStudents = numberOfPartners
//                = numberOfParticipants = numberOfTags = numberOfTasks = 0;
//        this.tasks = new Task[maximumNumberOfTasks];
//        this.participants = new Participant[(int) maximumNumberOfParticipants];
//        this.tags = new String[2];
//    }
    public ProjectImp(String name, String description, int numberOfFacilitators, int numberOfStudents, int numberOfPartners, int taskArraySize, String[] tags) {

        this.name = name;
        this.description = description;

        this.numberOfFacilitators = this.numberOfStudents = this.numberOfPartners
                = this.numberOfParticipants = this.numberOfTasks = this.numberOfTags = 0;

        //The limits variables need to be created based  on the arguments
        this.maximumNumberOfTasks = taskArraySize;
        this.maximumNumberOfStudents = numberOfStudents;
        this.maximumNumberOfPartners = numberOfPartners;
        this.maximumNumberOfFacilitators = numberOfFacilitators;
        this.maximumNumberOfParticipants = this.maximumNumberOfStudents + this.maximumNumberOfPartners + this.maximumNumberOfFacilitators;

        this.tasks = new Task[maximumNumberOfTasks];
        this.participants = new Participant[(int) maximumNumberOfParticipants];
        this.tags = new String[2];

        for (String tag : tags) {
            try {
                this.addTags(tag);
            } catch (AlreadyExistsInArray e) {
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
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
    public int getNumberOfFacilitators() {
        return numberOfFacilitators;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfPartners() {
        return numberOfPartners;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfTasks() {
        return numberOfTasks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumNumberOfTasks() {
        return maximumNumberOfTasks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getMaximumNumberOfParticipants() {
        return maximumNumberOfParticipants;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumNumberOfStudents() {
        return maximumNumberOfStudents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumNumberOfPartners() {
        return maximumNumberOfPartners;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumNumberOfFacilitators() {
        return maximumNumberOfFacilitators;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task[] getTasks() {
        Task[] temp = new Task[numberOfTasks];
        int i = 0;
        for (Task t : tasks) {
            if (t != null) {
                temp[i++] = t;
            }
        }
        return temp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    private boolean hasParticipant(Participant p) {
        for (Participant participant : participants) {
            if (participant != null && participant.equals(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addParticipant(Participant p) throws IllegalNumberOfParticipantType, ParticipantAlreadyInProject {

        if (p instanceof Facilitator) {
            if (numberOfFacilitators == maximumNumberOfFacilitators) {
                throw new IllegalNumberOfParticipantType("Maximum ammount of Facilitators for Project!");
            }
        } else if (p instanceof Student) {
            if (numberOfStudents == maximumNumberOfStudents) {
                throw new IllegalNumberOfParticipantType("Maximum ammount of Students for Project!");
            }
        } else if (p instanceof Partner) {
            if (numberOfPartners == maximumNumberOfPartners) {
                throw new IllegalNumberOfParticipantType("Maximum ammount of Partners for Project!");
            }
        }

        if (hasParticipant(p)) {
            throw new ParticipantAlreadyInProject("Participant already exists in Project!");
        }

        participants[numberOfParticipants++] = p;

        if (p instanceof Facilitator) {
            numberOfFacilitators++;
        } else if (p instanceof Student) {
            numberOfStudents++;
        } else if (p instanceof Partner) {
            numberOfPartners++;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Project)) {
            return false;
        }
        final Project other = (Project) obj;

        return this.name.equals(other.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Participant removeParticipant(String string) {
        Participant deleted = new ParticipantImp(null, string, null, null);
        int pos = -1, i = 0;
        while (pos == -1 && i < numberOfParticipants) {

            if (participants[i].equals(deleted)) {
                pos = i;
                deleted = participants[i];
            } else {
                i++;
            }
        }
        if (pos == -1) {
            throw new IllegalArgumentException("No Participant found!");
        }
        for (i = pos; i < numberOfParticipants; i++) {
            participants[i] = participants[i + 1];
        }
        participants[--numberOfParticipants] = null;

        if (deleted instanceof Facilitator) {
            numberOfFacilitators--;
        } else if (deleted instanceof Student) {
            numberOfStudents--;
        } else if (deleted instanceof Partner) {
            numberOfPartners--;
        }

        return deleted;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Participant getParticipant(String string) {
        Participant p = new ParticipantImp(null, string, null, null);

        for (int i = 0; i < numberOfParticipants; i++) {

            if (participants[i].equals(p)) {
                p = participants[i];
                return p;
            }
        }
        if (p == null) {
            throw new IllegalArgumentException("No Participant found!");
        }
        return p;
    }
///////////////////////////////////////////////////

    private void reallocTags() {
        String[] temp = new String[tags.length * 2];
        int i = 0;
        for (String t : tags) {
            temp[i++] = t;
        }
        tags = temp;
    }

    public void addTags(String t) throws AlreadyExistsInArray {
        if (t == null) {
            throw new IllegalArgumentException("Null argument");
        }

        if (hasTag(t)) {
            throw new AlreadyExistsInArray("Tag already exists");
        }

        if (numberOfTags == tags.length) {
            reallocTags();
        }
        tags[numberOfTags++] = t;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getTags() {
        String[] temp = null;
        int i = 0;
        for (String s : tags) {
            temp[i++] = s;
        }
        return temp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasTag(String string) {
        for (String s : tags) {
            if (s != null && s.equals(string)) {
                return true;
            }
        }
        return false;
    }
//////////////////////////////////////////

    private boolean hasTask(Task task) {
        for (Task t : tasks) {
            if (t != null && t.equals(task)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTask(Task task) throws IllegalNumberOfTasks, TaskAlreadyInProject {
        if (numberOfTasks == maximumNumberOfTasks) {
            throw new IllegalNumberOfTasks("Maximum ammount of Task reached in project!");
        }
        if (hasTask(task)) {
            throw new TaskAlreadyInProject("Task already exists in Project!");
        }

        tasks[numberOfTasks++] = task;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task getTask(String string) {
        Task t = new TaskImp(string, null, null, null, 0);

        for (int i = 0; i < numberOfTasks; i++) {

            if (tasks[i].equals(t)) {
                return tasks[i];
            }
        }
        throw new IllegalArgumentException("No task found!");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCompleted() {
        if (this.numberOfTasks != this.maximumNumberOfTasks) {
            return false;
        }
        for (Task t : tasks) {
            if (t.getNumberOfSubmissions() < 1) {
                return false;
            }
        }
        return true;
    }

    /*
    this.name = name;
        this.description = description;

        this.numberOfFacilitators = this.numberOfStudents = this.numberOfPartners
                = this.numberOfParticipants = this.numberOfTasks = this.numberOfTags = 0;

        //The limits variables need to be created based  on the arguments
        this.maximumNumberOfTasks = taskArraySize;
        this.maximumNumberOfStudents = numberOfStudents;
        this.maximumNumberOfPartners = numberOfPartners;
        this.maximumNumberOfFacilitators = numberOfFacilitators;
        this.maximumNumberOfParticipants = this.maximumNumberOfStudents + this.maximumNumberOfPartners + this.maximumNumberOfFacilitators;

    */
    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("name", name);
        jsonObject.put("description", description);
        jsonObject.put("numberOfFacilitators", numberOfFacilitators);
        jsonObject.put("numberOfStudents", numberOfStudents);
        jsonObject.put("numberOfPartners", numberOfPartners);
        jsonObject.put("numberOfParticipants", numberOfParticipants);
        jsonObject.put("numberOfTasks", numberOfTasks);
        jsonObject.put("numberOfTags", numberOfTags);
       

        jsonObject.put("maximumNumberOfFacilitators", maximumNumberOfFacilitators);
        jsonObject.put("maximumNumberOfStudents", maximumNumberOfStudents);
        jsonObject.put("maximumNumberOfPartners", maximumNumberOfPartners);
        jsonObject.put("maximumNumberOfParticipants", maximumNumberOfParticipants);
        jsonObject.put("maximumNumberOfTasks", maximumNumberOfTasks);
        
        
        JSONArray tasksArray = new JSONArray();
        for (int i=0; i<numberOfTasks;i++) {
            tasksArray.add(((TaskImp)tasks[i]).toJsonObj());
        }
        jsonObject.put("tasks", tasksArray);

        JSONArray participantsArray = new JSONArray();
        for (int i=0; i< numberOfParticipants;i++) {
            participantsArray.add(((ParticipantImp)participants[i]).toJsonObj());
        }
        jsonObject.put("participants", participantsArray);

        JSONArray tagsArray = new JSONArray();
        for (int i=0; i<numberOfTags;i++) {
            tagsArray.add(tags[i]);
        }
        jsonObject.put("tags", tagsArray);

        return jsonObject;
    }
    
    
     //to do
    @Override
    public String toString() {
        return "ProjectImp{" + "name=" + name + ", description=" + description
                + ", numberOfFacilitators=" + numberOfFacilitators
                + ", numberOfStudents=" + numberOfStudents + ", numberOfPartners="
                + numberOfPartners + ", numberOfParticipants=" + numberOfParticipants
                + ", numberOfTasks=" + numberOfTasks + ", maximumNumberOfTasks="
                + maximumNumberOfTasks + ", maximumNumberOfStudents="
                + maximumNumberOfStudents + ", maximumNumberOfPartners="
                + maximumNumberOfPartners + ", maximumNumberOfFacilitators="
                + maximumNumberOfFacilitators + ", numberOfTags=" + numberOfTags
                + ", maximumNumberOfParticipants=" + maximumNumberOfParticipants
                + ", tasks=" + Arrays.toString(tasks) + ", participants=" + Arrays.toString(participants) + ", tags="
                + Arrays.toString(tags) + '}';
    }

}
