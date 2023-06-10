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
import java.util.Arrays;
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

/**
 *
 * @author David Santos
 */
public class EditionImp implements Edition {

    /**
     *
     * @param name Edition name
     * @param start Edition's start date
     * @param end Edition's end date
     * @param status Edition's status
     * @param numberOfProjects Variable that defines how many Editions are in a
     * list
     * @param projectTemplate
     * @param defaultProjectTemplate
     * @param projects[] List of Editions
     */
    //private static final int initialSize = 5;
    private String name;
    private LocalDate start, end;
    private Status status;
    private int numberOfProjects;
    private String projectTemplate;
    private static final String defaultProjectTemplate = "src/Files/project_template.json";
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

    public EditionImp(String name, LocalDate start, LocalDate end, Status status, String projectTemplate) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.status = status;
        this.projectTemplate = projectTemplate;
        projects = new Project[5];
        numberOfProjects = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public LocalDate getStart() {
        return start;
    }

    @Override
    public String getProjectTemplate() {
        return projectTemplate;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int getNumberOfProjects() {
        return numberOfProjects;
    }

    @Override
    public LocalDate getEnd() {
        return end;
    }

    private boolean hasProject(Project pj) {
        for (Project p : projects) {
            if (p != null && p.equals(pj)) {
                return true;
            }
        }
        return false;
    }

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
        Project project = new ProjectImp(string, null, 0, 0, 0, 0, null);

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
     * This method
     *
     * @param string
     * @return
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
     */
    @Override
    public Project[] getProjects() {
        int counter=0;
        Project temp[] = new Project[numberOfProjects];

        for (int i = 0; i < numberOfProjects; i++) {
            temp[counter++] = projects[i];
        }
        if (counter == numberOfProjects){
            return temp;
        }
        
        Project trimmedTemp[] = new Project[counter];
        for (int i = 0; i < counter; i++){
            trimmedTemp[i] = temp[i];
        }
        return trimmedTemp;
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
            return temp;
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

        if (counter == 0) {
            return temp;
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
        Project[] temp = new Project[this.numberOfProjects];
        int i = 0;
        for (Project p : projects) {
            if (!p.isCompleted()) {
                temp[i++] = p;
            }
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

//    public String toJson() {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", name);
//        jsonObject.put("start", start.toString());
//        jsonObject.put("end", end.toString());
//        jsonObject.put("status", status.toString());
//        jsonObject.put("numberOfProjects", numberOfProjects);
//        jsonObject.put("projectTemplate", projectTemplate);
//
//        JSONArray projectsArray = new JSONArray();
//        for (int i=0; i<numberOfProjects; i++) {
//            projectsArray.add(((ProjectImp)projects[i]).toJson());
//        }
//        jsonObject.put("projects", projectsArray);
//
//        return jsonObject.toJSONString();
//    }
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

    public Project[] getProjectsByParticipant(Participant p) {
        int counter = 0;

        Project[] temp = new Project[numberOfProjects];

        for (int i = 0; i < numberOfProjects; i++) {

            try {
                Participant participant = projects[i].getParticipant(p.getEmail());

                temp[counter++] = projects[i];

            } catch (IllegalArgumentException e) {
            }
        }
        //limit the array to just the not null positions
        if (counter != numberOfProjects) {
            Project[] projectsByParticipant = new Project[counter];

            for (int i = 0; i < counter; i++) {
                projectsByParticipant[i] = temp[i];
            }
            return projectsByParticipant;
        }
        return temp;
    }

    @Override
    public String toString() {
        return "EditionImp{" + "name=" + name + ", start=" + start + ", end=" + end + status.toString() + ", numberOfProjects=" + numberOfProjects + ", projects=" + Arrays.toString(projects) + '}';
    }

}
