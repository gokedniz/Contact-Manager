package command;

/**
 * Interface for implementing the Command design pattern.
 * 
 * <p>Defines the contract for command objects that can be executed and undone.
 * This pattern allows encapsulation of requests as objects, enabling undo/redo functionality
 * and operation queuing.</p>
 * 
 * @author Project2-Group10
 * @version 1.0
 * @since 1.0
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
