/*
 * Nome: Carolina Bonito Queiroga De Almeida
 * Número: 8180091 
 * Turna: LSIRCT1
 *
 * Nome: David Leandro Spencer Conceição dos Santos
 * Número: 8220651
 * Turna: LSIRCT1
 */
package editions;

import Exceptions.AlreadyExistsInArray;


/**
 *
 * @author David Santos
 */
public class CBL  {

    private static final int initialSize = 5;
    private int numberOfEditions;
    private Edition editionList[];

    public CBL(int size) {
        this.editionList = new Edition[size];
        numberOfEditions = 0;
    }

    public CBL() {
        this.editionList = new Edition[initialSize];
        numberOfEditions = 0;
    }

    private void realloc() {
        Edition temp[] = new Edition[editionList.length * 2];
        for (int i = 0; i < editionList.length; i++) {
            temp[i] = editionList[i];
        }
        editionList = temp;
    }

    private boolean hasEdition(Edition edition) {
        for (Edition e : editionList) {
            if (e.equals(edition)) {
                return true;
            }
        }
        return false;
    }

  
    public void addEdition(Edition edition) throws Exception {
        if (edition == null) {
            throw new NullPointerException("Null edition!");
        }
        if (hasEdition(edition)) {
            throw new AlreadyExistsInArray("Edition already pressent in the list");
        }
        if (numberOfEditions == editionList.length) {
            realloc();
        }
        editionList[numberOfEditions++] = edition;
    }

    public int getNumberOfEditions() {
        return this.numberOfEditions;
    }

}
