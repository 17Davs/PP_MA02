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
import ma02_resources.participants.Participant;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;

public interface CBL {

    /**
     * This method adds an edition to the CBL if it doesn't already exist.
     *
     * @param edition The edition to be added.
     * @throws Exceptions.EditionAlreadyInCBL if the edition already exists.
     * @throws IllegalArgumentException if the given edition's name is null or
     * empty.
     */
    public void addEdition(Edition edition) throws EditionAlreadyInCBL;

    /**
     * This method removes an edition from the CBL. The edition is identified by
     * its name.
     *
     * @param name The name of the edition
     * @return Returns the removed edition
     * @throws IllegalArgumentException if the given edition's name is null or
     * empty, or if the edition does not exist.
     */
    public Edition removeEdition(String name);

    /**
     * This method returns an edition with the name given by argumment if it
     * exists
     *
     * @param name The name of the edition
     * @return Returns the edition with the given name
     * @throws IllegalArgumentException if the edition wasn't found
     */
    public Edition getEdition(String name);

    /**
     * This method changes the status of an edition to active certifying it is
     * the only active edition of the CBL
     *
     * @param name The name of the edition to be activated
     */
    public void activateEdition(String name);

    /**
     * This method returns all the editions that have projects with missing
     * submissions on tasks.
     *
     * @return An array of editions with incomplete projects
     * @throws NullPointerException - if None of the editions are incomplete
     */
    public Edition[] uncompletedEditions();

    /**
     * This method returns the number of existing editions on the CBL
     *
     * @return Returns the number of editions
     */
    public int getNumberOfEditions();

    /**
     * This method returns a list of editions in which the participant is part
     * of.
     *
     * @param p Participant to be searched
     * @return An array of editions by participant
     * @throws NullPointerException -If it doesn't find any editions
     */
    public Edition[] getEditionsByParticipant(Participant p);

    /**
     * This method exports the data from the CBL to a JSON file at the specified
     * file path. The file contains every data aboud the editions, its taks,
     * participants, submissions...
     *
     * @param filePath The path to the JSON file.
     * @return true if the export is successful, false otherwise.
     */
    public boolean export(String filePath);

    /**
     * Imports data from a JSON file located at the specified file path.
     *
     * It reads the JSON file, parses its contents, and adds the editions to 
     * the current object.
     * 
     * @param filePath The path to the JSON file.
     * @return true if the import is successful, false otherwise.
     */
    public boolean importData(String filePath);

    /**
     * This method returns an array of Edition objects representing the
     * available editions.
     *
     * @return An array of Edition objects.
     */
    public Edition[] getEditions();

    /**
     * Retrieves an array of projects in which the participant is involved.
     *
     * @param participant The participant for whom to retrieve the projects.
     * @return An array of projects in which the participant is involved.
     */
    public Project[] getProjectsOf(Participant participant);
}
