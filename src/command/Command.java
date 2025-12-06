package command;

/**
 * Interface for implementing the Command design pattern.
 * 
 * <p>Defines the contract for command objects that can be executed and undone.
 * Enables encapsulation of requests as objects and supports undo/redo functionality.</p>
 * 
 * @author Group 10
 * @version 1.0
 */
public interface Command {
    /**
     * Executes the command action.
     */
    void execute();

    /**
     * Reverts/undoes the command action.
     */
    void undo();
}
