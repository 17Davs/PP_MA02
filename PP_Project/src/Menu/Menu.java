/*
 * Nome: Carolina Bonito Queiroga De Almeida
 * Número: 8180091 
 * Turna: LSIRCT1
 *
 * Nome: David Leandro Spencer Conceição dos Santos
 * Número: 8220651
 * Turna: LSIRCT1
 */
package Menu;

import CBLStructure.CBL;
import CBLStructure.CBLImp;
import CBLStructure.EditionImp;
import CBLStructure.ProjectImp;
import CBLStructure.SubmissionImp;
import CBLStructure.TaskImp;
import Exceptions.AlreadyExistsInArray;
import Exceptions.EditionAlreadyInCBL;
import Managers.InstituitionsManager;
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
import Managers.ParticipantsManager;
import java.io.FileWriter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pack.StudentImp;
import pack.FacilitatorImp;
import pack.PartnerImp;
import ma02_resources.participants.Partner;
import ma02_resources.project.Status;
import ma02_resources.project.Submission;
import ma02_resources.project.exceptions.IllegalNumberOfParticipantType;
import ma02_resources.project.exceptions.IllegalNumberOfTasks;
import ma02_resources.project.exceptions.InstituitionAlreadyExistException;
import ma02_resources.project.exceptions.ParticipantAlreadyInProject;
import ma02_resources.project.exceptions.TaskAlreadyInProject;
import org.json.simple.JSONObject;

public class Menu {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private CBL cbl;
    private ParticipantsManager pm;
    private InstituitionsManager im;
    private Participant loggedInParticipant;
    private Edition currentEdition;
    private Project currentProject;
    private BufferedReader reader;

