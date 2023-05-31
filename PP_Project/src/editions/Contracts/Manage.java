/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package editions.Contracts;



/**
 *
 * @author David Santos
 */
public interface Manage {
    public void addEdition(Edition edition) throws Exception;
    public Edition removeEdition(int index) throws Exception;
    public Edition getEdition(int index) throws Exception;
    public void setActiveEdition(int index) throws Exception;
    public Edition[] getIncompletedEditions() throws Exception;
    public Project[] getIncompletedProjects() throws Exception;
    public int getNumberOfEditions();
}
