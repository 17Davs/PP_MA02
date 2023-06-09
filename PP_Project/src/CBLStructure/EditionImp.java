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

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import ma02_resources.participants.Participant;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;
import ma02_resources.project.Status;
import ma02_resources.project.exceptions.IllegalNumberOfTasks;
import ma02_resources.project.exceptions.TaskAlreadyInProject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class EditionImp implements Edition {

    /**
     * Variable that defines Edition name.
     */
    private String name;
    /**
     * Variables that defines Edition's start and end date.
     */
    private LocalDate start, end;
    /**
     * Variable that defines the Edition's status from ACTIVE, INACTIVE, CANCELED, CLOSED.
     */
    private Status status;
    /**
     * Variable that defines how many Editions are in a list.
     */
    private int numberOfProjects;
    /**
     * Project template defined by user.
     */
    private String projectTemplate;
    /**
     * Default structure of project when one isn't chosen.
     */
    private static final String defaultProjectTemplate = "src/Files/project_template.json";
    /**
     * List of Projects.
     */
    private Project projects[];

    /**
     * This is the constructor method for Edition.
     *
     * @param name Edition name
     * @param start Edition's start date
     * @param end Edition's end date
     */
    public EditionImp(String name, LocalDate start, LocalDate end) {
        this.name = name;
        this.start = start;
        this.end = end;
        projectTemplate = defaultProjectTemplate;
        status = Status.INACTIVE;
        projects = new Project[5];
        numberOfProjects = 0;
    }

    /**
     * This is the constructor method for Edition.
     *
     * @param name Edition name
     * @param start Edition's start date
     * @param end Edition's end date
     * @param status Edition's status
     * @param projectTemplate Project template defined by user.
     */
    public EditionImp(String name, LocalDate start, LocalDate end, Status status, String projectTemplate) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.status = status;
        this.projectTemplate = projectTemplate;
        projects = new Project[5];
        numberOfProjects = 0;
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
    public LocalDate getStart() {
        return start;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProjectTemplate() {
        return projectTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfProjects() {
        return numberOfProjects;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getEnd() {
        return end;
    }

    /**
     * This method searches for a project in the list.
     * 
     * @param pj Project to be searched.
     * @return true if it finds a project.
     * @return false if it doesn't find a project.
     */
    private boolean hasProject(Project pj) {
        for (Project p : projects) {
            if (p != null && p.equals(pj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method adds space to the Projects list.
     */
    private void reallocProjects() {
        Project[] temp = new Project[projects.length * 2];
        for (int i = 0; i < projects.length; i++) {
            temp[i] = projects[i];
        }
        projects = temp;
    }

    /**
     * This method adds a project to the edition. The project is created from
     * the template.
     *
     * @param name The name of the Project
     * @param description The description of the project
     * @param tags the tags for the project
     * @throws IOException if the project template is not found.
     * @throws ParseException if the project template is not valid.
     */
    @Override
    public void addProject(String name, String description, String[] tags) throws IOException, ParseException {
        if (name == null || description == null || tags == null) {
            throw new IllegalArgumentException("Illegal (null) argument");
        }

        //verify space
        if (numberOfProjects == projects.length) {
            reallocProjects();
        }

        //creating from the template
        try {
            Reader reader = new FileReader(projectTemplate);

            JSONParser parser = new JSONParser();
            JSONObject object;

            object = (JSONObject) parser.parse(reader);

            long number_of_facilitors = (long) object.get("number_of_facilitors");
            long number_of_students = (long) object.get("number_of_students");
            long number_of_partners = (long) object.get("number_of_partners");
            System.out.println(number_of_partners);

            // Create a json array and read into it the tasks of the template 
            JSONArray taskArray = (JSONArray) object.get("tasks");

            Project newPj = new ProjectImp(name, description, (int) number_of_facilitors, (int) number_of_students, (int) number_of_partners, taskArray.size(), tags);
            if (hasProject(newPj)) {
                throw new IllegalArgumentException("Project already exists");
            }
            for (int i = 0; i < taskArray.size(); i++) {
                JSONObject aTask = (JSONObject) taskArray.get(i);
                String title = (String) aTask.get("title");
                String taskDescription = (String) aTask.get("description");
                long start_at = (long) aTask.get("start_at");
                long duration = (long) aTask.get("duration");

                //calculate the start and the  end days
                LocalDate taskStart = this.start.plusDays(start_at);
                LocalDate taskEnd = this.start.plusDays(duration);
                // create task

                try {
                    //add to the new project
                    newPj.addTask(new TaskImp(title, taskDescription, taskStart, taskEnd, (int) duration));
                } catch (IllegalNumberOfTasks | TaskAlreadyInProject ex) {
                    Logger.getLogger(EditionImp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //add the project to the array
            projects[numberOfProjects++] = newPj;

        } catch (IOException ex) {
            throw new IOException("Project template not found!");
        } catch (org.json.simple.parser.ParseException ex) {
            throw new ParseException("Project template not valid!", 0);
        }

    }

    /**
     * This method removes a project from the edition. The project is identified
     * by its name.
     *
     * @param string The name of the project.
     * @throws IllegalArgumentException - if the project name is null or empty,
     * or if the project does not exist.
     */
    @Override
    public void removeProject(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Null argument!");
        }
        String[] tags = {"null"};
        Project project = new ProjectImp(string, null, 0, 0, 0, 0, tags);

        int pos = -1, i = 0;

        while (pos == -1 && i < numberOfProjects) {

            if (projects[i].equals(project)) {
                pos = i;
            } else {
                i++;
            }
        }
        if (pos == -1) {
            throw new IllegalArgumentException("No project found with that argument!");
        }
        for (i = pos; i < numberOfProjects; i++) {
            projects[i] = projects[i + 1];
        }
        projects[--numberOfProjects] = null;
    }

    /**
     * This method searches for a project in a list of projects.
     *
     * @param string Project name.
     * @return Project in the list.
     * @throws IllegalArgumentException - If it doesn't find a project.
     */
    @Override
    public Project getProject(String string) {
        String[] tags = {"null"};
        Project p = new ProjectImp(string, null, 0, 0, 0, 0, tags);

        for (int i = 0; i < numberOfProjects; i++) {
            if (projects[i].equals(p)) {
                return projects[i];
            }
        }

        throw new IllegalArgumentException("No project found!");

    }

    /**
     *
     * {@inheritDoc}
     * 
     * This method creates a list with all the projects.
     */
    @Override
    public Project[] getProjects() {
        int counter=0;
        Project temp[] = new Project[numberOfProjects];

        for (int i = 0; i < numberOfProjects; i++) {
            temp[counter++] = projects[i];
        }
        
        return temp;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Project[] getProjectsByTag(String string) {
        Project[] temp = new Project[this.numberOfProjects];
        int counter = 0;
        for (int i = 0; i < numberOfProjects; i++) {
            if (projects[i].hasTag(string)) {
                temp[counter++] = projects[i];
            }
        }
        if (counter == 0) {
            return null;
        }

        if (counter != numberOfProjects) {
            Project[] trimmedTemp = new Project[counter];

            for (int i = 0; i < counter; i++) {
                trimmedTemp[i] = temp[i];
            }
            return trimmedTemp;
        }
        return temp;
    }

    /**
     *
     * {@inheritDoc}
     * 
     * This method returns a list of all projects by a certain participant.
     */
    @Override
    public Project[] getProjectsOf(String string) {
        Project[] temp = new Project[this.numberOfProjects];
        Participant participant;

        int counter = 0;
        for (int i = 0; i < numberOfProjects; i++) {
            try {
                projects[i].getParticipant(string);
                //if it didnt throw an exception, the project will be added to the array that will be later returned
                temp[counter++] = projects[i];
            } catch (IllegalArgumentException ignored) {
            }
        }

        if (counter != numberOfProjects) {
            Project[] trimmedTemp = new Project[counter];

            for (int i = 0; i < counter; i++) {
                trimmedTemp[i] = temp[i];
            }
            return trimmedTemp;
        }
        return temp;
    }
    

    /**
     * This method returns all the uncompleted projects of the edition
     *
     * @return an array of uncompleted projects
     */
    public Project[] getUncompletedProjects() {
        int counter =0;
        Project[] temp = new Project[this.numberOfProjects];

        for (int i = 0; i < numberOfProjects; i++) {
            if (!projects[i].isCompleted()) {
                temp[counter++] = projects[i];
            }
        }
        if (counter == 0){
            return null;
        }
        
        if (counter != numberOfProjects) {
            Project[] trimmedTemp = new Project[counter];

            for (int i = 0; i < counter; i++) {
                trimmedTemp[i] = temp[i];
            }
            return trimmedTemp;
        }
        return temp;
  
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Edition)) {
            return false;
        }
        final Edition other = (Edition) obj;
        return this.name.equals(other.getName());
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("start", start.toString());
        jsonObject.put("end", end.toString());
        jsonObject.put("status", status.toString());
        jsonObject.put("numberOfProjects", numberOfProjects);
        jsonObject.put("projectTemplate", projectTemplate);

        JSONArray projectsArray = new JSONArray();
        for (int i = 0; i < numberOfProjects; i++) {
            projectsArray.add(((ProjectImp) projects[i]).toJsonObj());
        }
        jsonObject.put("projects", projectsArray);

        return jsonObject;
    }

    public static Edition fromJsonObj(JSONObject jsonObject) {

        String name = (String) jsonObject.get("name");
        LocalDate start = LocalDate.parse((String) jsonObject.get("start"));
        LocalDate end = LocalDate.parse((String) jsonObject.get("end"));
        Status status = Status.valueOf(((String) jsonObject.get("status")).toUpperCase());
        String projectTemplate = (String) jsonObject.get("projectTemplate");

        EditionImp edition = new EditionImp(name, start, end, status, projectTemplate);

        JSONArray projectsArray = (JSONArray) jsonObject.get("projects");

        for (int i = 0; i < projectsArray.size(); i++) {
            try {

                JSONObject projectJson = (JSONObject) projectsArray.get(i);

                edition.addProjectFormImport(ProjectImp.fromJsonObj(projectJson));
            } catch (IllegalArgumentException e) {

            }
        }

        return edition;

    }

    /**
     * This method adds 
     * @param p 
     */
    private void addProjectFormImport(Project p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (hasProject(p)) {
            throw new IllegalArgumentException("Project already exists");
        }
        if (numberOfProjects == projects.length) {
            reallocProjects();
        }

        projects[numberOfProjects++] = p;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        // Count the total number of projects
        int completedProjects = 0;
        int totalCompletedTasks = 0;

        //count completed projects and tasks
        for (int i =0;i<numberOfProjects;i++) {
            if (projects[i].isCompleted()) {
                completedProjects++;
                totalCompletedTasks += projects[i].getNumberOfTasks();
            }
        }

        // Calculate the progress percentage
        int progressPercentage = (int) ((double) completedProjects / numberOfProjects * 100);

        // the textual representation of the progress
        String progressText = "Edition Progress: " + progressPercentage + "%\n\n";
        progressText += "Completed Projects: " + completedProjects + "/" + numberOfProjects + "\n";
        progressText += "Completed Tasks: " + totalCompletedTasks + "\n";
        progressText += " *** A task is considered completed if it has at least 1 submission *** \n";

        return progressText;
    }

}
