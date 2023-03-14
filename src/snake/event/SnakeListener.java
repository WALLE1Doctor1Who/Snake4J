/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package snake.event;

import java.util.EventListener;
import snake.*;

/**
 * This is the listener interface for receiving snake events. <p>
 * 
 * Classes that are interested in processing snake events implement this 
 * interface, and the instance of that class is registered with a snake by using 
 * the snake's {@code addSnakeListener} method.
 * 
 * @author Milo Steier
 * @see SnakeEvent
 * @see Snake
 */
public interface SnakeListener extends EventListener{
    /**
     * The method invoked by {@link Snake snakes} to notify their listeners of 
     * an action they performed or a change to their state.
     * @param evt The SnakeEvent to be processed.
     */
    public void snakeChanged(SnakeEvent evt);
    
}
