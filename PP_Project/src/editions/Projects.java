/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editions;

import Exceptions.AlreadyExistsInArray;
import ma02_resources.participants.Facilitator;
import pack.*;
import ma02_resources.participants.Participant;
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
public class Projects implements Project {

    private String name, description;
    private int numberOfFacilitators, numberOfStudents, numberOfPartners, numberOfParticipants,
            numberOfTasks, maximumNumberOfTasks, maximumNumberOfStudents,
            maximumNumberOfPartners, maximumNumberOfFacilitators, numberOfTags;
    private long maximumNumberOfParticipants;
    private Task tasks[];
    private Participant participants[];
    private String[] tags;
    
    

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

    @Override
    public void addParticipant(Participant p) throws IllegalNumberOfParticipantType, ParticipantAlreadyInProject {
       
        if (p instanceof Facilitator) {
            if (numberOfFacilitators == maximumNumberOfFacilitators) {
                throw new IllegalNumberOfParticipantType("Maximum ammount of Facilitators for Project!");
            }
        } else if (p instanceof Students) {
            if (numberOfStudents == maximumNumberOfStudents) {
                throw new IllegalNumberOfParticipantType("Maximum ammount of Students for Project!");
            }
        } else if (p instanceof Partners) {
            if (numberOfPartners == maximumNumberOfPartners) {
                throw new IllegalNumberOfParticipantType("Maximum ammount of Partners for Project!");
            }
        }
        //TAG (ASK WHAT IT IS)
        if (hasTag(p.getName())) {
            throw new ParticipantAlreadyInProject("Participant already exists in Project!");
        }

        participants[numberOfParticipants++] = p;

        if (p instanceof Facilitator) {
            numberOfFacilitators++;
        } else if (p instanceof Students) {
            numberOfStudents++;
        } else if (p instanceof Partners) {
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Projects other = (Projects) obj;
        
        return this.name.equals(other.name);
    }

    

    @Override
    public Participant removeParticipant(String string) {
        Participant deleted = null;
        int pos = -1, i = 0;
        while (pos == -1 && i < numberOfParticipants) {
            //assuming name as TAg
            if (participants[i].getEmail().equals(string)) {
                pos = i;
                deleted = participants[i];
            } else {
            i++;
            }
        }
        
        for (i = pos; i < numberOfParticipants; i++){
            participants[i] = participants[i+1];
        }
        participants[--numberOfParticipants] = null;
        
        if (deleted instanceof Facilitator) {
            numberOfFacilitators--;
        } else if (deleted instanceof Students) {
            numberOfStudents--;
        } else if (deleted instanceof Partners) {
            numberOfPartners--;
        }
        
        return deleted;
    }

    @Override
    public Participant getParticipant(String string) {
        Participant p = null;
        
        for(int i =0; i < numberOfParticipants; i++) {
            
            if (participants[i].getEmail().equals(string)) {
                p = participants[i];
                return p;
            }
        }
        return p;
    }
///////////////////////////////////////////////////
    private void reallocTags(){
        String[] temp = new String[tags.length * 2];
        int i=0;
        for (String t : tags){
            temp[i++] = t;
        }
        tags = temp;
    }
    
    public void addTags(String t) throws AlreadyExistsInArray{
        if (t == null) {
            throw new IllegalArgumentException("Null argument");
        }
        
        if (hasTag(t)) throw new AlreadyExistsInArray("Tag already exists");
        
        if (numberOfTags == tags.length){
            reallocTags();
        }
        tags[numberOfTags++] = t;
        
    }
    
      
    @Override
    public String[] getTags() {
        String[] temp = null;
        int i=0;
        for (String s : tags){
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
    private boolean hasTask(Task task){
        for (Task t : tasks){
            if (t.equals(task))
                return true;
        }
        return false;
    }
    
    @Override
    public void addTask(Task task) throws IllegalNumberOfTasks, TaskAlreadyInProject {
        if (numberOfTasks == maximumNumberOfTasks){
            throw new IllegalNumberOfTasks("Maximum ammount of Task reached in project!");
        }
        if (hasTask(task)){
            throw new TaskAlreadyInProject("Task already exists in Project!");
        }
        
        tasks[numberOfTasks++] = task; 

    }

    @Override
    public Task getTask(String string) { 
        Task t = null;
        
        for (int i=0; i<numberOfTasks;i++){
            if (tasks[i].getTitle().equals(string)){
                t = tasks[i];
                return t;
            }
        }
        return t;
    }

    @Override
    public boolean isCompleted() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
