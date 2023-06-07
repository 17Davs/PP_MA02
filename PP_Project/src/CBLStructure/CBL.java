/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
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
}

