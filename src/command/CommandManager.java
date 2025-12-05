package command;

import java.util.Stack;

/**
 * Manages command execution and undo history using the Singleton pattern.
 * 
 * <p>This class maintains a history stack of executed commands and provides
 * functionality to execute commands and undo the last executed command.
 * Uses synchronized access to ensure thread-safety for the singleton instance.</p>
 * 
 * @author Project2-Group10
 * @version 1.0
 * @since 1.0
 */
public class CommandManager {
    private static CommandManager instance;
    private Stack<Command> history = new Stack<>();

    private CommandManager() {
    }

    /**
     * Gets the singleton instance of CommandManager.
     * 
     * <p>Lazily initializes the instance on first call with synchronized access.</p>
     * 
     * @return The singleton CommandManager instance.
     */
    public static synchronized CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    /**
     * Executes a command and adds it to the undo history.
     * 
     * @param command The command to execute. Cannot be null.
     * 
     * @see Command#execute()
     */
    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
    }

    /**
     * Undoes the last executed command.
     * 
     * <p>Pops the most recent command from history and calls its undo method.
     * If no commands are in history, returns false without action.</p>
     * 
     * @return true if undo was successful, false if no commands to undo.
     * 
     * @see Command#undo()
     */
    public boolean undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            command.undo();
            return true;
        } else {
            return false;
        }
    }
}
