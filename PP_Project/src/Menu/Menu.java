/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menu;

import CBLStructure.CBL;
import CBLStructure.CBLImp;
import Exceptions.AlreadyExistsInArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import ma02_resources.participants.Contact;
import ma02_resources.participants.Instituition;
import ma02_resources.participants.InstituitionType;
import ma02_resources.participants.Participant;
import ma02_resources.participants.Student;
import ma02_resources.participants.Facilitator;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;
import ma02_resources.project.Task;
import pack.ContactImp;
import pack.InstituitionImp;
import pack.ParticipantImp;
import pack.ParticipantsManager;
import pack.StudentImp;
import pack.FacilitatorImp;
import pack.PartnerImp;
import ma02_resources.participants.Partner;

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
    private BufferedReader reader;

    public Menu(CBL cbl, ParticipantsManager pm) {
        this.cbl = cbl;
        this.pm = pm;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
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
            try {
                option = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                System.out.println("Error reading input.");
                continue;
            }
            switch (option) {
                case 1:
                    if (login()) {
                        showEditionsMenu();
                    }
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    if (loginAdmin()) {
                        // showAdminMenu();
                    }
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid option!");
                    start();
            }

        } while (option != 4);
    }

    private boolean login() {
        int counter = 0;
        System.out.println("===== Login =====");
        do {
            System.out.print("Email: ");
            try {
                String email = reader.readLine();
                loggedInParticipant = pm.getParticipant(email);
                System.out.println("Login successful. Welcome, " + loggedInParticipant.getName() + "!");
                return true;
            } catch (IOException e) {
                System.out.println("Error reading input.");
            } catch (IllegalArgumentException e) {
                System.out.println("User not found\n");
            }
        } while (++counter < 3);

        return false;
    }

    private boolean loginAdmin() {
        int counter = 0;

        System.out.println("===== Login =====");
        System.out.print("Email: ");
        try {
            String email = reader.readLine();
            if (email.equals(USERNAME)) {
                do {
                    System.out.print("Password: ");
                    String password = reader.readLine();
                    if (!password.equals(PASSWORD)) {
                        System.out.println("Login Failed!\n");
                    } else {
                        return true;
                    }
                } while (++counter < 3);
            } else {
                System.out.println("Login Failed!");
                return false;
            }
        } catch (IOException e) {
            System.out.println("Error reading input.");
        }
        return false;
    }

    private boolean register() {
        int option = 0;
        do {
            System.out.println("=== Register ===");
            System.out.println("1. As a Student");
            System.out.println("2. As a Facilitator");
            System.out.println("3. As a Partner");
            System.out.println("4. Back");
            try {
                option = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                System.out.println("Error reading input.");
                continue;
            }
        } while (option > 4 || option < 1);

        if (option == 4) {
            return false;
        }

        try {
            System.out.println("Name: ");
            String name = reader.readLine();
            System.out.println("Email: ");
            String email = reader.readLine();
            System.out.println("Street: ");
            String street = reader.readLine();
            System.out.println("City: ");
            String city = reader.readLine();
            System.out.println("State: ");
            String state = reader.readLine();
            System.out.println("ZipCode: ");
            String zipCode = reader.readLine();
            System.out.println("Country: ");
            String country = reader.readLine();
            System.out.println("Phone: ");
            String phone = reader.readLine();

            Contact contact = new ContactImp(street, city, state, zipCode, country, phone);

            //apresentar instituições do manager de instituições ainda não criado
            Instituition instituition = null;
            Participant newParticipant = new ParticipantImp(name, email, contact, instituition);

            switch (option) {
                case 1:
                    return registerStudent(newParticipant);
                case 2:
                    // Handle registering as a facilitator
                    return registerFacilitator(newParticipant);
                case 3:
                    // Handle registering as a partner
                    return registerPartner(newParticipant);
            }
        } catch (IOException e) {
            System.out.println("Error reading input.");
            
        }
        return false;
    }

    private boolean registerStudent(Participant participant) {
        try {
            //number problem -> can be same number if done like this
            //get missing variables
            System.out.println("Number: ");
            int number = Integer.parseInt(reader.readLine());
            //create student
            Student newStudent = new StudentImp(number, participant.getName(),
                    participant.getEmail(), participant.getContact(),
                    participant.getInstituition());
            //add to participant Manager array
            pm.addParticipant(newStudent);

            return true;

        } catch (IOException ex) {
            System.out.println("Error reading input.");
            return false;
        } catch (AlreadyExistsInArray ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private boolean registerFacilitator(Participant participant) {
        try {

            //get missing variables
            System.out.println("Area Of Expertise: ");
            String areaOfExpertise = reader.readLine();

            //create facilitator
            Facilitator newFacilitator = new FacilitatorImp(areaOfExpertise,
                    participant.getName(), participant.getEmail(),
                    participant.getContact(), participant.getInstituition());

            //add newFacilitator to participant manager
            pm.addParticipant(newFacilitator);

            return true;
        } catch (IOException ex) {
            System.out.println("Error reading input.");
            return false;
        } catch (AlreadyExistsInArray ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private boolean registerPartner(Participant participant) {
        try {

            //get missing variables
            System.out.println("Vat: ");
            String vat = reader.readLine();
            System.out.println("WebSite: ");
            String website = reader.readLine();

            //create partner
            Partner newPartner = new PartnerImp(vat, website,
                    participant.getName(), participant.getEmail(),
                    participant.getContact(), participant.getInstituition());

            //add newPartner to participant manager
            pm.addParticipant(newPartner);

            return true;

        } catch (IOException ex) {
            System.out.println("Error reading input.");
            return false;
        } catch (AlreadyExistsInArray ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private void showEditionsMenu() {
        System.out.println("===== Editions Menu =====");
        System.out.println("Select an edition:");
        try {
            Edition[] editions = cbl.getEditionsByParticipant(loggedInParticipant);
            System.out.println("== Your Editions == ");
            for (int i = 0; i < editions.length; i++) {
                System.out.println((i + 1) + ". " + editions[i].getName());
            }

            System.out.print("Enter the number of the edition: ");
            try {
                int editionNumber = Integer.parseInt(reader.readLine());

                // Verifique se o número da edição é válido
                if (editionNumber >= 1 && editionNumber <= editions.length) {
                    Edition selectedEdition = editions[editionNumber - 1];
                    showProjectsMenu(selectedEdition);
                } else {
                    System.out.println("Invalid selection. Please try again.");
                    showEditionsMenu();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                showEditionsMenu();
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading input.");
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
        try {
            int projectNumber = Integer.parseInt(reader.readLine());

            // Verifique se o número do projeto é válido
            if (projectNumber >= 1 && projectNumber <= projects.length) {
                Project selectedProject = projects[projectNumber - 1];
                showProjectDetails(selectedProject);
            } else {
                System.out.println("Invalid selection. Please try again.");
                showProjectsMenu(edition);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            showProjectsMenu(edition);
        } catch (IOException e) {
            System.out.println("Error reading input.");
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
        try {
            int taskNumber = Integer.parseInt(reader.readLine());

            // Verifique se o número da tarefa é válido
            if (taskNumber >= 1 && taskNumber <= tasks.length) {
                Task selectedTask = tasks[taskNumber - 1];
                // showTaskDetails(selectedTask);
            } else {
                System.out.println("Invalid selection. Please try again.");
                showProjectDetails(project);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            showProjectDetails(project);
        } catch (IOException e) {
            System.out.println("Error reading input.");
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
