/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.action;

import java.util.Objects;
import java.util.function.Consumer;
import snake.Snake;

/**
 * This is a default implementation of {@link SnakeActionCommand 
 * SnakeActionCommand} which can be set to perform a {@link SnakeCommand 
 * command} on a given {@link Snake snake} when {@link #accept accept} is 
 * invoked.
 * @author Milo Steier
 * @see Snake
 * @see SnakeCommand
 * @see SnakeActionCommand
 * @see SnakeCommand#getCommandActionMap 
 * @see SnakeCommand#getActionForCommand 
 * @see Snake#doCommand 
 * @see #accept 
 */
public class DefaultSnakeActionCommand extends SnakeActionCommand {
    /**
     * This stores the command that a snake is given when {@link #accept 
     * accept} is called.
     */
    private SnakeCommand cmd;
    /**
     * This constructs a DefaultSnakeActionCommand with the given command for a 
     * {@link Snake snake} to perform when the {@link #accept accept} method is 
     * invoked.
     * @param command The command to provide to the snake when performing this 
     * operation (cannot be null).
     * @throws NullPointerException If the command is null.
     */
    public DefaultSnakeActionCommand(SnakeCommand command){
        DefaultSnakeActionCommand.this.setCommand(command);
    }
    /**
     * {@inheritDoc }
     * @see #setCommand 
     * @see Snake#doCommand 
     * @see #accept
     */
    @Override
    public SnakeCommand getCommand(){
        return cmd;
    }
    /**
     * This sets the command for a {@link Snake snake} to perform when the 
     * {@link #accept accept} method is invoked. 
     * @param command The command to provide to the snake when performing this 
     * operation (cannot be null).
     * @throws NullPointerException If the command is null.
     * @see #getCommand 
     * @see Snake#doCommand 
     * @see #accept
     */
    public void setCommand(SnakeCommand command){
        cmd = Objects.requireNonNull(command);
    }
    /**
     * {@inheritDoc }
     * @see #andThen(Consumer) 
     * @see #getCommand 
     * @see #accept 
     * @see Snake#doCommand 
     */
    @Override
    public Consumer<Snake> andThen(SnakeCommand after){
        return andThen(new DefaultSnakeActionCommand(after));
    }
}
