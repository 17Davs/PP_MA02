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
import ma02_resources.participants.Instituition;
import ma02_resources.project.Project;
import ma02_resources.project.exceptions.InstituitionAlreadyExistException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pack.InstituitionImp;

public class InstituitionsManager {

    /**
     * @param instituitionsList 
     * @param instituitionsCounter This counter increases the number of institutions.
     */
    private static Instituition[] instituitionsList;
    private static int instituitionsCounter;

    public InstituitionsManager() {
        instituitionsCounter = 0;
        instituitionsList = new Instituition[10];
    }

    public int getInstituitionsCounter() {
        return instituitionsCounter;
    }

    private void realloc() {
        Instituition[] temp = new Instituition[instituitionsList.length * 2];
        int i = 0;
        for (Instituition p : instituitionsList) {
            temp[i++] = p;
        }
        instituitionsList = temp;
    }

    public boolean hasInstituition(Instituition p) {
        for (Instituition instituition : instituitionsList) {
            if (instituition != null && instituition.equals(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param p
     * @throws InstituitionAlreadyExistException
     */
    public void addInstituition(Instituition p) throws InstituitionAlreadyExistException {

        if (hasInstituition(p)) {
            throw new InstituitionAlreadyExistException("Instituition with same name already exists!");
        }
        if (instituitionsCounter == instituitionsList.length) {
            realloc();
        }
        instituitionsList[instituitionsCounter++] = p;
    }

    public Instituition removeInstituition(String string) {
        Instituition deleted = new InstituitionImp(string, null, null, null, null, null);
        int pos = -1, i = 0;
        while (pos == -1 && i < instituitionsCounter) {

            if (instituitionsList[i].equals(deleted)) {
                pos = i;
                deleted = instituitionsList[i];
            } else {
                i++;
            }
        }
        if (pos == -1) {
            throw new IllegalArgumentException("No Instituition found!");
        }
        for (i = pos; i < instituitionsCounter; i++) {
            instituitionsList[i] = instituitionsList[i + 1];
        }
        instituitionsList[--instituitionsCounter] = null;
        return deleted;
    }

    public Instituition getInstituition(String string) throws IllegalArgumentException {
        Instituition p = new InstituitionImp(string, null, null, null, null, null);

        for (int i = 0; i < instituitionsCounter; i++) {

            if (instituitionsList[i].equals(p)) {
                p = instituitionsList[i];
                return p;
            }
        }
        throw new IllegalArgumentException("No Instituition found!");
    }

    public Instituition[] getInstituitions() throws NullPointerException {
        if (instituitionsCounter > 0){
            Instituition temp[] = new Instituition[instituitionsCounter];

            for (int i = 0; i < instituitionsCounter; i++) {
                temp[i] = instituitionsList[i];
            }
            return temp;  
        } else {
            throw new NullPointerException("No instituitions found!");
        }
        

    }

    public boolean export(String filePath) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("instituitionsCounter", instituitionsCounter);

        JSONArray instituitionsArray = new JSONArray();
        for (int i = 0; i < instituitionsCounter; i++) {
            try {
                instituitionsArray.add(((InstituitionImp) instituitionsList[i]).toJsonObj());
            } catch (NullPointerException e) {
            }
        }
        jsonObject.put("instituitions", instituitionsArray);

        try ( FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean importData(String filePath) {
        JSONParser parser = new JSONParser();

        try ( FileReader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONArray instituitionsArray = (JSONArray) jsonObject.get("instituitions");
            for (int i = 0; i < instituitionsArray.size(); i++) {
                try {
                    JSONObject instituitionsJson = (JSONObject) instituitionsArray.get(i);
                    Instituition p = InstituitionImp.fromJsonObj(instituitionsJson);
                    this.addInstituition(p);
                } catch (InstituitionAlreadyExistException e) {

                }
            }
            return true;

        } catch (IOException | ParseException ex) {
            return false;
        }

    }
}
