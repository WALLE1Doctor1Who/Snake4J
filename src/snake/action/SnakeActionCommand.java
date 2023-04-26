/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package snake.action;

import java.util.Objects;
import java.util.function.Consumer;
import snake.*;

/**
 * This is an abstract implementation of a consumer that can be used on a {@link 
 * Snake snake} to perform some action based off a {@link SnakeCommand command}. 
 * When the {@link #accept accept} method is invoked with a non-null snake, it  
 * will call the snake's {@link Snake#doCommand doCommand} method with the 
 * command returned by the {@link #getCommand getCommand} method. If the {@code 
 * accept} method is invoked with a null snake, then it will do nothing.
 * @author Milo Steier
 * @see Snake
 * @see SnakeCommand
 * @see DefaultSnakeActionCommand
 * @see SnakeCommand#getCommandActionMap 
 * @see SnakeCommand#getActionForCommand 
 * @see Snake#doCommand 
 * @see #accept 
 * @see #getCommand 
 */
public abstract class SnakeActionCommand implements Consumer<Snake>,SnakeConstants{
    /**
     * This constructs a SnakeActionCommand.
     */
    public SnakeActionCommand(){ }
    /**
     * This returns the command for a {@link Snake snake} to perform when the 
     * {@link #accept accept} method is invoked. 
     * @return The command for the action that a snake should perform. This 
     * should never be null.
     * @see Snake#doCommand 
     * @see #accept
     */
    public abstract SnakeCommand getCommand();
    /**
     * This performs this operation on the given snake. 
     * 
     * @implSpec The default implementation calls {@link Snake#doCommand 
     * doCommand} on the given snake with the command returned by {@link 
     * #getCommand getCommand}. If the given snake is null, then this will do 
     * nothing.
     * 
     * @param snake The snake to perform the operation on.
     * @see SnakeCommand
     * @see Snake#doCommand 
     * @see #getCommand
     */
    @Override
    public void accept(Snake snake) {
        if (snake != null)  // If the snake is not null
            snake.doCommand(getCommand());
    }
    /**
     * This returns a composed {@code Consumer} that performs, in sequence, this 
     * operation followed by an operation that will have a snake perform the 
     * given command. If performing either operation throws an exception, then 
     * the exception will be relayed to the caller of the composed operation. If 
     * performing this operation throws an exception, then the operation to 
     * perform the {@code after} command will not be performed. <p>
     * 
     * This is equivalent to calling {@link #andThen(Consumer) andThen} with a 
     * {@code SnakeActionCommand} that returns {@code after} as the {@link 
     * #getCommand command} that a snake will {@link Snake#doCommand perform} 
     * when its {@link #accept accept} method is invoked.
     * 
     * @param after The command for the operation to perform after this 
     * operation (cannot be null).
     * @return A composed {@code Consumer} that performs, in sequence, this 
     * operation followed by an operation to perform the {@code after} command.
     * @throws NullPointerException If {@code after} is null.
     * @see #andThen(Consumer) 
     * @see #getCommand 
     * @see #accept 
     * @see Snake#doCommand 
     */
    public Consumer<Snake> andThen(SnakeCommand after){
        if (after == null)    // If the command is null
            throw new NullPointerException();
        return andThen(new SnakeActionCommand(){
            /**
             * This stores the snake command to run next.
             */
            final SnakeCommand cmd = after;
            @Override
            public SnakeCommand getCommand() {
                return cmd;
            }
        });
    }
    /**
     * This returns a String representation of this action. This method is 
     * primarily intended to be used only for debugging purposes, and the 
     * content and format of the returned String may vary between 
     * implementations.
     * @return A String representation of this action.
     */
    protected String paramString(){
        return getCommand().toString();
    }
    /**
     * This returns a String representation of this action and its values.
     * @return A String representation of this action.
     */
    @Override
    public String toString(){
        return getClass().getName() + "["+paramString()+"]";
    }
    /**
     * This compares the given object with this operation and returns whether 
     * the given object is a {@code SnakeActionCommand} object with the same 
     * {@link #getCommand command} as this operation.
     * @param obj The object to be compared with.
     * @return Whether the object is an operation that is the same as this 
     * operation.
     * @see #getCommand 
     */
    @Override
    public boolean equals(Object obj){
        if (obj == this)    // If the object is this SnakeActionCommand
            return true;
            // If the object is a SnakeActionCommand
        else if (obj instanceof SnakeActionCommand)
                // Return whether their commands are the same
            return Objects.equals(getCommand(),((SnakeActionCommand)obj).getCommand());
        return false;
    }
    /**
     * This returns the hash code value for this {@code SnakeActionCommand} 
     * object.
     * @return The hash code for this operation.
     */
    @Override
    public int hashCode() {
        int hash = 5;   // This gets the hash code
        hash = 73 * hash + Objects.hashCode(getCommand());
        return hash;
    }
}
