/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editions;

import Exceptions.AlreadyExistsInArray;
import editions.Contracts.Manage;

/**
 *
 * @author David Santos
 */
public class CBL implements Manage {

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

    @Override
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

    @Override
    public Edition removeEdition(int index) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Edition getEdition(int index) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setActiveEdition(int index) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Edition[] getIncompletedEditions() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Project[] getIncompletedProjects() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getNumberOfEditions() {
        return this.numberOfEditions;
    }

}
