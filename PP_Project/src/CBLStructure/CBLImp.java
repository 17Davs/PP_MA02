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

import Exceptions.AlreadyExistsInArray;
import Exceptions.EditionAlreadyInCBL;
import ma02_resources.project.Edition;


/**
 *
 * @author David Santos
 */
public class CBLImp implements CBL  {

    private int numberOfEditions;
    private Edition editions[];

    public CBLImp(int size) {
        this.editions = new Edition[size];
        numberOfEditions = 0;
    }

    public CBLImp() {
        this.editions = new Edition[2];
        numberOfEditions = 0;
    }

    private void realloc() {
        Edition temp[] = new Edition[editions.length * 2];
        for (int i = 0; i < editions.length; i++) {
            temp[i] = editions[i];
        }
        editions = temp;
    }

    private boolean hasEdition(Edition edition) {
        for (Edition e : editions) {
            if (e.equals(edition)) {
                return true;
            }
        }
        return false;
    }

  
    public void addEdition(Edition edition) throws EditionAlreadyInCBL{
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

    public int getNumberOfEditions() {
        return this.numberOfEditions;
    }

    @Override
    public Edition removeEdition(String name) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Edition getEdition(String name) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void activateEdition(String name) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Edition[] uncompletedEditions() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
