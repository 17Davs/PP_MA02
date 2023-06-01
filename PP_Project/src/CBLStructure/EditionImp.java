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



import ma02_resources.project.Project;


/**
 *
 * @author David Santos
 */
public class EditionImp {

    private static final int initialSize = 5;
    private static int idCounter = 0;
    private int id; 
    private Project projects[];

    public EditionImp() {
        this.id = ++idCounter;
        this.projects = new Project[initialSize];
    }

    public EditionImp(int size) {
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
