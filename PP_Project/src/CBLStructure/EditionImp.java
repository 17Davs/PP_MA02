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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;
import ma02_resources.project.Status;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author David Santos
 */
public class EditionImp implements Edition {

    //private static final int initialSize = 5;
    private String name;
    private LocalDate start, end;
    private Status status;
    private int numberOfProjects;
    private static final String projectTemplate = "src/Files/project_template.json";
    private Project projects[];

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

    // use project equals not viable for this situation unless we create a Project temp obj with that name
    private boolean hasProject(String name) {
        for (Project p : projects) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addProject(String name, String description, String[] tags) throws IOException, ParseException {
        if (name == null || description == null || tags == null) {
            throw new IllegalArgumentException("Illegal (null) argument");
        }

        if (hasProject(name)) {
            throw new IllegalArgumentException("Illegal (null) argument");
        }

        try {
            Reader reader = new FileReader(projectTemplate);

            JSONParser parser = new JSONParser();
            JSONObject object;

            object = (JSONObject) parser.parse(reader);

            // falta criar objeto
            int number_of_facilitors = (int) object.get("number_of_facilitors");
            int number_of_students = (int) object.get("number_of_students");
            int number_of_partners = (int) object.get("number_of_partners");

            // para arrays criar array e depois percorrer e ler 
            JSONArray taskArray = (JSONArray) object.get("tasks");

            Project newPj = new ProjectImp(name, description, number_of_facilitors, number_of_students, number_of_partners, taskArray.size() , tags);
            
            for (int i = 0; i < taskArray.size(); i++) {
                JSONObject aTask = (JSONObject) taskArray.get(i);
                String title = (String) aTask.get("title");
                String taskDescription = (String) aTask.get("description");
                int start_at = (int) aTask.get("start_at");
                int duration = (int) aTask.get("duration");
                
                //calculate the start and the  end days
                
                // create task
                
                //add to the new project
                
            }
        } catch (IOException ex) {
            throw new IOException("Project template not found!");
        } catch (org.json.simple.parser.ParseException ex) {
            throw new ParseException("Project template not valid!", 0);
        }

    }

    @Override
    public void removeProject(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Project getProject(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Project[] getProjects() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Project[] getProjectsByTag(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Project[] getProjectsOf(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
