/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editions;


import java.util.Arrays;
import ma02_resources.project.Project;
import ma02_resources.project.Submission;

/**
 *
 * @author David Santos
 */
public class Edition {

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

    
   
    public int getNumberOfProjects() {
        int counter = 0;
        for (Project p : projects){
            if (p != null){
                counter++;
            }
        }
        return counter;
    }


      
}
