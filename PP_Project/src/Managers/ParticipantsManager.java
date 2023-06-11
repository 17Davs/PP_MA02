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

import Exceptions.AlreadyExistsInArray;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import ma02_resources.participants.Facilitator;

import ma02_resources.participants.Participant;
import ma02_resources.participants.Partner;
import ma02_resources.participants.Student;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pack.ParticipantImp;

public class ParticipantsManager {

    /**
     * Array that stores Participants.
     */
    private static Participant[] participantsList;
    /**
     * This counter variable increases the number of participants.
     */
    private static int participantsCounter;

    
    /**
     * This is the constructor method for ParticipantsManager.
     */
    public ParticipantsManager() {
        participantsCounter = 0;
        participantsList = new Participant[10];
    }

    /**
     * This method is a getter for Participants Manager.
     * @return How many Participants exist.
     */
    public int getParticipantsCounter() {
        return participantsCounter;
    }

    /**
     * This method adds space to the list if it's full.
     * 
     */
    private void realloc() {
        Participant[] temp = new Participant[participantsList.length * 2];
        int i = 0;
        for (Participant p : participantsList) {
            temp[i++] = p;
        }
        participantsList = temp;
    }

    /**
     * This method checks if Participant exists in array.
     * 
     * @param p Participant to be checked.
     * @return true if exists.
     * @return false if doensn't exit.
     */
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
    
    /**
     * This method adds a Participant to the list. It checks if Participant to be added
     * already exists and if the array is full.
     * 
     * @param p Participant to be added.
     * @throws AlreadyExistsInArray
     */
    public void addParticipant(Participant p) throws AlreadyExistsInArray {

        if (hasParticipant(p)) {
            throw new AlreadyExistsInArray("Participant with same email already exists!");
        }
        if (participantsCounter == participantsList.length) {
            realloc();
        }
        participantsList[participantsCounter++] = p;
    }

    /**
     * This method removes a Participant from array.
     * 
     * @param string Name.
     * @return The deleted Participant.
     */
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

    /**
     * This method gets data of one Participant.
     * 
     * @param string Name to be searched.
     * @return The Participant.
     * @throws IllegalArgumentException - if not found.
     */
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

//    public Participant[] getParticipants() {
//        int counter = 0;
//        Participant temp[] = new Participant[participantsCounter];
//
//        for (int i = 0; i < participantsCounter; i++) {
//            temp[counter++] = participantsList[i];
//        }
//        if (counter == participantsCounter) {
//            return temp;
//        }
//
//        Participant trimmedTemp[] = new Participant[counter];
//        for (int i = 0; i < counter; i++) {
//            trimmedTemp[i] = temp[i];
//        }
//        return trimmedTemp;
//    }
    public int getNumberOfFacilitators() {
        int numberOfFacilitators = 0;
        for (int i = 0; i < participantsCounter; i++) {
            if (participantsList[i] instanceof Facilitator) {
                numberOfFacilitators++;
            }
        }
        return numberOfFacilitators;
    }

    public int getNumberOfStudents() {
        int numberOfStudents = 0;
        for (int i = 0; i < participantsCounter; i++) {
            if (participantsList[i] instanceof Student) {
                numberOfStudents++;
            }
        }
        return numberOfStudents;
    }

    public int getNumberOfPartners() {
        int numberOfPartners = 0;
        for (int i = 0; i < participantsCounter; i++) {
            if (participantsList[i] instanceof Partner) {
                numberOfPartners++;
            }
        }
        return numberOfPartners;
    }

    /**
     * This method gets data of all Participants, ordered by Facilitatiors, Students
     * and Partners.
     * 
     * @return The list with data.
     */
    public Participant[] getParticipants() {
        int counter = 0;

        Participant[] temp = new Participant[participantsCounter];

        for (int i = 0; i < participantsCounter; i++) {
            if (participantsList != null) {
                temp[counter++] = participantsList[i];
            }
        }

        Participant trimmedTemp[] = new Participant[counter];
        int j = 0;
        counter = 0;

        while (j < getNumberOfFacilitators() && counter < trimmedTemp.length) {
            for (int i = 0; i < trimmedTemp.length; i++) {
                if (temp[i] instanceof Facilitator) {
                    trimmedTemp[counter++] = temp[i];
                    j++;
                }
            }
        }

        j = 0;
        while (j < getNumberOfStudents() && counter < trimmedTemp.length) {
            for (int i = 0; i < trimmedTemp.length; i++) {
                if (temp[i] instanceof Student) {
                    trimmedTemp[counter++] = temp[i];
                    j++;
                }
            }
        }

        j = 0;
        while (j < getNumberOfPartners() && counter < trimmedTemp.length) {
            for (int i = 0; i < trimmedTemp.length; i++) {
                if (temp[i] instanceof Partner) {
                    trimmedTemp[counter++] = temp[i];
                    j++;
                }
            }
        }

        return trimmedTemp;
    }

    /**
     * This method returns All Students.
     * 
     * @return A list with all institutions.
     * @throws NullPointerException - if not found.
     */
    public Student[] getStudents() {
        int counter = 0;

        if (getNumberOfStudents() > 0) {
            Student[] temp = new Student[getNumberOfStudents()];
            for (int i = 0; i < participantsCounter; i++) {
                if (participantsList[i] instanceof Student) {
                    temp[counter++] = (Student) participantsList[i];
                }
            }
            return temp;
        }
        return null;
    }

    /**
     * This method exports data to a JSON file.
     * 
     * @param filePath The path where the end file is located.
     * @return true if successful.
     */
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
            fileWriter.close();
        } catch (IOException e) {

            return false;
        }
        return true;
    }

    /**
     * This method imports data from JSON file.
     * 
     * @param filePath The start path where the JSON file is located.
     * @return true if successful.
     */
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
