/*
 * Nome: Carolina Bonito Queiroga De Almeida
 * Número: 8180091 
 * Turna: LSIRCT1
 *
 * Nome: David Leandro Spencer Conceição dos Santos
 * Número: 8220651
 * Turna: LSIRCT1
 */


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 *
 * @author David Santos
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    // TODO code application logic here

    try {
        Reader reader = new FileReader("src/Files/project_template.json");

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(reader);

        // falta criar objeto
        long number_of_facilitors = (long) object.get("number_of_facilitors");
        int number_of_students = (int) object.get("number_of_students");
        int number_of_partners = (int) object.get("number_of_partners");

        // para arrays criar array e depois percorrer e ler 
        JSONArray taskArray = (JSONArray) object.get("tasks");

        for (int i = 0; i < taskArray.size(); i++) {
            JSONObject aTask = (JSONObject) taskArray.get(i);
            String title = (String) aTask.get("title");
            String description = (String) aTask.get("description");
            int start_at = (int) aTask.get("start_at");
            int duration = (int) aTask.get("duration");
            // addicionar no objeto
        }
    } catch (FileNotFoundException ex) {
        System.out.println("File not found!");
    } catch (IOException ex) {
        System.out.println("IO Exception");
    } catch (ParseException ex) {
        System.out.println(ex.toString());
    } catch (Exception ignored) {}
    
    
    }
}