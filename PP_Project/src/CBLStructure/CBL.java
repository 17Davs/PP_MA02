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

/**
 *
 * @author David Santos
 */
public interface CBL {
    public void addEdition(Edition edition) throws EditionAlreadyInCBL;
    public Edition removeEdition(String name);
    public Edition getEdition(String name);
    public void activateEdition(String name);
    public Edition[] uncompletedEditions();
    public int getNumberOfEditions();
    //public String simpleToString();
    public Edition[] getEditionsByParticipant(Participant p);
    public boolean export(String filePath);
    public boolean importData (String filePath);
    public Edition[] getEditions();
}

