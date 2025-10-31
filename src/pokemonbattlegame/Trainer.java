/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dilro
 */
public class Trainer extends Character
{
    private String username;
    private int score;
    private String challengeMessage;
    
    public Trainer (String username, String name, int score, int level, Pokemon starterPokemon, String challengeMessage)
    {
        super(name, level);
        this.username = username;
        this.score = score;
        this.setStarterPokemon(starterPokemon);
        this.challengeMessage = challengeMessage;
    }
    
    public Trainer(String username, String name, String challengeMessage)
    {
        super(name, 1);
        this.username = username;
        this.score = 0;
        this.challengeMessage = challengeMessage;
        this.setTeam(new ArrayList<>());
    }
    
    // Implement dialogue of battle challenge message 
    @Override 
    public void speak()
    {
        System.out.println(getName() + ": " + challengeMessage);
    }
    
    // Getter and setter methods
    public ArrayList<Pokemon> getTeam()
    {
        return team;
    }
    
    public void setStarterPokemon(Pokemon starterPokemon)
    {
        this.starterPokemon = starterPokemon;
    }
    
    public Pokemon getStarterPokemon()
    {
        return starterPokemon;
    }
    
    public String getChallengeMessage()
    {
        return challengeMessage;
    }
    
    public void setChallengeMessage(String challengeMessage)
    {
        this.challengeMessage = challengeMessage;
    }

    public String getUsername() 
    {
        return username;
    }

    public int getScore() 
    {
        return score;
    }

    public void setScore(int score) 
    {
        this.score = score;
    }
    
    public void setTeam(ArrayList<Pokemon> team) 
    {
        this.team = team;
    }
}
