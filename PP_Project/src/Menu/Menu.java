/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menu;

import CBLStructure.CBL;
import CBLStructure.CBLImp;
import Exceptions.AlreadyExistsInArray;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ma02_resources.participants.Contact;
import ma02_resources.participants.Instituition;
import ma02_resources.participants.InstituitionType;
import ma02_resources.participants.Participant;
import ma02_resources.participants.Student;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;
import ma02_resources.project.Task;
import pack.ContactImp;
import pack.InstituitionImp;
import pack.ParticipantImp;
import pack.ParticipantsManager;
import pack.StudentImp;

/**
 *
 * @author David Santos
 */
public class Menu {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private CBL cbl;
    private ParticipantsManager pm;
    private Participant loggedInParticipant;
    private Scanner scanner;

    public Menu(CBL cbl, ParticipantsManager pm) {
        this.cbl = cbl;
        this.pm = pm;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option = 0;
        do {
            System.out.println("=== Menu ===");
            System.out.println("1. Log in");
            System.out.println("2. Register");
            System.out.println("3. Log in as Administrator");
            System.out.println("4. Exit");
            System.out.print("Option: ");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    if (login()) {
                        showEditionsMenu();
                    }
                    break;
                case 2:
                    //register();
                    break;

                case 3:
                    if (loginAdmin()) {
                        // showAdminMenu();
                    }
                case 4:
                    break;
                default:
                    System.out.println("Invalid option!");
                    start();
            }

        } while (option != 4);

    }
//fazer com do while
    private boolean login() {
        int counter = 0;
        System.out.println("===== Login =====");
        System.out.print("Email: ");
        scanner.nextLine();
        String email = scanner.nextLine();
        try {
            loggedInParticipant = pm.getParticipant(email);
            System.out.println("Login successful. Welcome, " + loggedInParticipant.getName() + "!");
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("User not found");
            if (++counter < 3) {
                login();
            } 
            return false;
        }
    }

    private boolean loginAdmin() {
        int counter = 0;

        System.out.println("===== Login =====");
        System.out.print("Email: ");
        scanner.nextLine();
        String email = scanner.nextLine();
        if (email.equals(USERNAME)) {
            do {
                System.out.print("Password: ");
                String password = scanner.nextLine();
                if (!password.equals(PASSWORD)) {
                    System.out.println("Login Failed!");
                } else {
                return true;
                }
            } while (++counter < 3);
                
        } else {
            System.out.println("Login Failed!");
                return false;
        }
        return false;
    }

    private void showEditionsMenu() {
        System.out.println("===== Editions Menu =====");
        System.out.println("Select an edition:");
        try {

            Edition[] editions = cbl.getEditionsByParticipant(loggedInParticipant);

            for (int i = 0; i < editions.length; i++) {
                System.out.println((i + 1) + ". " + editions[i].getName());
            }

            System.out.print("Enter the number of the edition: ");
            int editionNumber = scanner.nextInt();
            scanner.nextLine();

            // Verifique se o número da edição é válido
            if (editionNumber >= 1 && editionNumber <= editions.length) {
                Edition selectedEdition = editions[editionNumber - 1];
                showProjectsMenu(selectedEdition);
            } else {
                System.out.println("Invalid selection. Please try again.");
                showEditionsMenu();
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());

        }

    }

    private void showProjectsMenu(Edition edition) {
        System.out.println("===== Projects Menu =====");
        System.out.println("Edition: " + edition.getName());
        System.out.println("Select a project:");

        Project[] projects = edition.getProjects();
        for (int i = 0; i < projects.length; i++) {
            System.out.println((i + 1) + ". " + projects[i].getName());
        }

        System.out.print("Enter the number of the project: ");
        int projectNumber = scanner.nextInt();
        scanner.nextLine(); // Limpar a nova linha pendente

        // Verifique se o número do projeto é válido
        if (projectNumber >= 1 && projectNumber <= projects.length) {
            Project selectedProject = projects[projectNumber - 1];
            showProjectDetails(selectedProject);
        } else {
            System.out.println("Invalid selection. Please try again.");
            showProjectsMenu(edition);
        }
    }

    private void showProjectDetails(Project project) {
        System.out.println("===== Project Details =====");
        System.out.println("Project: " + project.getName());
        System.out.println("Description: " + project.getDescription());
        System.out.println("Tags: ");
        String[] tags = project.getTags();
        for (String s : tags) {
            System.out.print(s + " ");
        }
        System.out.println("Status: " + (project.isCompleted() ? "Completed" : "Incomplete"));
        System.out.println("Participants: " + project.getNumberOfParticipants() + "/" + project.getMaximumNumberOfParticipants());
        System.out.println(" -- Facilitators: " + project.getNumberOfFacilitators() + "/" + project.getMaximumNumberOfFacilitators());
        System.out.println(" -- Students: " + project.getNumberOfStudents() + "/" + project.getMaximumNumberOfStudents());
        System.out.println(" -- Partners: " + project.getNumberOfPartners() + "/" + project.getMaximumNumberOfPartners());
        System.out.println(" Tasks: " + project.getNumberOfTasks() + "/" + project.getMaximumNumberOfTasks());
        Task[] tasks = project.getTasks();
        for (int i = 0; i < tasks.length; i++) {
            System.out.println((i + 1) + ". " + tasks[i].getTitle());
        }

        System.out.print("Enter the number of the task: ");
        int taskNumber = scanner.nextInt();
        scanner.nextLine(); // Limpar a nova linha pendente

        // Verifique se o número do projeto é válido
        if (taskNumber >= 1 && taskNumber <= tasks.length) {
            Task selectedTask = tasks[taskNumber - 1];
            // showTaskDetails(selectedTask);

        }
    }

    public static void main(String[] args) {
        // Crie uma instância da classe CBL e adicione participantes, edições, projetos, etc.
        ParticipantsManager pm = new ParticipantsManager();
        CBL cbl = new CBLImp();

        Contact c = new ContactImp("Street", "city", "state", "zipCode", "country", "phone");
        Instituition estg = new InstituitionImp("ESTG", "estg@estg.ipp.pt", "estg.ipp.pt", "Escola de Tecnologia e Gestão de Felgueiras", c, InstituitionType.UNIVERSITY);
        Student p1 = new StudentImp(8220651, "David Santos", "david@estg.ipp.pt", c, estg);
        try {
            pm.addParticipant(p1);
        } catch (AlreadyExistsInArray ex) {
            System.out.println(ex.toString());
        }
        // Crie uma instância da classe Menu e inicie o menu
        Menu menu = new Menu(cbl, pm);
        menu.start();
    }
}
