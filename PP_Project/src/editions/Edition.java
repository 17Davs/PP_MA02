/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editions;

import editions.Contracts.EditionContract;
import java.util.Arrays;
import ma02_resources.project.Project;
import ma02_resources.project.Submission;

/**
 *
 * @author David Santos
 */
public class Edition implements EditionContract {

    private static final int initialSize = 5;
    private static int idCounter = 0;
    private int id; 
    private Project projects[];

    public Edition() {
        this.id = ++idCounter;
        this.projects = new Project[initialSize];
    }

    public Edition(int size) {
        this.id = ++idCounter;
        this.projects = new Project[size];
    }

    @Override
    public void addProject(Project p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getNumberOfProjects() {
        int counter = 0;
        for (Project p : projects){
            if (p != null){
                counter++;
            }
        }
        return counter;
    }

    @Override
    public String progressToString() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void addSubmission(Submission s) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Comparison between an object and an Edition using the id
     * @param obj Object to compare
     * @return {@code true} if they have same id or {@code false} otherwise;
     */
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
        final Edition other = (Edition) obj;
        
        return this.id == other.id; //&& Arrays.deepEquals(this.projects, other.projects); //////perguntar se necessario verificar se é o mesmo array
    }

      
}
