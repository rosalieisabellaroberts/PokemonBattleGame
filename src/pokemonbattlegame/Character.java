/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.util.ArrayList;

/**
 *
 * @author dilro
 */
public abstract class Character
{
    protected String name;
    protected int level;
    protected ArrayList<Pokemon> team = new ArrayList<>();
    protected Pokemon starterPokemon;
    
    public Character(String name, int level)
    {
        this.name = name;
        this.level = level;
    }
    
    // Implement dialgoue for character subclasses 
    public abstract String speak();
    
    // Getter and setter methods
    public String getName()
    {
        return name;
    }
    
    public int getLevel()
    {
        return level;
    }
    
    public ArrayList<Pokemon> getTeam()
    {
        return team;
    }
    
    public Pokemon getStarterPokemon()
    {
        return starterPokemon;
    }
    
    public void setStarterPokemon(Pokemon pokemon)
    {
        this.starterPokemon = pokemon;
    }
    
    public void setTeam(ArrayList<Pokemon> team)
    {
        this.team = team;
    }
    
}
