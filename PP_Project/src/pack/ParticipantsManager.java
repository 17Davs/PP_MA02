/*
 * Nome: Carolina Bonito Queiroga De Almeida
 * Número: 8180091 
 * Turna: LSIRCT1
 *
 * Nome: David Leandro Spencer Conceição dos Santos
 * Número: 8220651
 * Turna: LSIRCT1
 */
package pack;

import Exceptions.AlreadyExistsInArray;
import ma02_resources.participants.Participant;

/**
 *
 * @author David Santos
 */
public class ParticipantsManager {

    private Participant[] participantsList;
    private int participantsCounter;

    public ParticipantsManager() {
        participantsCounter = 0;
        participantsList = new Participant[10];
    }

    public int getParticipantsCounter() {
        return participantsCounter;
    }

    private void realloc() {
        Participant[] temp = new Participant[participantsList.length * 2];
        int i = 0;
        for (Participant p : participantsList) {
            temp[i++] = p;
        }
        participantsList = temp;
    }

    public boolean hasParticipant(Participant p) {
        for (Participant participant : participantsList) {
            if (participant != null && participant.equals(p)) {
                return true;
            }
        }
        return false;
    }
    
//    public int findParticipant(Participant p) {
//        int i=0;
//        for (Participant participant : participantsList) {
//            if (participant.equals(p)) {
//                return i;
//            }
//            i++;
//        }
//        return -1;
//    }

    public void addParticipant(Participant p) throws AlreadyExistsInArray {

        if (hasParticipant(p)) {
            throw new AlreadyExistsInArray("Participant with same email already exists!");
        }
        if (participantsCounter == participantsList.length) {
            realloc();
        }
        participantsList[participantsCounter++] = p;
    }

    public Participant removeParticipant(String string) {
        Participant deleted = null;
        int pos = -1, i = 0;
        while (pos == -1 && i < participantsCounter) {

            if (participantsList[i].getEmail().equals(string)) {
                pos = i;
                deleted = participantsList[i];
            } else {
                i++;
            }
        }
        if (pos == -1) {
            throw new IllegalArgumentException("No Participant found!");
        }
        for (i = pos; i < participantsCounter; i++) {
            participantsList[i] = participantsList[i + 1];
        }
        participantsList[--participantsCounter] = null;
        return deleted;
    }
   
    public Participant getParticipant(String string) throws IllegalArgumentException {
        Participant p = null;

        for (int i = 0; i < participantsCounter; i++) {

            if (participantsList[i].getEmail().equals(string)) {
                p = participantsList[i];
                return p;
            }
        }
        if (p == null) {
            throw new IllegalArgumentException("No Participant found!");
        }
        return p;
    }
}
