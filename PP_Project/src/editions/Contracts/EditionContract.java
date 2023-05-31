/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package editions.Contracts;

import editions.Project;
import editions.Submission;

/**
 *
 * @author David Santos
 */
public interface EditionContract {
    public void addProject(Project p) throws Exception;
    public int getNumberOfProjects() ;
    public String progressToString();
    public void addSubmission(Submission s) throws Exception;
}
