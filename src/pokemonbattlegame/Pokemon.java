/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.util.*;
/**
 *
 * @author dilro
 */
public class Pokemon
{
    private String name;
    private Type type;
    private int HP;
    private int originalHP;
    private ArrayList<Move> moves;
    
    public Pokemon(String name, Type type, int HP, ArrayList<Move> moves)
    {
       this.name = name;
       this.type = type;
       this.HP = HP;
       this.originalHP = HP;
       this.moves = moves;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getHP()
    {
        return HP;
    }
    
    public void setHP(int HP)
    {
        this.HP = HP;
    }
    
    public Type getPokemonType()
    {
        return type;
    }
    
    public void initaliseOriginalHP() 
    {
        // If original HP has not been initalised
        if (this.originalHP == 0) { 
            // Set value of original HP
            this.originalHP = this.HP;
        }
    }
    
    public int getOriginalHP() 
    {
        return originalHP;
    }
    
    public ArrayList<Move> getMoves()
    {
        return moves;
    }
}