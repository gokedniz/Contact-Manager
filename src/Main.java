import ui.ConsoleUI;

/**
 * Entry point for the Contact Manager console application.
 *
 * <p>
 * Initializes and starts the `ConsoleUI` which handles user interaction,
 * authentication, and all application workflows.
 * </p>
 *
 * @author Group 10
 * @version 1.0
 */
public class Main {

    /**
     * Starts the console application.
     *
     * @param args Command-line arguments (ignored by this application).
     */
    public static void main(String[] args) {
        intro.IntroManager.playIntro();
        new ConsoleUI().start();
        ui.ConsoleHelper.animateOutro();
        outro.Eiffel.playOutro();
    }
}
