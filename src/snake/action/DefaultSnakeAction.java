/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.action;

import java.util.Objects;
import java.util.function.Consumer;
import snake.Snake;
import snake.SnakeConstants;

/**
 *
 * @author Milo Steier
 */
public class DefaultSnakeAction implements Consumer<Snake>, SnakeConstants{
    
    private SnakeCommand cmd;
    
    public DefaultSnakeAction(SnakeCommand command){
        DefaultSnakeAction.this.setSnakeCommand(command);
    }
    
    public SnakeCommand getSnakeCommand(){
        return cmd;
    }
    
    public void setSnakeCommand(SnakeCommand command){
        cmd = Objects.requireNonNull(command);
    }

    @Override
    public void accept(Snake snake) {
        if (snake != null)
            snake.doCommand(getSnakeCommand());
    }
    
    public Consumer<Snake> andThen(SnakeCommand command){
        return andThen(new DefaultSnakeAction(command));
    }
    
    @Override
    public String toString(){
        return getClass().getName() + "["+getSnakeCommand()+"]";
    }
 
}
