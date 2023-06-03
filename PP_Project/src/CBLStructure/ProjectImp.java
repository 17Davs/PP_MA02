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

    public ProjectImp(String name, String description, int maximumNumberOfTasks, int maximumNumberOfStudents,
            int maximumNumberOfPartners, int maximumNumberOfFacilitators, long maximumNumberOfParticipants) {
        this.name = name;
        this.description = description;
        this.maximumNumberOfTasks = maximumNumberOfTasks;
        this.maximumNumberOfStudents = maximumNumberOfStudents;
        this.maximumNumberOfPartners = maximumNumberOfPartners;
        this.maximumNumberOfFacilitators = maximumNumberOfFacilitators;
        this.maximumNumberOfParticipants = maximumNumberOfParticipants;
        this.numberOfFacilitators = numberOfStudents = numberOfPartners
                = numberOfParticipants = numberOfTags = numberOfTasks = 0;
        this.tasks = new Task[maximumNumberOfTasks];
        this.participants = new Participant[(int) maximumNumberOfParticipants];
        this.tags = new String[2];
    }

    public ProjectImp(String name, String description, int numberOfFacilitators, int numberOfStudents, int numberOfPartners, int taskArraySize, String[] tags) {
        
        this.name = name;
        this.description = description;
        this.numberOfFacilitators = 0;
        this.numberOfStudents = 0;
        this.numberOfPartners = 0;
        this.numberOfParticipants = 0;
        this.numberOfTasks = 0;
        this.numberOfTags = 0;

        //The maximums variables need to be crreated. At the time they are being
        //  created using the given numbers + value (to never be 0)
        this.maximumNumberOfTasks = taskArraySize;
        this.maximumNumberOfStudents = 10 * numberOfStudents + 1;
        this.maximumNumberOfPartners = 2 * numberOfPartners + 1;
        this.maximumNumberOfFacilitators = 2 * numberOfFacilitators + 3;
        this.maximumNumberOfParticipants = this.maximumNumberOfStudents + this.maximumNumberOfPartners + this.maximumNumberOfFacilitators;

        this.tasks = new Task[maximumNumberOfTasks];
        this.participants = new Participant[(int) maximumNumberOfParticipants];
        this.tags = new String[2];

        for (String tag : tags) {
            try {
                this.addTags(tag);
            
            } catch (AlreadyExistsInArray e){
                
            }
        }

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getNumberOfFacilitators() {
        return numberOfFacilitators;
    }

    @Override
    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    @Override
    public int getNumberOfPartners() {
        return numberOfPartners;
    }

    @Override
    public int getNumberOfTasks() {
        return numberOfTasks;
    }

    @Override
    public int getMaximumNumberOfTasks() {
        return maximumNumberOfTasks;
    }

    @Override
    public long getMaximumNumberOfParticipants() {
        return maximumNumberOfParticipants;
    }

    @Override
    public int getMaximumNumberOfStudents() {
        return maximumNumberOfStudents;
    }

    @Override
    public int getMaximumNumberOfPartners() {
        return maximumNumberOfPartners;
    }

    @Override
    public int getMaximumNumberOfFacilitators() {
        return maximumNumberOfFacilitators;
    }

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

    @Override
    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    private boolean hasParticipant(Participant p) {
        for (Participant participant : participants) {
            if (participant.equals(p)) {
                return true;
            }
        }
        return false;
    }

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

    @Override
    public Participant removeParticipant(String string) {
        Participant deleted = null;
        int pos = -1, i = 0;
        while (pos == -1 && i < numberOfParticipants) {

            if (participants[i].getEmail().equals(string)) {
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

    @Override
    public Participant getParticipant(String string) {
        Participant p = null;

        for (int i = 0; i < numberOfParticipants; i++) {

            if (participants[i].getEmail().equals(string)) {
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

    @Override
    public String[] getTags() {
        String[] temp = null;
        int i = 0;
        for (String s : tags) {
            temp[i++] = s;
        }
        return temp;
    }

    @Override
    public boolean hasTag(String string) {
        for (String s : tags) {
            if (s.equals(string)) {
                return true;
            }
        }
        return false;
    }
//////////////////////////////////////////

    private boolean hasTask(Task task) {
        for (Task t : tasks) {
            if (t.equals(task)) {
                return true;
            }
        }
        return false;
    }

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

    @Override
    public Task getTask(String string) {
        Task t = null;

        for (int i = 0; i < numberOfTasks; i++) {
            if (tasks[i].getTitle().equals(string)) {
                t = tasks[i];
                return t;
            }
        }
        throw new IllegalArgumentException("No task found!");

    }

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

    @Override
    public String toString() {
        return "ProjectImp{" + "name=" + name + ", description=" + description + 
                ", numberOfFacilitators=" + numberOfFacilitators + 
                ", numberOfStudents=" + numberOfStudents + ", numberOfPartners=" 
                + numberOfPartners + ", numberOfParticipants=" + numberOfParticipants 
                + ", numberOfTasks=" + numberOfTasks + ", maximumNumberOfTasks="
                + maximumNumberOfTasks + ", maximumNumberOfStudents=" 
                + maximumNumberOfStudents + ", maximumNumberOfPartners=" + 
                maximumNumberOfPartners + ", maximumNumberOfFacilitators=" + 
                maximumNumberOfFacilitators + ", numberOfTags=" + numberOfTags + 
                ", maximumNumberOfParticipants=" + maximumNumberOfParticipants + 
                ", tasks=" + Arrays.toString(tasks) + ", participants=" + Arrays.toString(participants) + ", tags="
                + Arrays.toString(tags) + '}';
    }
    

}
