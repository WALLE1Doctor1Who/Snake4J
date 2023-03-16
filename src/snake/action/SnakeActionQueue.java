/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.action;

import java.util.*;
import java.util.function.Consumer;
import snake.*;

/**
 *
 * @author Milo Steier
 */
public class SnakeActionQueue extends ArrayDeque<Consumer<Snake>>{
    
    private final Snake snake;
    
    public SnakeActionQueue(Snake snake){
        super();
        this.snake = Objects.requireNonNull(snake);
    }
    
    public SnakeActionQueue(Snake snake,Collection<? extends Consumer<Snake>>c){
        super(c);
        this.snake = Objects.requireNonNull(snake);
    }
    
    public Snake getSnake(){
        return snake;
    }
    
    protected Consumer<Snake> getActionForCommand(SnakeCommand command){
        Consumer<Snake> action = SnakeCommand.getActionForCommand(command);
        if (action == null)
            throw new IllegalArgumentException(
                    "No action available for command " + command);
        return action;
    }
    
    public void addFirst(SnakeCommand command){
        addFirst(getActionForCommand(command));
    }
    
    public void addLast(SnakeCommand command){
        addLast(getActionForCommand(command));
    }
    
    public boolean add(SnakeCommand command){
        return add(getActionForCommand(command));
    }
    
    public boolean offerFirst(SnakeCommand command){
        return offerFirst(getActionForCommand(command));
    }
    
    public boolean offerLast(SnakeCommand command){
        return offerLast(getActionForCommand(command));
    }
    
    public boolean offer(SnakeCommand command){
        return offer(getActionForCommand(command));
    }
    
    public void push(SnakeCommand command){
        push(getActionForCommand(command));
    }
    
    protected boolean willSkipAction(Consumer<Snake> action,boolean skipRepeats){
        if (action == null)                     // If the given action is null
            return true;
        else if (skipRepeats){                  // If this is to skip repeats
                // Peek at the next action in the queue
            Consumer<Snake> next = peek();    
                // If the next action is the same as the given action
            if (action.equals(next))    
                return true;
        }   // If the action is a SnakeActionCommand
        if (action instanceof SnakeActionCommand){
                // Get the command for the action
            SnakeCommand cmd = ((SnakeActionCommand)action).getCommand();
            if (cmd == null)                // If the command is somehow null
                return true;
            if (snake.getTail() != null){   // If the snake has a tail
                    // Get the direction for the command
                Integer dir = SnakeCommand.getDirectionOf(cmd);
                    // If the command has a direction and that direction is 
                    // opposite to the direction the snake is facing
                if (dir != null && dir == SnakeUtilities.invertDirections(
                        snake.getDirectionFaced()))
                    return true;
            }
            switch(cmd){            // Determine which command is this
                case MOVE_UP:       // If the command is the move up command
                case MOVE_DOWN:     // If the command is the move down command
                case MOVE_LEFT:     // If the command is the move left command
                case MOVE_RIGHT:    // If the command is the move right command
                case MOVE_FORWARD:  // If the command is the move forward command
                case ADD_UP:        // If the command is the add up command
                case ADD_DOWN:      // If the command is the add down command
                case ADD_LEFT:      // If the command is the add left command
                case ADD_RIGHT:     // If the command is the add right command
                case ADD_FORWARD:   // If the command is the add forward command
                        // Skip the action if the snake crashed
                    return snake.isCrashed(); 
                case FLIP:          // If the command is the flip command
                    return false;   // Don't skip actions to flip
                case REVIVE:        // If the command is the revive command
                        // Skip the action if the snake hasn't crashed
                    return !snake.isCrashed();
                case REMOVE_TAIL:   // If the command is the remove tail command
                        // Skip the action if the snake doesn't have a tail
                    return snake.getTail() == null;   
                case DEFAULT_ACTION:// If the command is the default action command
                        // Skip the action if the default action is null or 
                        // disabled
                    return snake.getDefaultAction()==null||
                            !snake.isDefaultActionEnabled();
                default:            // The command must be an unknown command
                    return true;    // Just skip this action, since it's unknown
            }
        }
        return false;
    }
    
    public boolean willSkipNextAction(){
        return willSkipAction(peek(),false);
    }
    
    public Consumer<Snake> pollNext(){
            // This will get the next action for the snake to perform
        Consumer<Snake> action;
        do{ // A do while loop to get the next action to perform
            action = poll();    // Poll the next action in the queue
        }   // While the queue is not empty and the action should be skipped
        while (!isEmpty() && willSkipAction(action,
                snake.getSkipsRepeatedActions()));
            // If the retrieved action should be skipped, return null. 
            // Otherwise, return the action to perform (this accounts for 
            // whether the action was the last action in the queue, yet should 
            // still be skipped)
        return (willSkipAction(action,snake.getSkipsRepeatedActions())) ? null : 
                action;
    }
}
