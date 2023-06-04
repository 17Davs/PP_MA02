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
import ma02_resources.project.Edition;
import ma02_resources.project.Status;

/**
 *
 * @author David Santos
 */
public class CBLImp implements CBL {

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

    @Override
    public int getNumberOfEditions() {
        return this.numberOfEditions;
    }

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

    @Override
    public Edition getEdition(String name) {
        Edition e = null;
        for (int i = 0; i < numberOfEditions; i++) {
            if (editions[i].getName().equals(name)) {
                e = editions[i];
                return e;
            }
        }

        throw new IllegalArgumentException("No project found!");

    }

    @Override
    public void activateEdition(String name) {
        int pos = -1, i = 0;
        //find the active
        for (Edition e : editions) {
            if (e.getStatus() == Status.ACTIVE) {
                pos = i;
            }
            i++;
        }
        boolean complete = false;
        i = 0;
        //check and try to find edition with name, if found activate it and inactivate previously edition active found
        while (!complete && i < numberOfEditions) {
            if (editions[i].getName().equals(name)) {
                if (pos != -1) {
                    editions[pos].setStatus(Status.INACTIVE);
                }
                editions[i].setStatus(Status.ACTIVE);

                complete = true;
            }
            i++;
        }

    }

    @Override
    public Edition[] uncompletedEditions() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
