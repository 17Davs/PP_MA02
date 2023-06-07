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

import Exceptions.EditionAlreadyInCBL;
import java.time.LocalDate;
import java.util.Arrays;
import ma02_resources.participants.Participant;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;
import ma02_resources.project.Status;

/**
 *
 * @author David Santos
 */
public class CBLImp implements CBL {

    private int numberOfEditions;
    private Edition editions[];

    /**
     * This is the Constructor of the CBL.
     *
     * @param size The inicial lenght of the editions array
     */
    public CBLImp(int size) {
        this.editions = new Edition[size];
        numberOfEditions = 0;
    }

    /**
     * This is the Constructor of the CBL.
     */
    public CBLImp() {
        this.editions = new Edition[2];
        numberOfEditions = 0;
    }

    /**
     * This method expands the lenght of the editions array by creating another
     * array with twice the lenght of the edition list, copying all the editions
     * of the editions array to the temporary array and finally make the
     * editions array the temporary array
     */
    private void realloc() {
        Edition temp[] = new Edition[editions.length * 2];
        for (int i = 0; i < editions.length; i++) {
            temp[i] = editions[i];
        }
        editions = temp;
    }

    /**
     * This method verifies if the edition given by argument exists on the CBL.
     *
     * @param edition The edition to verify
     * @return {@code true} if the edition exists on the CBL or {@code false}
     * otherwise
     */
    private boolean hasEdition(Edition edition) {
        for (Edition e : editions) {
            if (e != null && e.equals(edition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This metohd adds an edition to the CBL if it doesn't already exists.
     *
     * @param edition The edition to be added.
     * @throws Exceptions.EditionAlreadyInCBL - if the edition already exists.
     * @throw IllegalArgumentException - if the given edition's name is null or
     * empty.
     */
    @Override
    public void addEdition(Edition edition) throws EditionAlreadyInCBL {
        if (edition == null) {
            throw new IllegalArgumentException("Null edition!");
        }
        if (hasEdition(edition)) {
            throw new EditionAlreadyInCBL("Edition already in CBL");
        }
        if (numberOfEditions == editions.length) {
            realloc();
        }
        editions[numberOfEditions++] = edition;
    }

    /**
     * This method returns the number of existing editions on the CBL
     *
     * @return Returns the number of editions
     */
    @Override
    public int getNumberOfEditions() {
        return this.numberOfEditions;
    }

    /**
     * This method removes an edition from the CBL. The edition is identified by
     * its name.
     *
     * @param name The name of the edition
     * @return Returns the removed edition
     * @throws IllegalArgumentException if the given edition's name is null or
     * empty, or if the edition does not exist.
     */
    @Override
    public Edition removeEdition(String name) {
        Edition deleted = null;
        if (name == null) {
            throw new IllegalArgumentException("Null argument!");
        }
        int pos = -1, i = 0;
        while (pos == -1 && i < numberOfEditions) {

            if (editions[i].getName().equals(name)) {
                pos = i;
                deleted = editions[i];
            } else {
                i++;
            }
        }
        if (pos == -1) {
            throw new IllegalArgumentException("No edition found!");
        }
        for (i = pos; i < numberOfEditions; i++) {
            editions[i] = editions[i + 1];
        }
        editions[--numberOfEditions] = null;
        return deleted;
    }

    /**
     * This method returns an edition with the name given by argumment if it
     * exists
     *
     * @param name The name of the edition
     * @return Returns the edition with the given name
     * @throws IllegalArgumentException if the edition wasn't found
     */
    @Override
    public Edition getEdition(String name) {
        Edition e = null;
        for (int i = 0; i < numberOfEditions; i++) {
            if (editions[i].getName().equals(name)) {
                e = editions[i];
                return e;
            }
        }
        throw new IllegalArgumentException("No edition found!");
    }

    /**
     * This method changes the status of an edition to active certifying it is
     * the only active edition of the CBL
     *
     * @param name The name of the edition to be activated
     */
    @Override
    public void activateEdition(String name) throws IllegalArgumentException {
        int pos = -1, i = 0;
        
        Edition edition = new EditionImp(name, null,null);
        //try to find the active
        for (Edition e : editions) {
            if (e.getStatus() == Status.ACTIVE) {
                pos = i;
            }
            i++;
        }
        boolean complete = false;
        i = 0;
        //check and try to find edition with name, 
        //if found, activate it and inactivate previously active edition found
        while (!complete && i < numberOfEditions) {
            if (editions[i].equals(edition)) {
                if (pos != -1) {
                    //closed if end Date not happened yet and cancelled if end date not happened
                    if (editions[pos].getEnd().compareTo(LocalDate.now()) <= 0){
                        editions[pos].setStatus(Status.CLOSED);
                    } else {
                        editions[pos].setStatus(Status.CANCELED);
                    }                
                }
                editions[i].setStatus(Status.ACTIVE);

                complete = true;
            }
            i++;
        }
        if (!complete){
            throw new IllegalArgumentException("No edition found!");
        }

    }

    /**
     * This method returns all the editions that have projects with missing
     * submissions on tasks.
     *
     * @return An array of editions with uncompelted projects
     * @throws NullPointerException - if None of the editions are uncompleted
     */
    @Override
    public Edition[] uncompletedEditions() {
        int counter = 0;
        Edition[] uncompletedEditions = new Edition[numberOfEditions];
        boolean hasIncompleteProject = false;

        for (int i = 0; i < numberOfEditions; i++) {
            hasIncompleteProject = false;
            for (Project project : editions[i].getProjects()) {
                if (!project.isCompleted() && !hasIncompleteProject) {
                    hasIncompleteProject = true;
                }
            }
            //if it found at least 1 uncompleted project, adds the edition to the uncompletedEditions array
            if (hasIncompleteProject) {
                uncompletedEditions[counter++] = editions[i];
            }
        }
        if (counter == 0) {
            throw new NullPointerException("None of the editions are uncompleted!");
        }
        //limit the array to just the not null posicions
        if (counter != numberOfEditions) {
            Edition[] trimmedUncompleted = new Edition[counter];

            for (int i = 0; i < counter; i++) {
                trimmedUncompleted[i] = uncompletedEditions[i];
            }
            return trimmedUncompleted;
        }
        return uncompletedEditions;
    }

//    @Override
//    public String simpleToString() {
//        return "CBL {" + "numberOfEditions=" + numberOfEditions + ", editions=" + Arrays.toString(editions) + '}';
//    }

    @Override
    public Edition[] getEditionsByParticipant(Participant p) throws NullPointerException {
        int counter = 0;

        Edition[] temp = new Edition[numberOfEditions];
        boolean hasP = false;
        for (int i = 0; i < numberOfEditions; i++) {
            hasP = false;
            for (Project project : editions[i].getProjects()) {
                if (!hasP) {
                    try {
                        Participant participant = project.getParticipant(p.getEmail());
                        if (participant != null) {
                            hasP = true;
                        }

                    } catch (IllegalArgumentException e) {
                    }
                }
            }
            if (hasP) {
                temp[counter++] = editions[i];
            }
        }
        if (counter == 0) {
            throw new NullPointerException("User does not participate in any of the editions");
        }
        //limit the array to just the not null posicions
        if (counter != numberOfEditions) {
            Edition[] editionsByParticipant = new Edition[counter];

            for (int i = 0; i < counter; i++) {
                editionsByParticipant[i] = temp[i];
            }
            return editionsByParticipant;
        }
        return temp;
    }
}
