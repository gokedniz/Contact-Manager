import ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        // Update passwords to hash (One-time setup for provided SQL data)
        // com.cmpe343.project.util.DatabaseSetup.updatePasswordsToHash();

        // Start the Console UI
        new ConsoleUI().start();
    }
}
