import java.util.Scanner;

public class Main {

    private static final String IO_ERROR_MESSAGE = "Error: impossible to read data prom the console.";

    public static void main(String[] args) {
        system_out.shoWelcome();
        try(Scanner sc = new Scanner(System.in)) {
            int choice = Integer.MAX_VALUE;
            while (choice != 0) {
                system_out.showAgenda();
                system_out.showInputMessage();
                String inputStr = sc.nextLine();
                choice = Integer.parseInt(inputStr);
                system_out.showNewLine();
                switch (choice) {
                    case 0: {
                        system_out.showGoodbye();
                        break;
                    }
                    case 1: {
                        if (database_connection.insertIntoWorkersTable(system_out.getWorkersFromInput(sc))) {
                            system_out.showNewLine();
                            System.out.println("A new worker info has been created.");
                        } else {
                            System.out.println("Error.");
                        }
                        break;
                    }
                    case 2: {
                        database_connection.getAllRowsFromWorkers().forEach(system_out::showWorker);
                        break;
                    }
                    case 3: {
                        if (database_connection.deleteWorkers(system_out.getIdFromInput(sc))) {
                            system_out.showNewLine();
                            System.out.println("Worker were deleted");
                        } else {
                            System.out.println("Error");
                        }
                        break;
                    }
                    default: system_out.showUnknownCommand();
                }
            }
        } catch (Exception e) {
            System.err.println(IO_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
