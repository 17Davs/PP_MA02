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
import ma02_resources.participants.InstituitionType;
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
                            showParticipantsMenu();
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

            Contact contact = assignContact();

            // create participant without instituition
            Participant newParticipant = new ParticipantImp(name, email, contact, null);

            //Participant chooses an instituition to be assigned to
            assignInstituition(newParticipant);

            //registar as Participant specific type
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

    private Contact assignContact() {
        try {
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
            return contact;

        } catch (IOException ex) {
            System.out.println("Error reading imput.");
            Contact contact = assignContact();
            return contact;
        }
    }

    private void listInstituitions() {
        boolean exit = false;
        while (!exit) {

            System.out.println("=== Institutions Selection ===");
            try {
                Instituition[] instituitions = getInstituitionsOutput();
                int i = instituitions.length;

                System.out.println((i + 1) + ". Back");
                System.out.print("Select an institution: ");
                try {
                    int institutionNumber = Integer.parseInt(reader.readLine());

                    // check if it's valid
                    if (institutionNumber >= 1 && institutionNumber <= instituitions.length) {
                        Instituition selectedInstitution = instituitions[institutionNumber - 1];
                        showInstituitionDetails(selectedInstitution);
                    } else if (institutionNumber == i + 1) {
                        exit = true;
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

    private Instituition[] getInstituitionsOutput() throws NullPointerException {
        System.out.println("=== Institutions ===");
        Instituition[] instituitions = this.im.getInstituitions();

        int i = 0;
        for (i = 0; i < instituitions.length; i++) {
            System.out.println((i + 1) + ". " + instituitions[i].getName());
        }
        return instituitions;
    }

    private void showInstituitionDetails(Instituition instituition) {
        boolean exit = false;
        while (!exit) {

            try {

                System.out.println(" === Instituition Details ===");
                System.out.println("Name: " + instituition.getName());
                System.out.println("Email: " + instituition.getEmail());
                System.out.println("Website: " + instituition.getWebsite());
                System.out.println("Description: " + instituition.getDescription());
                System.out.println("Institution Type: " + instituition.getType());

                System.out.println("Contact Details:");
                System.out.println("Street: " + instituition.getContact().getStreet());
                System.out.println("City: " + instituition.getContact().getCity());
                System.out.println("State: " + instituition.getContact().getState());
                System.out.println("Zip Code: " + instituition.getContact().getZipCode());
                System.out.println("Country: " + instituition.getContact().getCountry());
                System.out.println("Phone: " + instituition.getContact().getPhone());

                System.out.println(" --------------------------------");
                System.out.println(" 1. Change contact information");
                System.out.println(" 2. Change type");
                System.out.println(" 3. Change Website");
                System.out.println(" 4. Change Description");
                System.out.println(" 5. Remove this Instituition");
                System.out.println(" 6. Back");

                int option = Integer.parseInt(reader.readLine());

                switch (option) {
                    case 1:
                        Contact newContact = assignContact();
                        if (newContact != null) {
                            instituition.setContact(newContact);
                            System.out.println("Contact updated successfully\n");
                        } else {
                            System.out.println("Someting went wrong. Please try again.\n");
                        }
                        break;
                    case 2:
                        changeType(instituition);
                        break;
                    case 3:
                        changeWebsite(instituition);
                        break;
                    case 4:
                        changeDescription(instituition);
                        break;
                    case 5:
                        System.out.println("\nAre you sure you want to remove this instituition? (yes/no)");
                        String answer = reader.readLine();

                        if (answer.equalsIgnoreCase("yes")) {
                            showRemoveInstituition(instituition);
                            exit = true;
                        } else {
                            System.out.println("Removal canceled.");
                        }
                        break;
                    case 6:
                        exit = true;
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");

            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void changeType(Instituition instituition) {
        boolean complete = false;
        while (!complete) {
            System.out.println(" == Available Types == ");
            System.out.println(" 1. UNIVERSITY       2. COMPANY");
            System.out.println(" 3. NGO              4. OTHER");
            System.out.print("Select the type: ");
            try {

                int type = Integer.parseInt(reader.readLine());
                switch (type) {
                    case 1:
                        instituition.setType(InstituitionType.UNIVERSITY);
                        complete = true;
                        break;
                    case 2:
                        instituition.setType(InstituitionType.COMPANY);
                        complete = true;
                        break;
                    case 3:
                        instituition.setType(InstituitionType.NGO);
                        complete = true;
                        break;
                    case 4:
                        instituition.setType(InstituitionType.OTHER);
                        complete = true;
                        break;
                    default:
                        System.out.println("Invalid Selection.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");
            } catch (IOException ex) {
                System.out.println("Error reading imput.");
            }
        }
    }

    private void changeWebsite(Instituition instituition) {

        System.out.println(" == Change website information == ");
        System.out.println(" New Website: ");
        try {
            String website = reader.readLine();

            instituition.setWebsite(website);
            System.out.println("Website updated successfully.\n");

        } catch (IOException ex) {
            System.out.println("Error reading imput.");
        }
    }

    private void changeDescription(Instituition instituition) {

        System.out.println(" == Change description of Instituition == ");
        System.out.println(" New Description: ");
        try {
            String description = reader.readLine();

            instituition.setDescription(description);
            System.out.println("Description updated successfully.\n");

        } catch (IOException ex) {
            System.out.println("Error reading imput.");
        }
    }

    private void showRemoveInstituition(Instituition instituition) {
        boolean complete = false;
        try {
            Instituition revomedInstituition = im.removeInstituition(instituition.getName());
            System.out.println("Instituition removed successfully.");
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
                                jsonObject.put("revomed Instituition", ((InstituitionImp) revomedInstituition).toJsonObj());
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

    private void assignInstituition(Participant p) {

        System.out.println("=== Institutions Selection ===");
        try {
            Instituition[] instituitions = getInstituitionsOutput();
            int i = instituitions.length;

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

    private void showAddInstituition() {
        System.out.println(" == Add Instituition == ");
        try {
            System.out.print("\nName: ");
            String name = reader.readLine();
            System.out.print("\nEmail: ");
            String email = reader.readLine();
            System.out.print("\nWebsite: ");
            String website = reader.readLine();
            System.out.print("\nDescription: ");
            String description = reader.readLine();

            Contact contact = assignContact();

            Instituition instituition = new InstituitionImp(name, email, website, description, contact, null);

            changeType(instituition);

            im.addInstituition(instituition);

            System.out.println("Instituition added successfully.\n");
        } catch (IOException e) {
            System.out.println("Error reading imput.\n");
        } catch (InstituitionAlreadyExistException e) {
            System.out.println(e.getMessage());
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

    private void showAdminParticipantsMenu() {
        boolean exit = false;
        while (!exit) {

            Participant[] participants = listParticipants();

            int counter = participants.length;

            System.out.println("");
            System.out.println((counter + 1) + ". Create a participant");
            System.out.println((counter + 2) + ". Delete participant");
            System.out.println((counter + 3) + ". Back");

            try {
                int participantNumber = Integer.parseInt(reader.readLine());

                // Verifique se o número da tarefa é válido
                if (participantNumber == (counter + 3)) {
                    exit = true;
                } else if (participantNumber == (counter + 2)) {
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
                        showParticipantDetails(participant);
                    } else {
                        System.out.println("Invalid selection");
                    }
                } else if (participantNumber == (counter + 1)) {
                    if (register()) {
                        System.out.println("Registration Success!");
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

    private void showParticipantDetails(Participant participant) {
        boolean exit = false;
        while (!exit) {

            try {

                System.out.println(" === Participant Details ===");
                System.out.println("Name: " + participant.getName());
                System.out.println("Email: " + participant.getEmail());

                if (participant instanceof Facilitator) {
                    System.out.println("Area Of Expertise: " + ((Facilitator) participant).getAreaOfExpertise());
                } else if (participant instanceof Student) {
                    System.out.println("Student Number: " + ((Student) participant).getNumber());
                } else if (participant instanceof Partner) {
                    System.out.print("Vat: " + ((Partner) participant).getVat());
                    System.out.print("WebSite: " + ((Partner) participant).getWebsite());
                }

                System.out.println("Contact Details:");
                System.out.println("Street: " + participant.getContact().getStreet());
                System.out.println("City: " + participant.getContact().getCity());
                System.out.println("State: " + participant.getContact().getState());
                System.out.println("Zip Code: " + participant.getContact().getZipCode());
                System.out.println("Country: " + participant.getContact().getCountry());
                System.out.println("Phone: " + participant.getContact().getPhone());

                System.out.println("Institution Details:");
                if (participant.getInstituition() != null) {
                    System.out.println("Name: " + participant.getInstituition().getName() + " (" + participant.getInstituition().getType().toString() + ")");
                    System.out.println("Email: " + participant.getInstituition().getEmail());
                    System.out.println("Website: " + participant.getInstituition().getWebsite());
                } else {
                    System.out.println("No instituition assigned to this participant!");
                }

                System.out.println(" --------------------------------");
                System.out.println(" 1. Change contact information");
                System.out.println(" 2. Assign to another Instituition");
                System.out.println(" 3. Back");

                int option = Integer.parseInt(reader.readLine());

                switch (option) {
                    case 1:
                        Contact newContact = assignContact();
                        if (newContact != null) {
                            participant.setContact(newContact);
                            System.out.println("Contact updated successfully\n");
                        } else {
                            System.out.println("Someting went wrong. Please try again.\n");
                        }
                        break;
                    case 2:
                        assignInstituition(participant);
                        break;
                    case 3:
                        exit = true;
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");

            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void showParticipantsMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println(" ==== Menu ==== ");
            System.out.println(" 1. My Editions and Projects");
            System.out.println(" 2. My information");
            System.out.println(" 3. Exit");
            System.out.print("Select option: ");

            try {
                int option = Integer.parseInt(reader.readLine());

                switch (option) {
                    case 1:
                        showEditionsMenu();
                        break;
                    case 2:
                        showParticipantDetails(loggedInParticipant);
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid selection. Try again!\n");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");
            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    private void showInstituitionsMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println(" ==== Instituitions Menu ==== ");
            System.out.println(" 1. List instituitions");
            System.out.println(" 2. Add Instituitions");
            System.out.println(" 3. Exit");
            System.out.print("Select option: ");

            try {
                int option = Integer.parseInt(reader.readLine());

                switch (option) {
                    case 1:
                        listInstituitions();
                        break;
                    case 2:
                        showAddInstituition();
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid selection. Try again!\n");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");
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
            System.out.println(" 4. Listings/Reports");
            System.out.println(" 5. Exit");
            System.out.print("Select option: ");

            try {
                int option = Integer.parseInt(reader.readLine());

                switch (option) {
                    case 1:
                        showAdminEditionsMenu();
                        break;
                    case 2:
                        showAdminParticipantsMenu();
                        break;
                    case 3:
                        showInstituitionsMenu();
                        break;
                    case 4:
                        showListiongsMenu();
                        break;
                    case 5:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid selection. Try again!\n");
                        break;
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
            System.out.println((i + 2) + ". Project progress");
            System.out.println((i + 3) + ". List participants");
            System.out.println((i + 4) + ". Add participants ("
                    + (currentProject.getNumberOfParticipants() == currentProject.getMaximumNumberOfParticipants() ? "Not Available" : " Available") + ")");
            System.out.println((i + 5) + ". Add Task ("
                    + (currentProject.getNumberOfTasks() == currentProject.getMaximumNumberOfTasks() ? "Not Available" : " Available") + ")");
            // System.out.println(". Remove Task");
            System.out.println("\n" + (i + 6) + ". Back");
            System.out.print("Select an option: ");
            try {
                int taskNumber = Integer.parseInt(reader.readLine());

                // Verifique se o número da tarefa é válido
                if (taskNumber == (i + 6)) {
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
                    showProjectProgress();
                } else if (taskNumber == (i + 3)) {
                    if (currentProject.getNumberOfParticipants() > 0) {
                        listParticipantsOfProject();
                    } else {
                        System.out.println("No participants in project. Add participants");
                    }
                } else if (taskNumber == (i + 4)) {
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

                } else if (taskNumber == (i + 5)) {
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

    private void showProjectProgress() {
        boolean exit = false;

        System.out.println("\n === Project progress ===");
        System.out.println(currentProject.toString());
        System.out.println("\n");
        System.out.println("1. Back");
        while (!exit) {
            try {
                System.out.print("Select option: ");
                int option = Integer.parseInt(reader.readLine());
                if (option == 1) {
                    exit = true;
                } else {
                    System.out.println("Invalid Selection");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (IOException ex) {
                System.out.println("Error reading imput!");
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

//lists/reports
    private void showListiongsMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println(" ==== Listings/Reports ==== ");
            System.out.println(" 1. Top Participants by number of participation in projects");
            System.out.println(" 2. Top Students by number of task's submissions");
            System.out.println(" 3. Instituitions Participants and Number of Projects");
            System.out.println(" 4. Exit");
            System.out.print("Select option: ");

            try {
                int option = Integer.parseInt(reader.readLine());

                switch (option) {
                    case 1:
                        showTopParticipants();
                        break;
                    case 2:
                        showTopStudentsBySubmissions();
                        break;
                    case 3:
                        showInstituitionsParticipants();
                        break;
                    case 4:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid selection. Try again!\n");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");
            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }

    /**
     * This method retrieves an array of the top participants based on their
     * participation in projects. Participants are sorted in descending order
     * based on the number of projects they participated in. Returns a maximum
     * of 5 participants, or fewer if there are fewer than 5 participants.
     *
     * @return An array of the top participants based on participation in
     * projects.
     */
    private Participant[] getTopParticipantsByParticipation() {
        // sort participants by the number of projects they participated in, in descending order
        Participant[] participants = pm.getParticipants();

        // order the participants 
        for (int i = 0; i < participants.length - 1; i++) {
            for (int j = 0; j < participants.length - i - 1; j++) {
                if (cbl.getProjectsOf(participants[j]).length < cbl.getProjectsOf(participants[j + 1]).length) {
                    // Swap participants at positions 
                    Participant temp = participants[j];
                    participants[j] = participants[j + 1];
                    participants[j + 1] = temp;
                }
            }
        }

        // set the top 5 participants
        int count = Math.min(5, participants.length);
        Participant[] topParticipants = new Participant[count];
        for (int i = 0; i < count; i++) {
            topParticipants[i] = participants[i];
        }

        return topParticipants;
    }

    /**
     * Displays the list of top participants by the number of projects they have
     * participated in. The method prompts the user to select an option and
     * allows them to go back or exit the menu. This method keeps displaying the
     * list until the user chooses to exit.
     */
    private void showTopParticipants() {
        boolean exit = false;
        while (!exit) {

            System.out.println(" === List of Top Participants by number of Projects === ");
            Participant[] topParticipants = getTopParticipantsByParticipation();
            if (topParticipants != null && topParticipants.length > 0) {
                try {
                    int i = 0;
                    for (Participant participant : topParticipants) {
                        System.out.println(++i + ". " + participant.getName() + "(" + participant.getEmail() + ")");
                    }
                    System.out.println(" ---------------------------------------------\n");
                    System.out.println((i + 1) + ". Back");
                    System.out.print("Select the Back number: ");
                    int option = Integer.parseInt(reader.readLine());
                    if (option == i + 1) {
                        exit = true;
                    } else {
                        System.out.println("Invalid Selection.");
                    }
                } catch (IOException e) {
                    System.out.println("Error reading input.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.\n\n");
                }
            } else {
                System.out.println("No participants found!\n");
                exit = true;
            }
        }
    }

    private Student[] getTopStudentsBySubmissions() {
        // Get all students
        Student[] students = pm.getStudents();

        // order students by the number of submissions, in descending order
        for (int i = 0; i < students.length - 1; i++) {
            for (int j = 0; j < students.length - i - 1; j++) {
                Submission[] submissions1 = ((CBLImp) cbl).getSubmissionsOf(students[j]);
                Submission[] submissions2 = ((CBLImp) cbl).getSubmissionsOf(students[j + 1]);

                int submissionCount1 = (submissions1 != null) ? submissions1.length : 0;
                int submissionCount2 = (submissions2 != null) ? submissions2.length : 0;

                if (submissionCount1 < submissionCount2) {
                    // Swap students at positions
                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                }
            }
        }

        return students;
    }

    private void showTopStudentsBySubmissions() {
        Student[] topStudents = getTopStudentsBySubmissions();
        boolean exit = false;
        while (!exit) {

            System.out.println("=== Top Students by Submissions ===");

            if (topStudents != null && topStudents.length > 0) {
                System.out.println("Top 3 Students with the Most Submissions:");
                for (int i = 0; i < Math.min(3, topStudents.length); i++) {
                    try {
                        System.out.println(" -- " + topStudents[i].getName()
                                + " (" + topStudents[i].getEmail() + ")" + "--> "
                                + ((CBLImp) cbl).getSubmissionsOf(topStudents[i]).length + " submissions");
                    } catch (NullPointerException e) {
                        System.out.println(" -- " + topStudents[i].getName()
                                + " (" + topStudents[i].getEmail() + ")" + "--> "
                                + "0 submissions");
                    }
                }

                System.out.println();

                System.out.println("Top 3 Students with the Least Submissions:");
                for (int i = topStudents.length - 1; i >= Math.max(0, topStudents.length - 3); i--) {
                    try {

                        System.out.println(" -- " + topStudents[i].getName()
                                + " (" + topStudents[i].getEmail() + ")" + "--> "
                                + ((CBLImp) cbl).getSubmissionsOf(topStudents[i]).length + " submissions");
                    } catch (NullPointerException e) {
                        System.out.println(" -- " + topStudents[i].getName()
                                + " (" + topStudents[i].getEmail() + ")" + "--> "
                                + "0 submissions");
                    }

                }

                System.out.println(" ----------------------------------------------------");
                System.out.println(" 1. Back");
                System.out.print("Select option: ");
                try {
                    int option = Integer.parseInt(reader.readLine());
                    if (option == 1) {
                        exit = true;
                    } else {
                        System.out.println("Invalid Selection.");
                    }
                } catch (IOException e) {
                    System.out.println("Error reading input.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.\n\n");
                }

            } else {
                System.out.println("No students found!");
            }
            System.out.println();
        }

    }

    private void getInstituitionsParticipants() {

        try {
            Instituition[] instituitions = im.getInstituitions();
            int counter = 0;
            for (Instituition instituition : instituitions) {
                int numberOfParticipants = 0;
                int totalProjects = 0;
                System.out.println(++counter + ". " + instituition.getName());
                try {
                    for (Participant participant : pm.getParticipants()) {
                        if (participant.getInstituition().equals(instituition)) {
                            System.out.println(" --- " + participant.getEmail());
                            numberOfParticipants++;
                            totalProjects += cbl.getProjectsOf(participant).length;
                        }
                    }

                } catch (NullPointerException e) {

                }
                System.out.println(" --- Total project in which " + instituition.getName() + " takes part: " + totalProjects + "  ---");
            }

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

    }

    private void showInstituitionsParticipants() {
        boolean exit = false;
        while (!exit) {
            getInstituitionsParticipants();
            System.out.println(" ----------------------------------------------------");
            System.out.println(" 1. Back");
            System.out.print("Select option: ");
            try {
                int option = Integer.parseInt(reader.readLine());
                if (option == 1) {
                    exit = true;
                } else {
                    System.out.println("Invalid Selection.");
                }
            } catch (IOException e) {
                System.out.println("Error reading input.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n\n");
            }
        }
    }

    public static void main(String[] args) {

        //    try{
        // Crie uma instância da classe CBL e adicione participantes, edições, projetos, etc.
        ParticipantsManager pm = new ParticipantsManager();
        CBL cbl = new CBLImp();
        InstituitionsManager im = new InstituitionsManager();

//        Contact c = new ContactImp("Street", "city", "state", "zipCode", "country", "phone");
//        Instituition estg = new InstituitionImp("ESTG", "estg@estg.ipp.pt", "estg.ipp.pt", "Escola de Tecnologia e Gestão de Felgueiras", c, InstituitionType.UNIVERSITY);
//        Student p1 = new StudentImp("David Santos", "david@estg.ipp.pt", c, estg);
////            try {
//                cbl.addEdition(new EditionImp("Test edition", LocalDate.of(2023, Month.MARCH, 13), LocalDate.of(2023, Month.JUNE, 17)));
//            } catch (EditionAlreadyInCBL ex) {
//                System.out.println(ex.getMessage());
//            }
//            String[] tags = {"java", "teste"};
//            cbl.getEdition("Test edition").addProject("Project test", "testando projeto", tags);
//            cbl.getEdition("Test edition").getProject("Project test").addParticipant(p1);
//
//        try {
//            pm.addParticipant(p1);
//            im.addInstituition(estg);
//        } catch (AlreadyExistsInArray | InstituitionAlreadyExistException ex) {
//            System.out.println(ex.toString());
//        }
        // import data
        if (pm.importData("src/Files/users.json") && im.importData("src/Files/instituitions.json")) {
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
