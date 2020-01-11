package DBService;

import Confectionary.Worker;
import Confectionary.Addresses;

import java.util.Scanner;

public class system_out {


    private static final String AUTHOR_INFO = "Author: Vitālija Dovgaļuka 171RDB220 3.grupa 3.kurss";
    private static final String PROGRAM_DESCRIPTION = "Hi! This program is meant for working with Oracle DB";
    private static final String COMMANDS_DESCRIPTION = "You can see all the possible operations below:";
    private static final String EXIT_MESSAGE = "If you want to exit press zero.";
    private static final String INSERT_INTO_WORKERS_COMMAND = "If you want to add a new row into table ‘WORKERS’, press number one.";
    private static final String SELECT_ALL_FROM_WORKERS_COMMAND = "If you want to see all the fields from table ‘WORKERS’, press number two.";
    private static final String NO_SUCH_COMMAND = "There is no such a command!";
    private static final String INPUT = "Enter the number of command: ";
    private static final String DELIMITER = ", ";
    private static final String SAY_GOODBYE = "Bye bye!";
    private static final String STOP = ".";


    public static void shoWelcome() {
        System.out.println(AUTHOR_INFO);
        System.out.println(PROGRAM_DESCRIPTION);
        System.out.println();
    }

    public static void showMain() {
        System.out.println(COMMANDS_DESCRIPTION);
        System.out.println(EXIT_MESSAGE);
        System.out.println(INSERT_INTO_WORKERS_COMMAND);
        System.out.println(SELECT_ALL_FROM_WORKERS_COMMAND);
        System.out.println();
    }

    public static void showGoodbye() {
        System.out.println(SAY_GOODBYE);
        System.out.println();
    }

    public static void showUnknownCommand() {
        System.out.println(NO_SUCH_COMMAND);
        System.out.println();
    }

    public static void showInputMessage() {
        System.out.print(INPUT);
    }

    public static void showNewLine() {
        System.out.println();
    }

    public static void showWorkers(Worker worker) {
        System.out.println("Information about the worker: ");
        System.out.print("Name: " + worker.getName() + DELIMITER);
        System.out.print("Surname: " + worker.getSurname() + DELIMITER);
        System.out.print("Phone_number: " + worker.getPhone_number() + DELIMITER);
        System.out.print("Occupation: " + worker.getOccupation() + DELIMITER);
        System.out.print("Country: " + worker.getAddress().getCountry() + DELIMITER);
        System.out.print("City: " + worker.getAddress().getCity() + DELIMITER);
        System.out.print("Street: " + worker.getAddress().getStreet() + DELIMITER);
        System.out.print("Postal code: " + worker.getAddress().getPostal_code() + STOP);
        System.out.println();
        System.out.println();
    }

    public static Worker getWorkersFromInput(Scanner sc) {
        Worker worker = new Worker();
        Addresses address = new Addresses();

        System.out.println("Enter the information about the worker, please:");
        System.out.print("Id: ");
        worker.setWorker_id(Integer.parseInt(sc.nextLine()));
        System.out.println();
        System.out.print("Name: ");
        worker.setName(sc.nextLine());
        System.out.println();
        System.out.print("Surname: ");
        worker.setSurname(sc.nextLine());
        System.out.println();
        System.out.print("Phone_number: ");
        worker.setPhone_number(Integer.parseInt(sc.nextLine()));
        System.out.println();
        System.out.print("Occupation: ");
        worker.setOccupation(sc.nextLine());
        System.out.println();
        System.out.print("Address Id: ");
        address.setId(Integer.parseInt(sc.nextLine()));
        System.out.println();
        System.out.print("Country: ");
        address.setCountry(sc.nextLine());
        System.out.println();
        System.out.println("Street: ");
        address.setStreet(sc.nextLine());
        System.out.println();
        System.out.print("City: ");
        address.setCity(sc.nextLine());
        System.out.println();
        System.out.print("Postal code: ");
        address.setPostal_code(sc.nextLine());
        System.out.println();
        worker.setAddress(address);

        return worker;
    }

    public static int getIdFromInput(Scanner sc) {
        System.out.println("Worker id: ");
        return Integer.parseInt(sc.nextLine());
    }
}