/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */
public class PlayerAction 
{
    private final PlayerActionType type;
    // Either moveIndex or pokemonIndex depending on action
    private final int index;
    
    public PlayerAction(PlayerActionType type, int index)
    {
        this.type = type;
        this.index = index;
    }
    
    // Getter methods for type and index
    public PlayerActionType getType()
    {
        return type;
    }
    
    public int getIndex()
    {
        return index;
    }
    
}
