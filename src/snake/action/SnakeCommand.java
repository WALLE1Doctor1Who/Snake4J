/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package snake.action;

import snake.SnakeConstants;
/**
 *
 * @author Milo Steier
 */
public enum SnakeCommand implements SnakeConstants{
    
    MOVE_UP,
    
    MOVE_DOWN,
    
    MOVE_LEFT,
    
    MOVE_RIGHT,
    
    MOVE_FORWARD,
    
    ADD_UP,
    
    ADD_DOWN,
    
    ADD_LEFT,
    
    ADD_RIGHT,
    
    ADD_FORWARD,
    
    FLIP,
    
    REVIVE,
    
    REMOVE_TAIL,
    
    DEFAULT_ACTION;
    
    public static Integer getDirectionOf(SnakeCommand cmd){
        switch (cmd){
            case MOVE_UP: 
            case ADD_UP:
                return UP_DIRECTION;
            case MOVE_DOWN:
            case ADD_DOWN:
                return DOWN_DIRECTION;
            case MOVE_LEFT:
            case ADD_LEFT:
                return LEFT_DIRECTION;
            case MOVE_RIGHT:
            case ADD_RIGHT:
                return RIGHT_DIRECTION;
            case MOVE_FORWARD:
            case ADD_FORWARD:
                return 0;
            default:
                return null;
        }
    }
}
