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
import CBLStructure.SubmissionImp;
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
import pack.StudentImp;
import pack.FacilitatorImp;
import pack.PartnerImp;
import ma02_resources.participants.Partner;
import ma02_resources.project.Status;
import ma02_resources.project.Submission;

public class Menu {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private CBL cbl;
    private ParticipantsManager pm;
    private InstituitionsManager im;
    private Participant loggedInParticipant;
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
            } catch (IOException e) {
                System.out.println("Error reading input.");
                continue;
            }
            switch (option) {
                case 1:
                    if (login()) {
                        showMyEditionsMenu();
                    }
                    break;
                case 2:
                    if (register()) {
                        System.out.println("Registration Success. Login to continue!");
                    }
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
            
            listInstituitions(newParticipant);
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

    private void listInstituitions() {

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

    private void listInstituitions(Participant p) {

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
                    listInstituitions(p);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");
                listInstituitions(p);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading input.");
        }

    }

    private void showMyEditionsMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("===== Editions Menu =====");
            // System.out.println("Select an edition:");
            try {
                Edition[] editions = cbl.getEditionsByParticipant(loggedInParticipant);
                System.out.println("   == Your Editions == ");
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
                    if (editionNumber >= 1 && editionNumber <= editions.length) {
                        Edition selectedEdition = editions[editionNumber - 1];
                        showProjectsMenu(selectedEdition);
                    } else if (editionNumber == i + 1) {
                        exit = true;
                    } else {
                        System.out.println("Invalid selection. Please try again.\n\n");
                        showMyEditionsMenu();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.\n\n");
                    showMyEditionsMenu();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void showProjectsMenu(Edition edition) {
        boolean exit = false;
        while (!exit) {
            System.out.println("===== Projects Menu =====");
            System.out.println("Edition: " + edition.getName() + "("
                    + edition.getStatus().toString() + ")");

            Project[] projects = edition.getProjects();
            int i = 0;
            for (i = 0; i < projects.length; i++) {
                System.out.println((i + 1) + ". " + projects[i].getName());
            }
            System.out.println((i + 1) + ". Back");
            System.out.print("Enter the number of the project: ");
            try {
                int projectNumber = Integer.parseInt(reader.readLine());

                // Verifique se o número do projeto é válido
                if (projectNumber >= 1 && projectNumber <= projects.length) {
                    Project selectedProject = projects[projectNumber - 1];
                    showProjectDetails(selectedProject);
                } else if (projectNumber == i + 1) {
                    exit = true;
                } else {
                    System.out.println("Invalid selection. Please try again.\n\n");
                    showProjectsMenu(edition);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");
                showProjectsMenu(edition);
            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void showProjectDetails(Project project) {
        boolean exit = false;
        while (!exit) {

            System.out.println("===== Project Details =====");
            System.out.println("Project: " + project.getName());
            System.out.println("Description: " + project.getDescription());
            System.out.print("Tags: ");

            String[] tags = project.getTags();
            for (String s : tags) {
                System.out.print("#" + s + " ");
            }
            System.out.println("\nStatus: " + (project.isCompleted() ? "Completed" : "Incomplete"));
            System.out.println("Participants: " + project.getNumberOfParticipants() + "/" + project.getMaximumNumberOfParticipants());
            System.out.println(" -- Facilitators: " + project.getNumberOfFacilitators() + "/" + project.getMaximumNumberOfFacilitators());
            System.out.println(" -- Students: " + project.getNumberOfStudents() + "/" + project.getMaximumNumberOfStudents());
            System.out.println(" -- Partners: " + project.getNumberOfPartners() + "/" + project.getMaximumNumberOfPartners());
            System.out.println("\nTasks: " + project.getNumberOfTasks() + "/" + project.getMaximumNumberOfTasks());

            Task[] tasks = project.getTasks();
            int i = 0;
            for (i = 0; i < tasks.length; i++) {
                System.out.println((i + 1) + ". " + tasks[i].getTitle());
            }
            System.out.println("\n" + (i + 1) + ". Back");
            System.out.print("Enter the number of the task: ");
            try {
                int taskNumber = Integer.parseInt(reader.readLine());

                // Verifique se o número da tarefa é válido
                if (taskNumber >= 1 && taskNumber <= tasks.length) {
                    Task selectedTask = tasks[taskNumber - 1];
                    showTaskDetails(selectedTask);
                } else if (taskNumber == (i + 1)) {
                    exit = true;
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
            System.out.println("Numer of submissions: " + task.getNumberOfSubmissions());

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
                showTaskDetails(task);
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
                } else {
                    System.out.println("Invalid selection. Please try again.\n\n");
                    listSubmissions(task);
                }

                if (task.getNumberOfSubmissions() > 0) {
                    // Validate submission number
                    if (submissionNumber >= 1 && submissionNumber <= submissions.length) {
                        Submission selectedSubmission = submissions[submissionNumber - 1];
                        showSubmissionDetails(selectedSubmission);
                    } else {
                        System.out.println("Invalid selection. Please try again.\n\n");
                        listSubmissions(task);
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");
                listSubmissions(task);
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
                    showSubmissionDetails(submission);
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                showSubmissionDetails(submission);
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
        } catch (AlreadyExistsInArray ex) {
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
