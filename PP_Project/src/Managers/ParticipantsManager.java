/*
 * Nome: Carolina Bonito Queiroga De Almeida
 * Número: 8180091 
 * Turna: LSIRCT1
 *
 * Nome: David Leandro Spencer Conceição dos Santos
 * Número: 8220651
 * Turna: LSIRCT1
 */
package Managers;

import CBLStructure.EditionImp;
import Exceptions.AlreadyExistsInArray;
import Exceptions.EditionAlreadyInCBL;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import ma02_resources.participants.Instituition;
import ma02_resources.participants.Participant;
import ma02_resources.project.exceptions.IllegalNumberOfParticipantType;
import ma02_resources.project.exceptions.ParticipantAlreadyInProject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pack.ParticipantImp;

/**
 *
 * @author David Santos
 */
public class ParticipantsManager {

    private static Participant[] participantsList;
    private static int participantsCounter;

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
        Participant deleted = new ParticipantImp(null, string, null, null);;
        int pos = -1, i = 0;
        while (pos == -1 && i < participantsCounter) {

            if (participantsList[i].equals(deleted)) {
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
        Participant p = new ParticipantImp(null, string, null, null);

        for (int i = 0; i < participantsCounter; i++) {

            if (participantsList[i].equals(p)) {
                p = participantsList[i];
                return p;
            }
        }
            throw new IllegalArgumentException("No Participant found!");
              
    }
    
    
      public Participant[] getParticipants() {
        int counter=0;
        Participant temp[] = new Participant[participantsCounter];

        for (int i = 0; i < participantsCounter; i++) {
            temp[counter++] = participantsList[i];
        }
        if (counter == participantsCounter){
            return temp;
        }
        
        Participant trimmedTemp[] = new Participant[counter];
        for (int i = 0; i < counter; i++){
            trimmedTemp[i] = temp[i];
        }
        return trimmedTemp;
    }

    public boolean export(String filePath) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("participantsCounter", participantsCounter);

        JSONArray participantsArray = new JSONArray();
        for (int i = 0; i < participantsCounter; i++) {
            try {
                participantsArray.add(((ParticipantImp) participantsList[i]).toJsonObj());
            } catch (NullPointerException e) {
            }
        }
        jsonObject.put("participants", participantsArray);

        try ( FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonObject.toJSONString());
            //System.out.println("Exported to JSON file: " + filePath);
        } catch (IOException e) {

            return false;
        }
        return true;
    }

    public boolean importData(String filePath) {
        JSONParser parser = new JSONParser();

        try ( FileReader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONArray participantsArray = (JSONArray) jsonObject.get("participants");
            for (int i = 0; i < participantsArray.size(); i++) {
                try {
                    JSONObject participantJson = (JSONObject) participantsArray.get(i);
                    Participant p = ParticipantImp.fromJsonObj(participantJson);
                    this.addParticipant(p);
                } catch (AlreadyExistsInArray e) {

                }
            }
            return true;

        } catch (FileNotFoundException ex) {
            System.out.println("File");
        } catch (IOException ex) {
            System.out.println("2");
        } catch (ParseException ex) {
            System.out.println("3");
        }
        return false;

    }
}