    public Menu(CBL cbl, ParticipantsManager pm, InstituitionsManager im) {
        this.cbl = cbl;
        this.im = im;
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

                switch (option) {
                    case 1:
                        if (login()) {
                            showEditionsMenu();
                        }
                        break;
                    case 2:
                        if (register()) {
                            System.out.println("Registration Success. Login to continue!");
                        }
                        break;
                    case 3:
                        if (loginAdmin()) {
                            showAdminMenu();
                        }
                        break;
                    case 4:
                        break;
                    default:
                        System.out.println("Invalid option!");
                        start();
                }
            } catch (IOException e) {
                System.out.println("Error reading input.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");

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
                System.out.println("Login successful. Welcome, " + loggedInParticipant.getName() + "!\n\n");
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
                        System.out.println("Login Failed!\n\n");
                    } else {
                        //
                        loggedInParticipant = null;
                        return true;
                    }
                } while (++counter < 3);
            } else {
                System.out.println("Login Failed!\n\n");
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
            }
        } while (option > 4 || option < 1);

        if (option == 4) {
            return false;
        }

        try {
            System.out.print("\nName: ");
            String name = reader.readLine();
            System.out.print("\nEmail: ");
            String email = reader.readLine();
            System.out.print("\nStreet: ");
            String street = reader.readLine();
            System.out.print("\nCity: ");
            String city = reader.readLine();
            System.out.print("\nState: ");
            String state = reader.readLine();
            System.out.print("\nZipCode: ");
            String zipCode = reader.readLine();
            System.out.print("\nCountry: ");
            String country = reader.readLine();
            System.out.print("\nPhone: ");
            String phone = reader.readLine();

            Contact contact = new ContactImp(street, city, state, zipCode, country, phone);

            //apresentar instituições do manager de instituições ainda não criado
            Participant newParticipant = new ParticipantImp(name, email, contact, null);

            assignInstituition(newParticipant);
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
            //create student
            Student newStudent = new StudentImp(participant.getName(),
                    participant.getEmail(), participant.getContact(),
                    participant.getInstituition());
            //add to participant Manager array
            pm.addParticipant(newStudent);

            return true;
        } catch (AlreadyExistsInArray ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private boolean registerFacilitator(Participant participant) {
        try {

            //get missing variables
            System.out.print("\nArea Of Expertise: ");
            String areaOfExpertise = reader.readLine();

            //create facilitator
            Facilitator newFacilitator = new FacilitatorImp(areaOfExpertise,
                    participant.getName(), participant.getEmail(),
                    participant.getContact(), participant.getInstituition());

            //add newFacilitator to participant manager
            pm.addParticipant(newFacilitator);

            return true;
        } catch (IOException ex) {
            System.out.println("Error reading input.\n\n");
            return false;
        } catch (AlreadyExistsInArray ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private boolean registerPartner(Participant participant) {
        try {

            //get missing variables
            System.out.print("\nVat: ");
            String vat = reader.readLine();
            System.out.print("\nWebSite: ");
            String website = reader.readLine();

            //create partner
            Partner newPartner = new PartnerImp(vat, website,
                    participant.getName(), participant.getEmail(),
                    participant.getContact(), participant.getInstituition());

            //add newPartner to participant manager
            pm.addParticipant(newPartner);

            return true;

        } catch (IOException ex) {
            System.out.println("Error reading input.\n\n");
            return false;
        } catch (AlreadyExistsInArray ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private void assignInstituition(Participant p) {

        System.out.println("== Institutions Selection ==");
        try {
            Instituition[] instituitions = this.im.getInstituitions();
            System.out.println("   == Available Institutions == ");
            int i = 0;
            for (i = 0; i < instituitions.length; i++) {
                System.out.println((i + 1) + ". " + instituitions[i].getName());
            }
            System.out.println((i + 1) + ". No instituition");
            System.out.print("Select an institution: ");
            try {
                int institutionNumber = Integer.parseInt(reader.readLine());

                // check if it's valid
                if (institutionNumber >= 1 && institutionNumber <= instituitions.length) {
                    Instituition selectedInstitution = instituitions[institutionNumber - 1];
                    p.setInstituition(selectedInstitution);
                    System.out.println("Assigned with success to " + instituitions[institutionNumber - 1].getName());
                } else if (institutionNumber == i + 1) {
                    p.setInstituition(null);
                    System.out.println("Not Assigned to any Instituition.");
                } else {
                    System.out.println("Invalid selection. Please try again.\n\n");
                    assignInstituition(p);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");
                assignInstituition(p);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading input.");
        }

    }

    private Participant[] listParticipants() {

        System.out.println(" ===== All Participants registered ======= ");
        Participant[] participants = pm.getParticipants();
        int counter = 0;
        if (pm.getNumberOfFacilitators() > 0) {
            System.out.println("Facilitators: ");
            for (int i = 0; i < pm.getNumberOfFacilitators(); i++) {
                if (participants[counter] instanceof Facilitator) {
                    System.out.println((counter + 1) + ". " + participants[counter].getName() + "(" + participants[counter].getEmail() + ")");
                    counter++;
                }
            }
        }

        if (pm.getNumberOfStudents() > 0) {
            System.out.println("Students: ");
            for (int i = 0; i < pm.getNumberOfStudents(); i++) {
                if (participants[counter] instanceof Student) {
                    System.out.println((counter + 1) + ". " + participants[counter].getName() + "(" + participants[counter].getEmail() + ")");
                    counter++;
                }
            }
        }

        if (pm.getNumberOfPartners() > 0) {
            System.out.println("Partners: ");
            for (int i = 0; i < pm.getNumberOfPartners(); i++) {
                if (participants[counter] instanceof Partner) {
                    System.out.println((counter + 1) + ". " + participants[counter].getName() + "(" + participants[counter].getEmail() + ")");
                    counter++;
                }
            }
        }
        return participants;
    }

    private void showParticipantsMenu() {
        boolean exit = false;
        while (!exit) {

            Participant[] participants = listParticipants();

            int counter = participants.length;

            System.out.println("");
            System.out.println((counter + 1) + ". Delete participant");
            System.out.println((counter + 2) + ". Back");

            try {
                int participantNumber = Integer.parseInt(reader.readLine());

                // Verifique se o número da tarefa é válido
                if (participantNumber == (counter + 2)) {
                    exit = true;
                } else if (participantNumber == (counter + 1)) {
                    System.out.println("Select the number of the Participant you want to remove: ");
                    int removeParticipant = Integer.parseInt(reader.readLine());

                    if (counter != 0 && removeParticipant >= 1 && removeParticipant <= participants.length) {
                        System.out.println("Are you sure you want to remove this participant? (yes/no)");
                        String answer = reader.readLine();

                        if (answer.equalsIgnoreCase("yes")) {
                            try {
                                pm.removeParticipant(participants[removeParticipant - 1].getEmail());
                                System.out.println("Removed Successfully!\n");
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }

                        } else {
                            System.out.println("Canceled removal");
                        }
                    } else if (counter != 0 && removeParticipant >= 1 && removeParticipant <= participants.length) {
                        Participant participant = participants[participantNumber - 1];
//                        showParticipantDetails(participant);
                    } else {
                        System.out.println("Invalid selection");
                    }
                } else {
                    System.out.println("Invalid selection. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");

            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }

    }

    private void showEditionsMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("===== Editions Menu =====");
            try {
                Edition[] editions = cbl.getEditionsByParticipant(loggedInParticipant);

                //list the editions
                int i = 0;
                for (i = 0; i < editions.length; i++) {
                    System.out.println((i + 1) + ". " + editions[i].getName() + "("
                            + editions[i].getStatus().toString() + ")"
                    );
                }
                System.out.println((i + 1) + ". Back");
                System.out.print("Enter the number of the edition: ");
                try {
                    int editionNumber = Integer.parseInt(reader.readLine());

                    // check if it's valid
                    if (editionNumber == i + 1) {
                        exit = true;
                    } else if (editionNumber >= 1 && editionNumber <= editions.length) {
                        currentEdition = editions[editionNumber - 1];
                        showProjectsMenu();

                    } else {
                        System.out.println("Invalid selection. Please try again.\n\n");

                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.\n\n");

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void showAdminMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println(" ==== Admin Menu ==== ");
            System.out.println(" 1. Manage CBL");
            System.out.println(" 2. Manage Users/Participants");
            System.out.println(" 3. Manage Instituitions");
            System.out.println(" 4. Exit");
            System.out.print("Select option: ");

            try {
                int option = Integer.parseInt(reader.readLine());

                if (option == 1) {
                    showAdminEditionsMenu();

                } else if (option == 2) {
                    showParticipantsMenu();
                } else if (option == 3) {
                    //showInstituitionsMenu();
                } else if (option == 4) {
                    exit = true;
                } else {
                    System.out.println("Invalid selection. Try again!\n");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");
            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void showAdminEditionsMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("===== Editions Menu =====");
            System.out.println("Number of Edition: " + cbl.getNumberOfEditions());

            try {
                Edition[] editions = cbl.getEditions();
                //list the editions
                int i = 0;
                if (cbl.getNumberOfEditions() != 0) {
                    System.out.println(" ---- Editions List -----");

                    for (i = 0; i < editions.length; i++) {
                        System.out.println((i + 1) + ". " + editions[i].getName() + "("
                                + editions[i].getStatus().toString() + ")"
                        );
                    }
                    System.out.println(" ------------------- ");
                }
                System.out.println((i + 1) + ". Add/create an edition");
                System.out.println((i + 2) + ". List uncompleted editions");
                System.out.println((i + 3) + ". Back");
                System.out.print("Select option: ");
                try {
                    int editionNumber = Integer.parseInt(reader.readLine());

                    // check if it's valid
                    if (editionNumber == i + 3) {
                        exit = true;
                    } else if (editions.length != 0 && editionNumber >= 1 && editionNumber <= editions.length) {
                        currentEdition = editions[editionNumber - 1];
                        showAdminProjectsMenu();
                    } else if (editionNumber == i + 1) {
                        try {
                            showAddEditions();
                        } catch (EditionAlreadyInCBL e) {
                            System.out.println(e.getMessage());
                        }
                    } else if (editionNumber == i + 2) {
                        showUncompletedEditions();
                    } else {
                        System.out.println("Invalid selection. Please try again.\n\n");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.\n\n");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void showAddEditions() throws EditionAlreadyInCBL {
        System.out.println("===== Add/Create Edition =====");
        try {

            System.out.print("\nName: ");
            String name = reader.readLine();

            LocalDate start = null;
            while (start == null) {
                System.out.print("Start date (yyyy-mm-dd): ");
                String startDate = reader.readLine();
                try {
                    start = LocalDate.parse(startDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-mm-dd format.");
                }
            }

            LocalDate end = null;
            while (end == null) {
                System.out.print("End date (yyyy-mm-dd): ");
                String endDate = reader.readLine();
                try {
                    end = LocalDate.parse(endDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-mm-dd format.");
                }
            }

            Edition edition = new EditionImp(name, start, end);

            cbl.addEdition(edition);

        } catch (IOException e) {
            System.out.println("Error reading input.");

        }

    }

    private void showUncompletedEditions() {
        System.out.println("===== Uncompleted Editions =====");
        boolean exit = false;
        while (!exit) {
            try {
                Edition[] editions = cbl.uncompletedEditions();

                int i = 0;

                for (i = 0; i < editions.length; i++) {
                    System.out.println((i + 1) + ". " + editions[i].getName() + "("
                            + editions[i].getStatus().toString() + ")"
                    );
                }
                System.out.println((i + 1) + ". Back");
                System.out.print("Select option: ");
                try {
                    int editionNumber = Integer.parseInt(reader.readLine());

                    // check if it's valid
                    if (editionNumber == i + 1) {
                        exit = true;
                    } else if (editionNumber >= 1 && editionNumber <= editions.length) {
                        currentEdition = editions[editionNumber - 1];
                        showAdminProjectsMenu();
                    } else {
                        System.out.println("Invalid selection. Please try again.\n\n");

                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.\n\n");

                } catch (IOException e) {
                    System.out.println("Error reading input.");
                }
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void showProjectsMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("===== Projects Menu =====");
            System.out.println("Edition: " + currentEdition.getName() + "("
                    + currentEdition.getStatus().toString() + ")");

            Project[] projects = currentEdition.getProjectsOf(loggedInParticipant.getEmail());

            int i = 0;
            for (i = 0; i < projects.length; i++) {
                System.out.println((i + 1) + ". " + projects[i].getName());
            }
            System.out.println((i + 1) + ". Back");
            System.out.print("Select an option: ");
            try {
                int projectNumber = Integer.parseInt(reader.readLine());

                // Verifique se o número do projeto é válido
                if (projectNumber == i + 1) {
                    exit = true;
                } else if (projectNumber >= 1 && projectNumber <= projects.length) {
                    currentProject = projects[projectNumber - 1];

                    showProjectDetails();
                } else {
                    System.out.println("Invalid selection. Please try again.\n\n");

                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");
            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void showAdminProjectsMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("== Edition: " + currentEdition.getName() + "("
                    + currentEdition.getStatus().toString() + ") ==");

            Project[] projects = currentEdition.getProjects();
            int i = 0;
            if (currentEdition.getNumberOfProjects() != 0) {
                System.out.println(" ---- Projects List -----");

                for (i = 0; i < projects.length; i++) {
                    System.out.println((i + 1) + ". " + projects[i].getName());
                }

                System.out.println(" --------------------- ");
            }

            System.out.println((i + 1) + ". Activate edition");
            System.out.println((i + 2) + ". Remove edition");
            System.out.println(((i + 3) + ". Add Project"));
            System.out.println(((i + 4) + ". Get Projects by tag"));
            System.out.println((i + 5) + ". Back");
            System.out.print("Select an option: ");
            try {
                int projectNumber = Integer.parseInt(reader.readLine());

                // Verifique se o número do projeto é válido
                if (projectNumber == i + 5) {
                    exit = true;
                } else if (projectNumber >= 1 && projectNumber <= projects.length) {
                    currentProject = projects[projectNumber - 1];
                    showAdminProjectDetails();
                } else if (projectNumber == i + 1) {
                    try {
                        cbl.activateEdition(currentEdition.getName());

                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());

                    }
                } else if (projectNumber == i + 2) {
                    System.out.println("Are you sure you want to remove this edition? (yes/no)");
                    String answer = reader.readLine();

                    if (answer.equalsIgnoreCase("yes")) {
                        showRemoveEditionMenu();
                        currentEdition = null;
                        exit = true;
                    } else {
                        System.out.println("Removal canceled.");
                    }
                } else if (projectNumber == i + 3) {
                    showAddProject();
                } else if (projectNumber == i + 4) {

                    System.out.println("Tag to search: ");
                    String tag = reader.readLine();
                    tag = tag.trim();
                    if (tag != null) {
                        listProjectsByTag(tag);
                    }

                } else {
                    System.out.println("Invalid selection. Please try again.\n\n");

                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");

            } catch (IOException e) {
                System.out.println("Error reading input.");

            }
        }
    }

    private void showRemoveEditionMenu() {
        boolean complete = false;

        try {
            Edition removedEdition = cbl.removeEdition(currentEdition.getName());
            System.out.println("Edition removed successfully.");
            while (!complete) {
                try {

                    System.out.println("Do you want to save it to a json file? (yes/no)");
                    String saveAnswer = reader.readLine();
                    if (saveAnswer.equalsIgnoreCase("yes")) {

                        System.out.println("Name of the file you want to save it to: ");
                        String name = reader.readLine();
                        if (name.equalsIgnoreCase("cbl") || name.equalsIgnoreCase("instituitions")
                                || name.equalsIgnoreCase("project_template") || name.equalsIgnoreCase("users")) {
                            System.out.println("Invalid name.");
                        } else {
                            String path = null;
                            if (name.contains(".json")) {
                                path = "src/Files/" + name;
                            } else {
                                path = "src/Files/" + name + ".json";
                            }
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("removed Edition", ((EditionImp) removedEdition).toJsonObj());
                                FileWriter fileWriter = new FileWriter(path);
                                fileWriter.write(jsonObject.toJSONString());
                                fileWriter.close();
                                complete = true;

                            } catch (IOException e) {
                                e.getMessage();
                            }
                        }

                    } else {
                        complete = true;
                    }
                } catch (IOException e) {
                    System.out.println("Error reading input.");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }

    private void showAddProject() {
        System.out.println("===== Add New Project =====");
        try {

            System.out.print("\nName: ");
            String name = reader.readLine();

            System.out.print("\nDescription: ");
            String description = reader.readLine();

            System.out.println("Tags: (tag1,tag2,tag3) ");
            String allTags = reader.readLine();

            String[] tagsArray = allTags.split(",");
            for (int i = 0; i < tagsArray.length; i++) {
                tagsArray[i] = tagsArray[i].trim();
            }
            currentEdition.addProject(name, description, tagsArray);
            System.out.println("Project added successfully");
        } catch (IOException | ParseException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listProjectsByTag(String tag) {
        boolean exit = false;
        while (!exit) {
            System.out.println("===== Projects With Tag " + tag + " =====");

            Project[] projects = currentEdition.getProjectsByTag(tag);
            if (projects == null) {
                System.out.println(" No projects with tag " + tag + " found!\n\n");
                exit = true;
            } else {

                int i = 0;
                for (i = 0; i < projects.length; i++) {
                    System.out.println((i + 1) + ". " + projects[i].getName());
                }

                System.out.println(" ------------------------ ");
                System.out.println((i + 1) + ". Back");
                System.out.print("Select an option: ");
                try {
                    int projectNumber = Integer.parseInt(reader.readLine());

                    // Verifique se o número do projeto é válido
                    if (projectNumber == i + 1) {
                        exit = true;
                    } else if (projectNumber >= 1 && projectNumber <= projects.length) {
                        currentProject = projects[projectNumber - 1];
                        showAdminProjectDetails();
                        exit = true;
                    } else {
                        System.out.println("Invalid selection. Please try again.\n\n");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.\n\n");

                } catch (IOException e) {
                    System.out.println("Error reading input.");
                }
            }
        }
    }

    private void showAdminProjectDetails() {
        boolean exit = false;
        while (!exit) {

            System.out.println("===== Project Details =====");
            System.out.println("Project: " + currentProject.getName());
            System.out.println("Description: " + currentProject.getDescription());
            System.out.print("Tags: ");

            String[] tags = currentProject.getTags();
            for (String s : tags) {
                System.out.print("#" + s + " ");
            }
            System.out.println("\nStatus: " + (currentProject.isCompleted() ? "Completed" : "Incomplete"));
            System.out.println("Participants: " + currentProject.getNumberOfParticipants() + "/" + currentProject.getMaximumNumberOfParticipants());
            System.out.println(" -- Facilitators: " + currentProject.getNumberOfFacilitators() + "/" + currentProject.getMaximumNumberOfFacilitators());
            System.out.println(" -- Students: " + currentProject.getNumberOfStudents() + "/" + currentProject.getMaximumNumberOfStudents());
            System.out.println(" -- Partners: " + currentProject.getNumberOfPartners() + "/" + currentProject.getMaximumNumberOfPartners());
            System.out.println("\nTasks: " + currentProject.getNumberOfTasks() + "/" + currentProject.getMaximumNumberOfTasks());

            Task[] tasks = currentProject.getTasks();

            int i = 0;
            for (i = 0; i < tasks.length; i++) {
                System.out.println((i + 1) + ". " + tasks[i].getTitle());
            }
            System.out.println(" ----------------------- ");
            System.out.println((i + 1) + ". Remove this project");
            System.out.println((i + 2) + ". List participants");
            System.out.println((i + 3) + ". Add participants ("
                    + (currentProject.getNumberOfParticipants() == currentProject.getMaximumNumberOfParticipants() ? "Not Available" : " Available") + ")");
            // System.out.println(". Remove participants");
            System.out.println((i + 4) + ". Add Task ("
                    + (currentProject.getNumberOfTasks() == currentProject.getMaximumNumberOfTasks() ? "Not Available" : " Available") + ")");
            // System.out.println(". Remove Task");
            System.out.println("\n" + (i + 5) + ". Back");
            System.out.print("Select an option: ");
            try {
                int taskNumber = Integer.parseInt(reader.readLine());

                // Verifique se o número da tarefa é válido
                if (taskNumber == (i + 5)) {
                    exit = true;
                } else if (taskNumber == (i + 1)) {
                    System.out.println("Are you sure you want to remove this project? (yes/no)");
                    String answer = reader.readLine();

                    if (answer.equalsIgnoreCase("yes")) {
                        try {
                            cbl.getEdition(currentEdition.getName()).removeProject(currentProject.getName());
                            System.out.println("Project removed successfully!\n");
                            exit = true;
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } else if (taskNumber == (i + 2)) {
                    if (currentProject.getNumberOfParticipants() > 0) {
                        listParticipantsOfProject();
                    } else {
                        System.out.println("No participants in project. Add participants");
                    }
                } else if (taskNumber == (i + 3)) {
                    if (currentProject.getMaximumNumberOfParticipants() != currentProject.getNumberOfParticipants()) {
                        try {
                            showAddParticipant();
                            System.out.println("Added Successfully!\n");
                        } catch (IllegalNumberOfParticipantType | ParticipantAlreadyInProject e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Maximum ammount of Participants reached!\n");
                    }

                } else if (taskNumber == (i + 4)) {
                    if (currentProject.getNumberOfTasks() == currentProject.getMaximumNumberOfTasks()) {
                        try {
                            showAddTask();
                        } catch (IllegalNumberOfTasks | TaskAlreadyInProject e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Maximum ammount of Tasks reached!\n");
                    }

                } else if (taskNumber >= 1 && taskNumber <= tasks.length) {
                    Task selectedTask = tasks[taskNumber - 1];
                    showAdminTaskDetails(selectedTask);

                } else {
                    System.out.println("Invalid selection. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");

            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void showAddTask() throws IllegalNumberOfTasks, TaskAlreadyInProject {
        System.out.println(" === Add Task ===");
        try {

            System.out.print("\nTitle: ");
            String title = reader.readLine();

            System.out.print("\nDescription: ");
            String description = reader.readLine();

            LocalDate start = null;
            while (start == null) {
                System.out.print("Start date (yyyy-mm-dd): ");
                String startDate = reader.readLine();
                try {
                    start = LocalDate.parse(startDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-mm-dd format.");
                }
            }

            LocalDate end = null;
            while (end == null) {
                System.out.print("End date (yyyy-mm-dd): ");
                String endDate = reader.readLine();
                try {
                    end = LocalDate.parse(endDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-mm-dd format.");
                }
            }

            Task task = new TaskImp(title, description, start, end, 0);
            currentProject.addTask(task);
        } catch (IOException e) {
            System.out.println("Error reading input.");
        }
    }

    private void showAddParticipant() throws IllegalNumberOfParticipantType, ParticipantAlreadyInProject {

        Participant[] participants = listParticipants();
        int counter = participants.length;
        System.out.println((counter + 2) + ". Baack");

        try {
            int participantNumber = Integer.parseInt(reader.readLine());

            if (counter != 0 && participantNumber >= 1 && participantNumber <= participants.length) {
                Participant selectedParticipant = participants[participantNumber - 1];
                currentProject.addParticipant(selectedParticipant);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");

        } catch (IOException e) {
            System.out.println("Error reading input.");
        }

    }

    private void listParticipantsOfProject() {
        boolean exit = false;
        while (!exit) {

            System.out.println(" == Participants of " + currentProject.getName() + " == ");

            Participant[] participants = ((ProjectImp) currentProject).getParticipants();
            int counter = 0;

            if (currentProject.getNumberOfFacilitators() > 0) {
                System.out.println("Facilitators: ");
                for (int i = 0; i < currentProject.getNumberOfFacilitators(); i++) {
                    if (participants[counter] instanceof Facilitator) {
                        System.out.println((counter + 1) + ". " + participants[counter].getName() + "(" + participants[counter].getEmail() + ")");
                        counter++;
                    }
                }
            }

            if (currentProject.getNumberOfStudents() > 0) {
                System.out.println("Students: ");
                for (int i = 0; i < currentProject.getNumberOfStudents(); i++) {
                    if (participants[counter] instanceof Student) {
                        System.out.println((counter + 1) + ". " + participants[counter].getName() + "(" + participants[counter].getEmail() + ")");
                        counter++;
                    }
                }
            }

            if (currentProject.getNumberOfPartners() > 0) {
                System.out.println("Partners: ");
                for (int i = 0; i < currentProject.getNumberOfPartners(); i++) {
                    if (participants[counter] instanceof Partner) {
                        System.out.println((counter + 1) + ". " + participants[counter].getName() + "(" + participants[counter].getEmail() + ")");
                        counter++;
                    }
                }
            }

            System.out.println((counter + 1) + ". Remove from this project");
            System.out.println((counter + 2) + ". Baack");

            try {
                int participantNumber = Integer.parseInt(reader.readLine());

                // Verifique se o número da tarefa é válido
                if (participantNumber == (counter + 2)) {
                    exit = true;
                } else if (participantNumber == (counter + 1)) {
                    System.out.println("Select the number of the Participant you want to remove: ");
                    int removeParticipant = Integer.parseInt(reader.readLine());

                    if (counter != 0 && removeParticipant >= 1 && removeParticipant <= participants.length) {
                        System.out.println("Are you sure you want to remove this participant? (yes/no)");
                        String answer = reader.readLine();

                        if (answer.equalsIgnoreCase("yes")) {
                            try {
                                currentProject.removeParticipant(participants[removeParticipant - 1].getEmail());
                                System.out.println("Removed Successfully!\n");
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }

                        } else {
                            System.out.println("Canceled removal");
                        }
                    } else {
                        System.out.println("Invalid selection");
                    }
                } else {
                    System.out.println("Invalid selection. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");

            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }

    }

    private void showAdminTaskDetails(Task task) {
        boolean exit = false;

        while (!exit) {

            System.out.println("=== Task Details ===");
            System.out.println("Title: " + task.getTitle());
            System.out.println("Description: " + task.getDescription());
            System.out.println("Started: " + task.getStart().toString());
            System.out.println("End: " + task.getEnd().toString());
            System.out.println("Number of submissions: " + task.getNumberOfSubmissions());

            System.out.println("\nSelect an option:");
            System.out.println("1. List Submissions");
            System.out.println("2. Extend DeadLine");
            System.out.println("3. Back");
            System.out.print("\nSelect an option: ");
            int option = 0;
            try {

                option = Integer.parseInt(reader.readLine());

                switch (option) {
                    case 1:
                        listSubmissions(task);
                        break;
                    case 2:
                        System.out.println(" == Extend DeadLine == ");

                        System.out.println("Number of Days to extend: ");
                        int days = Integer.parseInt(reader.readLine());

                        System.out.println("\nThe task" + task.getTitle() + " end date will be extended by " + days + " days.");
                        task.extendDeadline(days);
                        System.out.println("New End Date: " + task.getEnd().toString());
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid selection. Please try again.\n\n");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");

            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void showProjectDetails() {
        boolean exit = false;
        while (!exit) {

            System.out.println("===== Project Details =====");
            System.out.println("Project: " + currentProject.getName());
            System.out.println("Description: " + currentProject.getDescription());
            System.out.print("Tags: ");

            String[] tags = currentProject.getTags();
            for (String s : tags) {
                System.out.print("#" + s + " ");
            }
            System.out.println("\nStatus: " + (currentProject.isCompleted() ? "Completed" : "Incomplete"));
            System.out.println("Participants: " + currentProject.getNumberOfParticipants() + "/" + currentProject.getMaximumNumberOfParticipants());
            System.out.println(" -- Facilitators: " + currentProject.getNumberOfFacilitators() + "/" + currentProject.getMaximumNumberOfFacilitators());
            System.out.println(" -- Students: " + currentProject.getNumberOfStudents() + "/" + currentProject.getMaximumNumberOfStudents());
            System.out.println(" -- Partners: " + currentProject.getNumberOfPartners() + "/" + currentProject.getMaximumNumberOfPartners());
            System.out.println("\nTasks: " + currentProject.getNumberOfTasks() + "/" + currentProject.getMaximumNumberOfTasks());

            Task[] tasks = currentProject.getTasks();
            int i = 0;
            for (i = 0; i < tasks.length; i++) {
                System.out.println((i + 1) + ". " + tasks[i].getTitle());
            }
            System.out.println("\n" + (i + 1) + ". Back");
            System.out.print("Enter the number of the task: ");
            try {
                int taskNumber = Integer.parseInt(reader.readLine());

                // Verifique se o número da tarefa é válido
                if (taskNumber == (i + 1)) {
                    exit = true;
                } else if (taskNumber >= 1 && taskNumber <= tasks.length) {
                    Task selectedTask = tasks[taskNumber - 1];
                    showTaskDetails(selectedTask);
                } else {
                    System.out.println("Invalid selection. Please try again.");

                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");

            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void showTaskDetails(Task task) {
        boolean isStudent = false, exit = false;
        if (loggedInParticipant instanceof Student) {
            isStudent = true;
        }
        while (!exit) {

            System.out.println("=== Task Details ===");
            System.out.println("Title: " + task.getTitle());
            System.out.println("Description: " + task.getDescription());
            System.out.println("Started: " + task.getStart().toString());
            System.out.println("End: " + task.getEnd().toString());
            System.out.println("Number of submissions: " + task.getNumberOfSubmissions());

            System.out.println("\nSelect an option:");
            System.out.println("1. List Submissions");

            if (isStudent) {
                System.out.println("2. Add submission");
                System.out.println("3. Back");
            } else {
                System.out.println("2. Back");
            }
            int option = 0;
            try {
                if (isStudent) {
                    do {
                        System.out.print("\nOption: ");
                        option = Integer.parseInt(reader.readLine());
                    } while (option < 1 || option > 3);

                } else {
                    do {
                        System.out.print("\nOption: ");
                        option = Integer.parseInt(reader.readLine());
                    } while (option < 1 || option > 2);
                }
                if (option == 1) {
                    listSubmissions(task);
                    // if option 2 && student, then allow to submit 
                } else if (option == 2 && isStudent) {
                    if (submitWork(task)) {
                        System.out.println("Success submitting work!");
                    } else {
                        System.out.println("Failed to submit work!");
                    }
                    //if not student, option 2 to exit or if student and option 3 exit
                } else {
                    exit = true;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");

            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void listSubmissions(Task task) {
        boolean exit = false;
        while (!exit) {
            int i = 0;
            Submission[] submissions = null;
            if (task.getNumberOfSubmissions() > 0) {

                submissions = task.getSubmissions();

                for (i = 0; i < submissions.length; i++) {
                    System.out.println((i + 1) + ". " + submissions[i].getText());
                }
            } else {
                System.out.println("  --- No submissions found! ---\n\n");
            }

            System.out.println((i + 1) + ". Back");
            System.out.print("\nSelect: ");
            try {
                int submissionNumber = Integer.parseInt(reader.readLine());

                if (submissionNumber == (i + 1)) {
                    exit = true;
                } else if (submissionNumber >= 1 && submissionNumber <= submissions.length) {
                    Submission selectedSubmission = submissions[submissionNumber - 1];
                    showSubmissionDetails(selectedSubmission);
                } else {
                    System.out.println("Invalid selection. Please try again.\n\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");
            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void showSubmissionDetails(Submission submission) {
        boolean exit = false;

        while (!exit) {
            System.out.println("== Submission ==");
            System.out.println("Student: " + submission.getStudent().getName() + "(" + submission.getStudent().getEmail() + ")");
            System.out.println("Text: " + submission.getText());
            System.out.println("Date: " + submission.getDate().toString());

            System.out.println("\n 1. Back");
            try {
                int option = Integer.parseInt(reader.readLine());
                if (option == 1) {
                    exit = true;
                } else {
                    System.out.println("Invalid selection. Please try again.");

                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");

            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private boolean submitWork(Task task) {
        System.out.println("=== Submit Work ===");
        System.out.println("Student: " + loggedInParticipant.getEmail());
        try {
            System.out.print("Enter the text: ");
            String text = reader.readLine();

            Submission newSubmission = new SubmissionImp((Student) loggedInParticipant, text);
            System.out.println("Date of Submission: " + newSubmission.getDate().toString());

            try {
                task.addSubmission(newSubmission);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return false;
            }
            return true;

        } catch (IOException e) {
            System.out.println("Error reading input.");
            return false;
        }
    }

    public static void main(String[] args) {

        //    try{
        // Crie uma instância da classe CBL e adicione participantes, edições, projetos, etc.
        ParticipantsManager pm = new ParticipantsManager();
        CBL cbl = new CBLImp();
        InstituitionsManager im = new InstituitionsManager();

        Contact c = new ContactImp("Street", "city", "state", "zipCode", "country", "phone");
        Instituition estg = new InstituitionImp("ESTG", "estg@estg.ipp.pt", "estg.ipp.pt", "Escola de Tecnologia e Gestão de Felgueiras", c, InstituitionType.UNIVERSITY);
        Student p1 = new StudentImp("David Santos", "david@estg.ipp.pt", c, estg);
//            try {
//                cbl.addEdition(new EditionImp("Test edition", LocalDate.of(2023, Month.MARCH, 13), LocalDate.of(2023, Month.JUNE, 17)));
//            } catch (EditionAlreadyInCBL ex) {
//                System.out.println(ex.getMessage());
//            }
//            String[] tags = {"java", "teste"};
//            cbl.getEdition("Test edition").addProject("Project test", "testando projeto", tags);
//            cbl.getEdition("Test edition").getProject("Project test").addParticipant(p1);
//
        try {
            pm.addParticipant(p1);
            im.addInstituition(estg);
        } catch (AlreadyExistsInArray | InstituitionAlreadyExistException ex) {
            System.out.println(ex.toString());
        }
        // Create menu and import data
        if (pm.importData("src/Files/users.json")/* && im.importData("src/Files/instituitions.json")*/) {
            System.out.println("Success importing program users");
        }
        if (cbl.importData("src/Files/cbl.json")) {

            System.out.println("Success importing program data");
        } else {
            System.out.println("Error importing program data");
        }

        Menu menu = new Menu(cbl, pm, im);
        menu.start();
//export data before close program

        if (cbl.export("src/Files/cbl.json")) {
            System.out.println("Success exporting program data");
        } else {
            System.out.println("Error exporting program data");
        }
        if (pm.export("src/Files/users.json") && im.export("src/Files/instituitions.json")) {
            System.out.println("Success exporting program users");
        }

//        } catch (IOException ex) {
//            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ParseException ex) {
//            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalNumberOfParticipantType ex) {
//            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ParticipantAlreadyInProject ex) {
//            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
