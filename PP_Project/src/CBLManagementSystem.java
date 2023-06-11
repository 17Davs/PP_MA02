//
//import CBLStructure.CBL;
//import CBLStructure.CBLImp;
//import java.util.Arrays;
//import java.util.Scanner;
//
//public class CBLManagementSystem {
//
////    private static final String USERNAME = "admin";
////    private static final String PASSWORD = "password";
////    private CBL cbl = new CBLImp();
////
////    public static void main(String[] args) {
////
////        if (login()) {
////            mainMenu();
////        } else {
////            System.out.println("Login failed. Exiting...");
////        }
////    }
////
////    public static boolean login() {
////        Scanner scanner = new Scanner(System.in);
////
////        System.out.println("==== Login ====");
////        System.out.print("Username: ");
////        String username = scanner.nextLine();
////        System.out.print("Password: ");
////        String password = scanner.nextLine();
////
////        return username.equals(USERNAME) && password.equals(PASSWORD);
////    }
////
////    public static void mainMenu() {
////
////        Scanner scanner = new Scanner(System.in);
////        int option;
////
////        do {
////            System.out.println("==== CBL Management Menu ====");
////            System.out.println("1. Manage Edition");
////            System.out.println("2. Manage Participants");
////            System.out.println("3. Logout");
////            System.out.print("Choose an option: ");
////
////            option = scanner.nextInt();
////
////            switch (option) {
////                case 1:
////                    manageEdition();
////                    break;
////                case 2:
////                    manageProjects();
////                    break;
////                case 3:
////                    System.out.println("Logging out...");
////                    break;
////                default:
////                    System.out.println("Invalid option!");
////            }
////
////            System.out.println();
////        } while (option != 3);
////        scanner.close();
////    }
////
////    public static void manageEdition() {
////        Scanner scanner = new Scanner(System.in);
////        int option;
////        do {
////            System.out.println("==== CBL Management Menu ====");
////
////            System.out.println("2. Manage Participants");
////            System.out.println("3. Logout");
////            System.out.println(" --   --");
////            System.out.print("Choose an option: ");
////
////        } while (option != 3);
////        System.out.println("=== Manage Edition ===");
////        // Implement edition management options here
////    }
////
////    public static void manageProjects() {
////        System.out.println("=== Manage Projects ===");
////        // Implement project management options here
////    }
////
////    public static void manageTasks() {
////        System.out.println("=== Manage Tasks ===");
////        // Implement task management options here
////    }
////
////    public static void submitWork() {
////        System.out.println("=== Submit Work ===");
////        // Implement work submission logic here
////    }
////
////    public static void manageParticipants() {
////        System.out.println("=== Manage Participants ===");
////        // Implement participant management options here
////    }
////public static void main(String[] args) {
////        String tagsString = "tag1,tag2,tag3";
////        
////        // Removendo espaços em branco extras antes de dividir a string
////        tagsString = tagsString.trim();
////        
////        // Dividindo a string usando vírgula como separador
////        String[] tagsArray = tagsString.split(",");
////        
////        // Removendo espaços em branco extras após a divisão
//////        for (int i = 0; i < tagsArray.length; i++) {
//////            tagsArray[i] = tagsArray[i].trim();
//////        }
////        
////        System.out.println("Tags array: " + Arrays.toString(tagsArray));
//    }
//}
